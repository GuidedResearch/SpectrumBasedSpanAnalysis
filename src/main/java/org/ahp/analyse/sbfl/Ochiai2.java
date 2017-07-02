package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Ochiai2 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double numerator = rankingParameter.getNumberExecuteFailed() * rankingParameter.getNumberNotExecutePassed();

		double sumExectued = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();
		double sumNotExectued = rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed();
		double sumFailed = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();
		double sumPassed = rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		double denominator = Math.sqrt(sumExectued * sumNotExectued * sumFailed * sumPassed);

		return numerator / denominator;
	}

}
