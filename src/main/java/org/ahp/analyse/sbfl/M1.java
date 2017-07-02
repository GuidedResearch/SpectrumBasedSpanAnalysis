package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class M1 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed();

		double denominator = rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed();

		return numerator / denominator;
	}

}
