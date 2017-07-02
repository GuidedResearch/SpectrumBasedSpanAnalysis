package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Zoltar implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed();

		double quotient = (10000 * rankingParameter.getNumberNotExecuteFailed() * rankingParameter.getNumberExecutePassed())
				/ rankingParameter.getNumberExecuteFailed();
		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed() + quotient;

		return numerator / denominator;
	}

}
