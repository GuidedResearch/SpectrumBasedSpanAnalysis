package org.ahp.analyse.condition;

import java.util.List;

import org.ahp.structure.ZipkinSpan;

public class RankingModeFussy extends AbstractRankingMode {

	@Override
	public double checkCondition(List<ZipkinSpan> spans) {

		long duration = getDurationOfTrace(spans);
		double avgSpanDuration = (double) duration / spans.size();

		if (avgSpanDuration >= THRESHOLD_DURATION_EACH_SPAN_HIGH) {
			System.out.println(String.format("<Fussy> TraceID: %s | Fussy: true | Duration: %.2f", spans.get(0).getTrace_id(), avgSpanDuration));
			return 1;
		} else if (avgSpanDuration <= THRESHOLD_DURATION_EACH_SPAN_LOW) {
			System.out.println(String.format("<Fussy> TraceID: %s | Fussy: false | Duration: %.2f", spans.get(0).getTrace_id(), avgSpanDuration));
			return 0;
		}

		double fussyValue = (avgSpanDuration - THRESHOLD_DURATION_EACH_SPAN_LOW) / (THRESHOLD_DURATION_EACH_SPAN_HIGH - THRESHOLD_DURATION_EACH_SPAN_LOW);

		System.out.println(String.format("<Fussy> TraceID: %s | Fussy: %.5f | Duration: %.2f", spans.get(0).getTrace_id(), fussyValue, avgSpanDuration));

		return fussyValue;
	}

}
