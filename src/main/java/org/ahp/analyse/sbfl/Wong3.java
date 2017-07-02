package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Wong3 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double value = 0;

		double executePassed = rankingParameter.getNumberExecutePassed();

		if (executePassed <= 2) {
			value = executePassed;
		} else if (executePassed <= 10) {
			value = 2 + 0.1 * (executePassed - 2);
		} else {
			value = 2.8 + 0.001 * (executePassed - 10);
		}

		return rankingParameter.getNumberExecuteFailed() - value;
	}

}
