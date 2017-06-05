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
	private static final int THRESHOLD_DURATION_EACH_SPAN = 200;

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
			long duration = 0;

			// Find root span
			for (ZipkinSpan zipkinSpan : entryZipkinSpan.getValue()) {
				if (zipkinSpan.getParent_id() == null) {
					duration = zipkinSpan.getDuration();
					break;
				}
			}

			// Standard Approach
			setRankingParameter(entryZipkinSpan.getValue(), mapRankingParameter, (duration >= THRESHOLD_DURATION_EACH_TRACE));

			// Advanced Approach
			if (((double) duration / entryZipkinSpan.getValue().size()) >= THRESHOLD_DURATION_EACH_SPAN) {
				// System.out.println(entryZipkinSpan.getKey() +
				// " - FAILED! ::: EACH SPAN");
			} else {
				// System.out.println(entryZipkinSpan.getKey() +
				// " - PASSED! ::: EACH SPAN");
			}

		}

		callRankingMethods(mapRankingParameter.values());
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
		for (RankingParameter rankingParameter : listRankingParameter) {

			System.out.println(rankingParameter);

			double rankingIndex1 = callRankingMethod(new Tarantula(), rankingParameter);
			double rankingIndex2 = callRankingMethod(new Ochiai(), rankingParameter);
			double rankingIndex3 = callRankingMethod(new Ochiai2(), rankingParameter);

			System.out.println(String.format("Microservice-ID: %d, Tarantula: %s, Ochiai: %s, Ochiai2: %s \n", rankingParameter.getMicroserviceId(),
					rankingIndex1, rankingIndex2, rankingIndex3));
		}
	}

	private double callRankingMethod(ISBFLRankingMethod rankingTechnique, RankingParameter rankingParameter) {
		return rankingTechnique.calculateRankingIndex(rankingParameter);
	}
}
