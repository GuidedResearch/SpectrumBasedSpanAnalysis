package org.ahp.sbfl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ahp.analyse.RankingAnalyser;
import org.ahp.analyse.RankingResult;
import org.ahp.analyse.condition.AbstractRankingMode;
import org.ahp.analyse.condition.RankingModeFuzzy;
import org.ahp.analyse.condition.RankingModeStandard;
import org.ahp.evaluation.SBFLMetricEvaluation;
import org.ahp.structure.ZipkinSpan;

public class AnalyseEvaluationStarter {

	public void runAnalyseAndEvaluation(List<ZipkinSpan> spans, List<String> listMicroservicesWithFaults) {

		if (spans == null) {
			throw new IllegalArgumentException("List of ZipkinSpans is null!");
		} else if (spans.isEmpty()) {
			throw new IllegalArgumentException("List of ZipkinSpans is empty!");
		} else if (listMicroservicesWithFaults == null) {
			throw new IllegalArgumentException("List of Microservices with Faults is null!");
		} else if (listMicroservicesWithFaults.isEmpty()) {
			throw new IllegalArgumentException("List of Microservices with Faults is empty!");
		}

		List<RankingResult> listRankingResultsStandard = this.runAnalyse(spans, (new RankingModeStandard()));
		this.runEvaluation(listRankingResultsStandard, listMicroservicesWithFaults);

		List<RankingResult> listRankingResultsEvaluation = this.runAnalyse(spans, (new RankingModeFuzzy()));
		this.runEvaluation(listRankingResultsEvaluation, listMicroservicesWithFaults);
	}

	public List<RankingResult> runAnalyse(List<ZipkinSpan> spans, AbstractRankingMode rankingMode) {

		HashMap<Long, List<ZipkinSpan>> mapTraceSpans = this.getTracesWithSpans(spans);

		RankingAnalyser analyser = new RankingAnalyser();
		List<RankingResult> listRankingResults = analyser.calculateSuspiciousness(mapTraceSpans, rankingMode);

		System.out.println(String.format("\n=== %s ===", rankingMode.getRankingModeName()));
		this.printRankingResults(listRankingResults);

		return listRankingResults;
	}

	public void runEvaluation(List<RankingResult> listRankingResults, List<String> listMicroservicesWithFaults) {

		SBFLMetricEvaluation evaluation = new SBFLMetricEvaluation();
		evaluation.evaluateRankingResults(listRankingResults, listMicroservicesWithFaults);
	}

	private HashMap<Long, List<ZipkinSpan>> getTracesWithSpans(List<ZipkinSpan> spans) {
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

	private void printRankingResults(List<RankingResult> listRankingResults) {

		System.out.println("\n=== SBFL techniques ===");

		for (RankingResult rankingResult : listRankingResults) {

			System.out.println(rankingResult.getRankingParameter());
			System.out.print(String.format("Microservice-ID: %s, ", rankingResult.getMicroserviceId()));
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
