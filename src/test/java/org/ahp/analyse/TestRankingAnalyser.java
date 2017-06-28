package org.ahp.analyse;

import java.util.ArrayList;
import java.util.List;

import org.ahp.structure.ZipkinSpan;
import org.junit.Before;
import org.junit.Test;

public class TestRankingAnalyser {

	private RankingAnalyser analyser = null;
	private List<ZipkinSpan> listSpans = null;

	@Before
	public void before() {
		analyser = new RankingAnalyser();
		listSpans = new ArrayList<ZipkinSpan>();
	}

	@Test
	public void testCalculateRankingIndizesWithEmptyInput() {
		analyser.calculateRankingIndizes(listSpans);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateRankingIndizesWithoutTraceID() {
		listSpans.add(new ZipkinSpan());
		analyser.calculateRankingIndizes(listSpans);
	}

	@Test
	public void testCalculateRankingIndizesOnePassed() {

		ZipkinSpan span1 = new ZipkinSpan();
		span1.setDuration(100l);
		span1.setEndpoint_ipv4(1000l);
		span1.setTrace_id(20000l);

		listSpans.add(span1);
		analyser.calculateRankingIndizes(listSpans);
	}

	@Test
	public void testCalculateRankingIndizesOneFailed() {

		ZipkinSpan span1 = new ZipkinSpan();
		span1.setDuration(100000l);
		span1.setEndpoint_ipv4(1000l);
		span1.setTrace_id(20000l);

		listSpans.add(span1);
		analyser.calculateRankingIndizes(listSpans);
	}

	@Test
	public void testCalculateRankingIndizes() {

		listSpans.add(initZipkinSpan(20000l, 1000l, null, 100l + 100l + 10000l));
		listSpans.add(initZipkinSpan(20000l, 1001l, 1000l, 100l));
		listSpans.add(initZipkinSpan(20000l, 1002l, 1001l, 10000l));

		listSpans.add(initZipkinSpan(20001l, 1001l, null, 1400l + 100l));
		listSpans.add(initZipkinSpan(20001l, 1003l, 1001l, 100l));

		listSpans.add(initZipkinSpan(20002l, 1000l, null, 950l + 50l));
		listSpans.add(initZipkinSpan(20002l, 1003l, 1000l, 50l));

		listSpans.add(initZipkinSpan(20003l, 1000l, null, 100l + 10000l));
		listSpans.add(initZipkinSpan(20003l, 1002l, 1000l, 10000l));

		listSpans.add(initZipkinSpan(20004l, 1001l, null, 100l + 10000l + 100l));
		listSpans.add(initZipkinSpan(20004l, 1002l, 1001l, 10000l));
		listSpans.add(initZipkinSpan(20004l, 1003l, 1001l, 100l));

		listSpans.add(initZipkinSpan(20005l, 1001l, null, 500l + 5000l));
		listSpans.add(initZipkinSpan(20005l, 1002l, 1001l, 5000l));
		listSpans.add(initZipkinSpan(20005l, 1003l, 1001l, 100l));
		listSpans.add(initZipkinSpan(20005l, 1003l, 1001l, 100l));
		listSpans.add(initZipkinSpan(20005l, 1003l, 1001l, 100l));
		listSpans.add(initZipkinSpan(20005l, 1003l, 1001l, 100l));

		listSpans.add(initZipkinSpan(20006l, 1001l, null, 100l + 50l));
		listSpans.add(initZipkinSpan(20006l, 1002l, 1001l, 50l));

		analyser.calculateRankingIndizes(listSpans);
	}

	private ZipkinSpan initZipkinSpan(Long traceId, Long microserviceId, Long parentId, Long duration) {
		ZipkinSpan span = new ZipkinSpan();
		span.setDuration(duration);
		span.setEndpoint_ipv4(microserviceId);
		span.setTrace_id(traceId);
		span.setParent_id(parentId);
		return span;
	}

}
