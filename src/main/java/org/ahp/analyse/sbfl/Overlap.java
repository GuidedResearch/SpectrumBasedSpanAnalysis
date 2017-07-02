package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Overlap implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double numerator = rankingParameter.getNumberExecuteFailed();

		double firstMin = Math.min(rankingParameter.getNumberExecuteFailed(), rankingParameter.getNumberNotExecuteFailed());
		double denominator = Math.min(rankingParameter.getNumberExecutePassed(), firstMin);

		return numerator / denominator;
	}
}
