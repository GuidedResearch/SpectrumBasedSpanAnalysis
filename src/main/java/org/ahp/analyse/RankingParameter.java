package org.ahp.analyse;

public class RankingParameter {

	private long numberExecuteFailed = 0;
	private long numberExecutePassed = 0;
	private long numberNotExecuteFailed = 0;
	private long numberNotExecutePassed = 0;

	private long microserviceId;

	public RankingParameter(long microserviceId) {
		this.microserviceId = microserviceId;
	}

	public void increaceNumberExecuteFailed() {
		numberExecuteFailed++;
	}

	public void increaceNumberExecutePassed() {
		numberExecutePassed++;
	}

	public void increaceNumberNotExecuteFailed() {
		numberNotExecuteFailed++;
	}

	public void increaceNumberNotExecutePassed() {
		numberNotExecutePassed++;
	}

	public long getNumberExecuteFailed() {
		return numberExecuteFailed;
	}

	public long getNumberExecutePassed() {
		return numberExecutePassed;
	}

	public long getNumberNotExecuteFailed() {
		return numberNotExecuteFailed;
	}

	public long getNumberNotExecutePassed() {
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
		return "RankingParameter [numberExecuteFailed=" + numberExecuteFailed + ", numberExecutePassed=" + numberExecutePassed + ", numberNotExecuteFailed="
				+ numberNotExecuteFailed + ", numberNotExecutePassed=" + numberNotExecutePassed + ", microserviceId=" + microserviceId + "]";
	}

}
