package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class SorensenDice implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		double numerator = 2 * rankingParameter.getNumberExecuteFailed();

		double denominator = numerator + rankingParameter.getNumberNotExecuteFailed() + rankingParameter.getNumberExecutePassed();

		return numerator / denominator;
	}

}
