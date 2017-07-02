package org.ahp.analyse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ahp.analyse.condition.AbstractRankingMode;
import org.ahp.analyse.condition.RankingModeFussy;
import org.ahp.analyse.condition.RankingModeStandard;
import org.ahp.analyse.sbfl.ISBFLRankingMethod;
import org.ahp.structure.ZipkinSpan;
import org.reflections.Reflections;

public class RankingAnalyser {

	public void calculateRankingIndizes(List<ZipkinSpan> spans) {

		HashMap<Long, List<ZipkinSpan>> mapTraceSpans = getTraceSpans(spans);

		System.out.println("\n=== Standard ===");
		AbstractRankingMode standardMode = new RankingModeStandard();
		executeRankingAnalyser(mapTraceSpans, standardMode);

		System.out.println("\n=== Fussy ===");
		AbstractRankingMode fussyMode = new RankingModeFussy();
		executeRankingAnalyser(mapTraceSpans, fussyMode);

	}

	private void executeRankingAnalyser(HashMap<Long, List<ZipkinSpan>> mapTraceSpans, AbstractRankingMode rankingMode) {

		HashMap<Long, RankingParameter> mapRankingParameter = initRankingParametersOfMicroservices(mapTraceSpans);

		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			calculateRankingParameter(entryZipkinSpan.getValue(), mapRankingParameter, rankingMode.checkCondition(entryZipkinSpan.getValue()));
		}

		List<RankingResult> listRankingResults = callRankingMethods(mapRankingParameter.values());
		this.printRankingResults(listRankingResults);
	}

	private HashMap<Long, RankingParameter> initRankingParametersOfMicroservices(HashMap<Long, List<ZipkinSpan>> mapTraceSpans) {
		HashMap<Long, RankingParameter> mapRankingParameter = new HashMap<Long, RankingParameter>();

		// Create for all microservices ranking parameters
		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			for (ZipkinSpan zipkinSpan : entryZipkinSpan.getValue()) {
				mapRankingParameter.put(zipkinSpan.getEndpoint_ipv4(), new RankingParameter(zipkinSpan.getEndpoint_ipv4()));
			}
		}

		return mapRankingParameter;
	}

	private HashMap<Long, List<ZipkinSpan>> getTraceSpans(List<ZipkinSpan> spans) {
		HashMap<Long, List<ZipkinSpan>> mapTraceSpans = new HashMap<Long, List<ZipkinSpan>>();
		for (ZipkinSpan zipkinSpan : spans) {

			Long traceID = zipkinSpan.getTrace_id();
			if (traceID == null) {
				throw new IllegalArgumentException("TraceID is null!");
			}

			if (mapTraceSpans.containsKey(traceID)) {
				mapTraceSpans.get(traceID).add(zipkinSpan);
			} else {
				List<ZipkinSpan> tempSpanList = new ArrayList<ZipkinSpan>();
				tempSpanList.add(zipkinSpan);
				mapTraceSpans.put(traceID, tempSpanList);
			}
		}
		return mapTraceSpans;
	}

	private void calculateRankingParameter(List<ZipkinSpan> listSpans, HashMap<Long, RankingParameter> mapAllParameter, double fussyValue) {

		List<Long> listMicroserviceID = new ArrayList<Long>();

		// Check 'execute'
		for (ZipkinSpan zipkinSpan : listSpans) {

			long microserviceId = zipkinSpan.getEndpoint_ipv4();

			// Use each microservice only once in a trace
			if (listMicroserviceID.contains(microserviceId)) {
				continue;
			}
			listMicroserviceID.add(microserviceId);

			RankingParameter currentMicroservice = mapAllParameter.get(microserviceId);

			currentMicroservice.increaseExecute(1 - fussyValue);
		}

		// Check 'not execute'
		for (Entry<Long, RankingParameter> entryRankingParameter : mapAllParameter.entrySet()) {

			long microserviceId = entryRankingParameter.getValue().getMicroserviceId();

			// Use each microservice only once
			if (listMicroserviceID.contains(microserviceId)) {
				continue;
			}
			listMicroserviceID.add(microserviceId);

			RankingParameter currentMicroservice = mapAllParameter.get(microserviceId);

			currentMicroservice.increaseNotExecute(1 - fussyValue);
		}
	}

	private List<RankingResult> callRankingMethods(Collection<RankingParameter> listRankingParameter) {

		Reflections reflections = new Reflections("org.ahp.analyse.sbfl");
		Set<Class<? extends ISBFLRankingMethod>> allRankingMethods = reflections.getSubTypesOf(ISBFLRankingMethod.class);

		List<RankingResult> listRankingResults = new ArrayList<RankingResult>();

		for (RankingParameter rankingParameter : listRankingParameter) {

			RankingResult rankingResult = new RankingResult(rankingParameter.getMicroserviceId(), rankingParameter);
			listRankingResults.add(rankingResult);

			for (Class<? extends ISBFLRankingMethod> method : allRankingMethods) {
				try {
					double result = callRankingMethod(method.newInstance(), rankingParameter);
					rankingResult.addRankingResult(method.getSimpleName(), result);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return listRankingResults;
	}

	private double callRankingMethod(ISBFLRankingMethod rankingTechnique, RankingParameter rankingParameter) {
		return rankingTechnique.calculateRankingIndex(rankingParameter);
	}

	private void printRankingResults(List<RankingResult> listRankingResults) {

		System.out.println("\n=== SBFL techniques ===");

		for (RankingResult rankingResult : listRankingResults) {

			System.out.println(rankingResult.getRankingParameter());
			System.out.print(String.format("Microservice-ID: %d, ", rankingResult.getMicroserviceId()));
			for (Entry<String, Double> result : rankingResult.getRankingResult().entrySet()) {
				System.out.print(String.format("%s: %.5f, ", result.getKey(), result.getValue()));
			}

			System.out.println("\n");
		}

		System.out.println("\n=== SBFL techniques (as CSV) ===");

		System.out.print("Microservice-ID; ");

		for (String rankingMethod : listRankingResults.get(0).getRankingResult().keySet()) {
			System.out.print(rankingMethod + "; ");
		}

		System.out.println();

		for (RankingResult rankingResult : listRankingResults) {

			System.out.print(rankingResult.getMicroserviceId() + "; ");

			// Same order
			for (String rankingMethod : rankingResult.getRankingResult().keySet()) {
				double result = rankingResult.getRankingResult().get(rankingMethod);
				System.out.print(String.format("%.5f; ", result));
			}

			System.out.println();
		}
	}
}
