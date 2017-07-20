package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class M2 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed();

		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed() + 2
				* (rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed());

		return numerator / denominator;
	}

}
