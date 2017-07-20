package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class HarmonicMean implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double product1 = rankingParameter.getNumberExecuteFailed() * rankingParameter.getNumberNotExecutePassed();
		double product2 = rankingParameter.getNumberNotExecuteFailed() * rankingParameter.getNumberExecutePassed();

		double sumExectued = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();
		double sumNotExectued = rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed();
		double sumFailed = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();
		double sumPassed = rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		double mainProduct = sumExectued * sumNotExectued * sumFailed * sumPassed;

		double numerator = (product1 - product2) * mainProduct;
		double denominator = mainProduct;

		return numerator / denominator;
	}

}
