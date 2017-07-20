package org.ahp.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ahp.analyse.RankingResult;

/**
 * Evaluation from RankingResults with Hit@X.
 */
public class SBFLMetricEvaluation {

	private static final int[] HIT_AT_X = { 2, 3 };

	/**
	 * Evaluate the provided RankingResults with Hit@X. The list of microservice
	 * id's with faults is required for the formula of Hit@X.
	 * 
	 * @param listRankingResults
	 *            - Each {@link RankingResult} contains one microservice with
	 *            the results of the SBFL metrics in a map.
	 * @param listMicroservicesWithFaults
	 *            - List of microservice id's.
	 */
	public void evaluateRankingResults(List<RankingResult> listRankingResults, List<String> listMicroservicesWithFaults) {

		if (listRankingResults == null) {
			throw new IllegalArgumentException("List of RankingResults is null!");
		} else if (listMicroservicesWithFaults == null) {
			throw new IllegalArgumentException("List of Microservice Id's is null!");
		}

		HashMap<String, RankingResult> mapResultsOfSBFLMetric = new HashMap<String, RankingResult>();
		for (RankingResult rankingResult : listRankingResults) {
			for (Entry<String, Double> result : rankingResult.getRankingResult().entrySet()) {
				mapResultsOfSBFLMetric.putIfAbsent(result.getKey(), new RankingResult(null, null));
				RankingResult metricResult = mapResultsOfSBFLMetric.get(result.getKey());
				metricResult.addRankingResult(rankingResult.getMicroserviceId(), result.getValue());
			}
		}

		System.out.println("\n=== Evaluation (as CSV) ===");

		System.out.println("Metric; Hit At X; Min; Max; Avg;");
		for (Entry<String, RankingResult> entryRankingResult : mapResultsOfSBFLMetric.entrySet()) {
			for (int hitAtX : HIT_AT_X) {
				int minHitCount = calcMinHitCount(entryRankingResult.getValue(), listMicroservicesWithFaults, hitAtX);
				int maxHitCount = calcMaxHitCount(entryRankingResult.getValue(), listMicroservicesWithFaults, hitAtX);
				int avgHitCount = calcAvgHitCount(entryRankingResult.getValue(), listMicroservicesWithFaults, hitAtX);

				System.out.println(String.format("%s; %d; %d; %d; %d;", entryRankingResult.getKey(), hitAtX, minHitCount, maxHitCount, avgHitCount));
			}
		}
	}

	private int calcMinHitCount(RankingResult rankingResult, List<String> listMicroservicesWithFaults, final int hitAtX) {
		int count = 0;

		for (String microserviceID : listMicroservicesWithFaults) {
			if (calcBestRank(rankingResult, microserviceID) <= hitAtX) {
				count++;
			}
		}

		return count;
	}

	private int calcMaxHitCount(RankingResult rankingResult, List<String> listMicroservicesWithFaults, final int hitAtX) {
		int count = 0;

		for (String microserviceID : listMicroservicesWithFaults) {
			if (calcWorstRank(rankingResult, microserviceID) <= hitAtX) {
				count++;
			}
		}

		return count;
	}

	private int calcAvgHitCount(RankingResult rankingResult, List<String> listMicroservicesWithFaults, final int hitAtX) {
		int count = 0;

		for (String microserviceID : listMicroservicesWithFaults) {
			if (calcAvgRank(rankingResult, microserviceID) <= hitAtX) {
				count++;
			}
		}

		return count;
	}

	private int calcBestRank(RankingResult rankingResult, String microserviceID) {

		double suspiciousnessOfFaultMicroservices = rankingResult.getRankingResult().get(microserviceID);

		int count = 1;
		for (double suspiciousness : rankingResult.getRankingResult().values()) {
			if (suspiciousness > suspiciousnessOfFaultMicroservices) {
				count++;
			}
		}
		return count;
	}

	private int calcWorstRank(RankingResult rankingResult, String microserviceID) {

		double suspiciousnessOfFaultMicroservices = rankingResult.getRankingResult().get(microserviceID);

		int count = 0;
		for (double suspiciousness : rankingResult.getRankingResult().values()) {
			if (suspiciousness >= suspiciousnessOfFaultMicroservices) {
				count++;
			}
		}
		return count;
	}

	private double calcAvgRank(RankingResult rankingResult, String microserviceID) {

		int bestRank = calcBestRank(rankingResult, microserviceID);
		int worstRank = calcWorstRank(rankingResult, microserviceID);
		return 0.5 * (bestRank + worstRank);
	}

}
