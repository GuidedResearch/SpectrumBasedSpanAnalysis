package org.ahp.analyse.metrics;

import org.ahp.analyse.RankingParameter;

public class Rogot2 implements ISBFLRankingMethod {

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		double executeFailed = rankingParameter.getNumberExecuteFailed();
		double notExecutePassed = rankingParameter.getNumberNotExecutePassed();

		double quotient1 = executeFailed / (executeFailed + rankingParameter.getNumberExecutePassed());
		double quotient2 = executeFailed / (executeFailed + rankingParameter.getNumberNotExecuteFailed());
		double quotient3 = notExecutePassed / (notExecutePassed + rankingParameter.getNumberExecutePassed());
		double quotient4 = notExecutePassed / (notExecutePassed + rankingParameter.getNumberNotExecuteFailed());

		return (quotient1 + quotient2 + quotient3 + quotient4) / 4;
	}

}
