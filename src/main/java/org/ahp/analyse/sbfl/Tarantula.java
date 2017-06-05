package org.ahp.analyse.sbfl;

import org.ahp.analyse.RankingParameter;

public class Tarantula implements ISBFLRankingMethod {

	public static final int ERROR_CODE_NO_FAILED_TRACES = -1;
	public static final int ERROR_CODE_NO_PASSED_TRACES = -2;

	@Override
	public double calculateRankingIndex(RankingParameter rankingParameter) {

		if (rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed() == 0) {
			return ERROR_CODE_NO_FAILED_TRACES;
		}

		if (rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed() == 0) {
			return ERROR_CODE_NO_PASSED_TRACES;
		}

		double procentFailed = (double) rankingParameter.getNumberExecuteFailed()
				/ (rankingParameter.getNumberExecuteFailed() + rankingParameter.getNumberNotExecuteFailed());
		double procentPassed = (double) rankingParameter.getNumberExecutePassed()
				/ (rankingParameter.getNumberExecutePassed() + rankingParameter.getNumberNotExecutePassed());

		return procentFailed / (procentFailed + procentPassed);
	}

}
