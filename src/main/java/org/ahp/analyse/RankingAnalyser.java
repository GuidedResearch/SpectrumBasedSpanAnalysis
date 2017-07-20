package org.ahp.analyse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ahp.analyse.condition.AbstractRankingMode;
import org.ahp.analyse.condition.RankingModeFuzzy;
import org.ahp.analyse.condition.RankingModeStandard;
import org.ahp.analyse.metrics.ISBFLRankingMethod;
import org.ahp.structure.ZipkinSpan;
import org.reflections.Reflections;

/**
 * Analyzes ZipkinSpans with SBFL metrics.
 */
public class RankingAnalyser {

	private static final String PACKAGE_OF_SBFL_METRICS = "org.ahp.analyse.metrics";

	/**
	 * Calculate the suspiciousness of the provided ZipkinSpans for each
	 * implemented SBFL metric.
	 * 
	 * @param mapTraceSpans
	 *            TraceID with the corresponding spans.
	 * @param rankingMode
	 *            Use {@link RankingModeStandard} or {@link RankingModeFuzzy} .
	 * @return List of all SBFL metrics and there results of the provided
	 *         ZipkinSpans.
	 */
	public List<RankingResult> calculateSuspiciousness(HashMap<Long, List<ZipkinSpan>> mapTraceSpans, AbstractRankingMode rankingMode) {

		HashMap<String, RankingParameter> mapRankingParameter = initRankingParametersOfMicroservices(mapTraceSpans);

		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			double conditionResult = rankingMode.checkCondition(entryZipkinSpan.getValue());
			this.calculateRankingParameter(entryZipkinSpan.getValue(), mapRankingParameter, conditionResult);
		}

		return this.callRankingMethods(mapRankingParameter.values());
	}

	private HashMap<String, RankingParameter> initRankingParametersOfMicroservices(HashMap<Long, List<ZipkinSpan>> mapTraceSpans) {
		HashMap<String, RankingParameter> mapRankingParameter = new HashMap<String, RankingParameter>();

		// Create for all microservices ranking parameters
		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			for (ZipkinSpan zipkinSpan : entryZipkinSpan.getValue()) {
				mapRankingParameter.put(zipkinSpan.getEndpoint_service_name(), new RankingParameter(zipkinSpan.getEndpoint_service_name()));
			}
		}

		return mapRankingParameter;
	}

	private void calculateRankingParameter(List<ZipkinSpan> listSpans, HashMap<String, RankingParameter> mapAllParameter, double fussyValue) {

		List<String> listMicroserviceID = new ArrayList<String>();

		// Check 'execute'
		for (ZipkinSpan zipkinSpan : listSpans) {

			String microserviceId = zipkinSpan.getEndpoint_service_name();

			// Use each microservice only once in a trace
			if (listMicroserviceID.contains(microserviceId)) {
				continue;
			}
			listMicroserviceID.add(microserviceId);

			RankingParameter currentMicroservice = mapAllParameter.get(microserviceId);

			currentMicroservice.increaseExecute(1 - fussyValue);
		}

		// Check 'not execute'
		for (Entry<String, RankingParameter> entryRankingParameter : mapAllParameter.entrySet()) {

			String microserviceId = entryRankingParameter.getValue().getMicroserviceId();

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

		Reflections reflections = new Reflections(PACKAGE_OF_SBFL_METRICS);
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

}
