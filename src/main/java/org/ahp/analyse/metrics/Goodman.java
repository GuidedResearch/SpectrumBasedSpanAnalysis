package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class Goodman implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = 2 * rankingParameter.getNumberExecuteFailed() - rankingParameter.getNumberNotExecuteFailed()
				- rankingParameter.getNumberExecutePassed();

		double denominator = 2 * rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed();

		return numerator / denominator;
	}

}
