package org.ahp.analyse.condition;

import java.util.List;

import org.ahp.Configuration;
import org.ahp.structure.ZipkinSpan;

public abstract class AbstractRankingMode {

	protected static final int THRESHOLD_DURATION_EACH_SPAN_HIGH = Configuration.THRESHOLD_DURATION_EACH_SPAN_HIGH;
	protected static final int THRESHOLD_DURATION_EACH_SPAN_LOW = Configuration.THRESHOLD_DURATION_EACH_SPAN_LOW;

	public abstract double checkCondition(List<ZipkinSpan> spans);

	protected long getDurationOfTrace(List<ZipkinSpan> spans) {
		for (ZipkinSpan zipkinSpan : spans) {
			// Find root span
			Long parentId = zipkinSpan.getParent_id();
			if (parentId == 0) {
				return zipkinSpan.getDuration();
			}
		}
		return 0;
	}

	public abstract String getRankingModeName();
}
