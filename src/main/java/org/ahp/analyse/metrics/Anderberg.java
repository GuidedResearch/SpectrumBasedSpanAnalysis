package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class Anderberg implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed();

		double value = 2 * (rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed());
		double denominator = rankingParameter.getNumberExecuteFailed() + value;

		return numerator / denominator;
	}

}
