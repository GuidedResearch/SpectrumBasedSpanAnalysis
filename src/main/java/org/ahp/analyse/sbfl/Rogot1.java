package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Rogot1 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double numerator1 = rankingParameter.getNumberExecuteFailed();
		double denominator1 = 2 * rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed();

		double quotient1 = numerator1 / denominator1;

		double numerator2 = rankingParameter.getNumberNotExecutePassed();
		double denominator2 = 2 * rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed();

		double quotient2 = numerator2 / denominator2;

		return (quotient1 + quotient2) / 2;
	}

}
