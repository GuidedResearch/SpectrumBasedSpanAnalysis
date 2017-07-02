package org.ahp.analyse;

import java.util.HashMap;

public class RankingResult {

	private long microserviceId;

	private HashMap<String, Double> rankingResult;

	private RankingParameter parameter;

	public RankingResult(long microserviceId, RankingParameter parameter) {
		this.microserviceId = microserviceId;
		this.parameter = parameter;
		this.rankingResult = new HashMap<String, Double>();
	}

	public void addRankingResult(String method, double result) {
		this.rankingResult.put(method, result);
	}

	public long getMicroserviceId() {
		return microserviceId;
	}

	public HashMap<String, Double> getRankingResult() {
		return rankingResult;
	}

	public RankingParameter getRankingParameter() {
		return parameter;
	}
}
