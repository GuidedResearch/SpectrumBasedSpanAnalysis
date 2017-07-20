package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class ArithmeticMean implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double product1 = 2 * rankingParameter.getNumberExecuteFailed() * rankingParameter.getNumberNotExecutePassed();
		double product2 = 2 * rankingParameter.getNumberNotExecuteFailed() * rankingParameter.getNumberExecutePassed();
		double numerator = (product1 - product2);

		double sumExectued = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();
		double sumNotExectued = rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed();
		double sumFailed = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();
		double sumPassed = rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		double denominator = (sumExectued * sumNotExectued) + (sumFailed * sumPassed);

		return numerator / denominator;
	}

}
