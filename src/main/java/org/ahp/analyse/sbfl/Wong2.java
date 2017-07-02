package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Wong2 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		return rankingParameter.getNumberExecuteFailed() - rankingParameter.getNumberExecutePassed();
	}

}
