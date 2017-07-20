package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class Ample implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator1 = rankingParameter.getNumberExecuteFailed();
		double denominator1 = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();

		double quotient1 = numerator1 / denominator1;

		double numerator2 = rankingParameter.getNumberExecutePassed();
		double denominator2 = rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		double quotient2 = numerator2 / denominator2;

		return Math.abs(quotient1 - quotient2);
	}

}
