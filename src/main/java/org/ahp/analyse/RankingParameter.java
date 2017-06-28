package org.ahp.analyse;

public class RankingParameter {

	private double numberExecuteFailed = 0;
	private double numberExecutePassed = 0;
	private double numberNotExecuteFailed = 0;
	private double numberNotExecutePassed = 0;

	private long microserviceId;

	public RankingParameter(long microserviceId) {
		this.microserviceId = microserviceId;
	}

	public void increaseExecute(double passed) {
		numberExecutePassed += passed;
		numberExecuteFailed += (1 - passed);
	}

	public void increaseNotExecute(double passed) {
		numberNotExecutePassed += passed;
		numberNotExecuteFailed += (1 - passed);
	}

	public double getNumberExecuteFailed() {
		return numberExecuteFailed;
	}

	public double getNumberExecutePassed() {
		return numberExecutePassed;
	}

	public double getNumberNotExecuteFailed() {
		return numberNotExecuteFailed;
	}

	public double getNumberNotExecutePassed() {
		return numberNotExecutePassed;
	}

	public long getMicroserviceId() {
		return microserviceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("RankingParameter [numberExecuteFailed=%.3f, numberExecutePassed=%.3f, numberNotExecuteFailed=%.3f, numberNotExecutePassed=%.3f, microserviceId=%d]",
						numberExecuteFailed, numberExecutePassed, numberNotExecuteFailed, numberNotExecutePassed, microserviceId);
	}

}
