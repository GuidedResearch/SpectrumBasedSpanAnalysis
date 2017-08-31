package org.ahp.analyse.condition;

import java.util.List;

import org.ahp.structure.ZipkinSpan;

public class RankingModeStandard extends AbstractRankingMode {

	@Override
	public double checkCondition(List<ZipkinSpan> spans) {

		long duration = getDurationOfTrace(spans);
		double avgSpanDuration = (double) duration / spans.size();

		// (duration >= THRESHOLD_DURATION_EACH_TRACE)
		final boolean METRIC_QUERY = (avgSpanDuration >= THRESHOLD_DURATION_EACH_SPAN_HIGH);

//		System.out.println(String.format("<Trace-Status> TraceID: %s | Failed: %b", spans.get(0).getTrace_id(), METRIC_QUERY));

		return METRIC_QUERY ? 1 : 0;
	}

	@Override
	public String getRankingModeName() {
		return "Standard";
	}

}
