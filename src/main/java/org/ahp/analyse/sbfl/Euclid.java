package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Euclid implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		return Math.sqrt(rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecutePassed());
	}

}
