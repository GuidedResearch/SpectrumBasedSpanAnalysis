package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Wong1 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {
		return rankingParameter.getNumberExecuteFailed();
	}

}
