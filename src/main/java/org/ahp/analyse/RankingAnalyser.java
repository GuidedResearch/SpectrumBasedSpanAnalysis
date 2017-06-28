package org.ahp.analyse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ahp.analyse.condition.AbstractRankingMode;
import org.ahp.analyse.condition.RankingModeFussy;
import org.ahp.analyse.condition.RankingModeStandard;
import org.ahp.analyse.sbfl.ISBFLRankingMethod;
import org.ahp.analyse.sbfl.Ochiai;
import org.ahp.analyse.sbfl.Ochiai2;
import org.ahp.analyse.sbfl.Tarantula;
import org.ahp.structure.ZipkinSpan;

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

		System.out.println("\n=== SBFL techniques ===");

		callRankingMethods(mapRankingParameter.values());
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

	private void callRankingMethods(Collection<RankingParameter> listRankingParameter) {

		ISBFLRankingMethod[] methods = { new Tarantula(), new Ochiai(), new Ochiai2() };

		for (RankingParameter rankingParameter : listRankingParameter) {

			System.out.println(rankingParameter);
			System.out.print(String.format("Microservice-ID: %d, ", rankingParameter.getMicroserviceId()));

			List<Double> listRankingIndizes = new ArrayList<Double>();
			for (ISBFLRankingMethod method : methods) {
				double rankingIndex = callRankingMethod(method, rankingParameter);
				listRankingIndizes.add(rankingIndex);
				System.out.print(String.format("%s: %.5f, ", method.getClass().getSimpleName(), rankingIndex));
			}
			System.out.println("\n");
		}
	}

	private double callRankingMethod(ISBFLRankingMethod rankingTechnique, RankingParameter rankingParameter) {
		return rankingTechnique.calculateRankingIndex(rankingParameter);
	}
}
