package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class GeometricMean implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double product1 = rankingParameter.getNumberExecuteFailed() * rankingParameter.getNumberNotExecutePassed();
		double product2 = rankingParameter.getNumberNotExecuteFailed() * rankingParameter.getNumberExecutePassed();
		double numerator = product1 - product2;

		double sumExectued = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberExecutePassed();
		double sumNotExectued = rankingParameter.getNumberNotExecutePassed() + rankingParameter.getNumberNotExecuteFailed();
		double sumFailed = rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed();
		double sumPassed = rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed();

		double denominator = Math.sqrt(sumExectued * sumNotExectued * sumFailed * sumPassed);

		return numerator / denominator;
	}

}
