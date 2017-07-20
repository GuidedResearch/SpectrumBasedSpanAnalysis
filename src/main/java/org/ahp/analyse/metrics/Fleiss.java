package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class Fleiss implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double product1 = 4 * rankingParameter.getNumberExecuteFailed() * rankingParameter.getNumberNotExecutePassed();
		double product2 = 4 * rankingParameter.getNumberNotExecuteFailed() * rankingParameter.getNumberExecutePassed();
		double numerator = product1 - product2 - Math.pow(rankingParameter.getNumberNotExecuteFailed() - rankingParameter.getNumberExecutePassed(), 2);

		double sum1 = 2 * rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed();
		double sum2 = 2 * rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed();

		double denominator = sum1 + sum2;

		return numerator / denominator;
	}

}
