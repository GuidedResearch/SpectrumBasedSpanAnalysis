package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class RussellAndRao implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed();

		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		return numerator / denominator;
	}

}
