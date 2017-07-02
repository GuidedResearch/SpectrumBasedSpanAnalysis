package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Hamann implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed()
				- rankingParameter.getNumberNotExecuteFailed() - rankingParameter.getNumberExecutePassed();

		double denominator = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed()
				+ rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		return numerator / denominator;
	}

}
