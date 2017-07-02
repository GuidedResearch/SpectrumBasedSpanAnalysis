package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Sokal implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = 2 * (rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed());

		double denominator = 2 * (rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed())
				+ rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed();

		return numerator / denominator;
	}

}
