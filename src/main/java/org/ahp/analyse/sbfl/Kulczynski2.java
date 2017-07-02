package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Kulczynski2 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator1 = rankingParameter.getNumberExecuteFailed();
		double denominator1 = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();

		double quotient1 = numerator1 / denominator1;

		double numerator2 = rankingParameter.getNumberExecuteFailed();
		double denominator2 = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();

		double quotient2 = numerator2 / denominator2;

		return (quotient1 + quotient2) / 2;
	}

}
