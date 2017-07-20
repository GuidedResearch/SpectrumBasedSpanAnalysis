package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class RogersTanimoto implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed();

		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed() + 2
				* (rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed());

		return numerator / denominator;
	}

}
