package org.ahp.analyse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ahp.analyse.sbfl.ISBFLRankingMethod;
import org.ahp.analyse.sbfl.Ochiai;
import org.ahp.analyse.sbfl.Ochiai2;
import org.ahp.analyse.sbfl.Tarantula;
import org.ahp.structure.ZipkinSpan;

public class RankingAnalyser {

	private static final int THRESHOLD_DURATION_EACH_TRACE = 2000;
	private static final int THRESHOLD_DURATION_EACH_SPAN_HIGH = 1000;
	private static final int THRESHOLD_DURATION_EACH_SPAN_LOW = (THRESHOLD_DURATION_EACH_SPAN_HIGH / 4);

	public void calculateRankingIndizes(List<ZipkinSpan> spans) {

		HashMap<Long, List<ZipkinSpan>> mapTraceSpans = getTraceSpans(spans);
		HashMap<Long, RankingParameter> mapRankingParameter = new HashMap<Long, RankingParameter>();

		// Create for all microservices ranking parameters
		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			for (ZipkinSpan zipkinSpan : entryZipkinSpan.getValue()) {
				mapRankingParameter.put(zipkinSpan.getEndpoint_ipv4(), new RankingParameter(zipkinSpan.getEndpoint_ipv4()));
			}
		}

		for (Entry<Long, List<ZipkinSpan>> entryZipkinSpan : mapTraceSpans.entrySet()) {
			setRankingParameter(entryZipkinSpan.getValue(), mapRankingParameter, checkCondition(entryZipkinSpan.getValue()));
			System.out.println(String.format("Fussy? %.5f", checkFussyCondition(entryZipkinSpan.getValue())));
		}

		callRankingMethods(mapRankingParameter.values());
	}

	private double checkFussyCondition(List<ZipkinSpan> spans) {

		long duration = getDurationOfTrace(spans);
		double avgSpanDuration = (double) duration / spans.size();

		if (avgSpanDuration >= THRESHOLD_DURATION_EACH_SPAN_HIGH) {
			return 1;
		} else if (avgSpanDuration <= THRESHOLD_DURATION_EACH_SPAN_LOW) {
			return 0;
		}

		return (avgSpanDuration - THRESHOLD_DURATION_EACH_SPAN_LOW) / (THRESHOLD_DURATION_EACH_SPAN_HIGH - THRESHOLD_DURATION_EACH_SPAN_LOW);
	}

	private boolean checkCondition(List<ZipkinSpan> spans) {

		long duration = getDurationOfTrace(spans);
		double avgSpanDuration = (double) duration / spans.size();

		// (duration >= THRESHOLD_DURATION_EACH_TRACE)
		final boolean METRIC_QUERY = (avgSpanDuration >= THRESHOLD_DURATION_EACH_SPAN_HIGH);
		return METRIC_QUERY;
	}

	private long getDurationOfTrace(List<ZipkinSpan> spans) {
		for (ZipkinSpan zipkinSpan : spans) {
			// Find root span
			if (zipkinSpan.getParent_id() == null) {
				return zipkinSpan.getDuration();
			}
		}
		return 0;
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

	private void setRankingParameter(List<ZipkinSpan> listSpans, HashMap<Long, RankingParameter> mapAllParameter, boolean failed) {
		System.out.println("<Trace-Status> failed: " + failed);

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

			if (failed) {
				currentMicroservice.increaceNumberExecuteFailed();
			} else {
				currentMicroservice.increaceNumberExecutePassed();
			}
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

			if (failed) {
				currentMicroservice.increaceNumberNotExecuteFailed();
			} else {
				currentMicroservice.increaceNumberNotExecutePassed();
			}
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
