package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Jaccard implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed();

		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed();

		return numerator / denominator;
	}

}
