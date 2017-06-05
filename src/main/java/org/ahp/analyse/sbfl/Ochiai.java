package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Ochiai implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double numerator = rankingParameter.getNumberExecuteFailed();

		double sumFailed = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();
		double sumExectued = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();

		double denominator = Math.sqrt(sumFailed * sumExectued);

		return numerator / denominator;
	}

}
