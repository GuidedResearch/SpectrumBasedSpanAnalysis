package org.ahp.analyse.condition;

import java.util.List;

import org.ahp.structure.ZipkinSpan;

public abstract class AbstractRankingMode {

	protected static final int THRESHOLD_DURATION_EACH_SPAN_HIGH = 1000;
	protected static final int THRESHOLD_DURATION_EACH_SPAN_LOW = (THRESHOLD_DURATION_EACH_SPAN_HIGH / 4);

	public abstract double checkCondition(List<ZipkinSpan> spans);

	protected long getDurationOfTrace(List<ZipkinSpan> spans) {
		for (ZipkinSpan zipkinSpan : spans) {
			// Find root span
			if (zipkinSpan.getParent_id() == null) {
				return zipkinSpan.getDuration();
			}
		}
		return 0;
	}

	public abstract String getRankingModeName();
}
