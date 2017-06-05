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

		ZipkinSpan span1 = new ZipkinSpan();
		listSpans.add(span1);
		span1.setDuration(100l);
		span1.setEndpoint_ipv4(1000l);
		span1.setTrace_id(20000l);

		ZipkinSpan span2 = new ZipkinSpan();
		listSpans.add(span2);
		span2.setDuration(100l);
		span2.setEndpoint_ipv4(1001l);
		span2.setTrace_id(20001l);

		ZipkinSpan span3 = new ZipkinSpan();
		listSpans.add(span3);
		span3.setDuration(100l);
		span3.setEndpoint_ipv4(1000l);
		span3.setTrace_id(20002l);

		ZipkinSpan span4 = new ZipkinSpan();
		listSpans.add(span4);
		span4.setDuration(10000l);
		span4.setEndpoint_ipv4(1002l);
		span4.setTrace_id(20003l);

		ZipkinSpan span5 = new ZipkinSpan();
		listSpans.add(span5);
		span5.setDuration(100l);
		span5.setEndpoint_ipv4(1000l);
		span5.setTrace_id(20003l);

		ZipkinSpan span6 = new ZipkinSpan();
		listSpans.add(span6);
		span6.setDuration(100l);
		span6.setEndpoint_ipv4(1002l);
		span6.setTrace_id(20004l);

		analyser.calculateRankingIndizes(listSpans);
	}
}
