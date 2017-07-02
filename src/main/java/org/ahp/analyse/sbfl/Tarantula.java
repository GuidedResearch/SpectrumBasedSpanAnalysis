package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Tarantula implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double procentFailed = (double) rankingParameter.getNumberExecuteFailed()
				/ (rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed());
		double procentPassed = (double) rankingParameter.getNumberExecutePassed()
				/ (rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed());

		return procentFailed / (procentFailed + procentPassed);
	}

}
