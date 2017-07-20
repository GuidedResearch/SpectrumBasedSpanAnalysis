package org.ahp.analyse;

import java.util.ArrayList;
import java.util.List;

import org.ahp.sbfl.AnalyseEvaluationStarter;
import org.ahp.structure.ZipkinSpan;
import org.junit.Before;
import org.junit.Test;

public class TestRankingAnalyser {

	private AnalyseEvaluationStarter analyser = null;
	private List<ZipkinSpan> listSpans = null;
	private List<String> listMicroservicesWithFaults = null;

	@Before
	public void before() {
		analyser = new AnalyseEvaluationStarter();
		listSpans = new ArrayList<ZipkinSpan>();
		listMicroservicesWithFaults = new ArrayList<String>();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateRankingIndizesWithNullSpans() {
		analyser.runAnalyseAndEvaluation(null, listMicroservicesWithFaults);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateRankingIndizesWithNullMicroservices() {
		listSpans.add(new ZipkinSpan());
		analyser.runAnalyseAndEvaluation(listSpans, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateRankingIndizesWithEmptyInput() {
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateRankingIndizesWithoutTraceID() {
		listSpans.add(new ZipkinSpan());
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
	}

	@Test
	public void testCalculateRankingIndizesOnePassed() {

		listSpans.add(initZipkinSpan(20000l, 1000l, null, 100l));

		listMicroservicesWithFaults.add("1000");
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
	}

	@Test
	public void testCalculateRankingIndizesOneFailed() {

		listSpans.add(initZipkinSpan(20000l, 1000l, null, 100000l));

		listMicroservicesWithFaults.add("1000");
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
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

		listMicroservicesWithFaults.add("1002");
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
	}

	@Test
	public void testCalculateRankingIndizesMixed() {

		listSpans.add(initZipkinSpan(20000l, 1000l, null, 100l + 100l + 10000l));
		listSpans.add(initZipkinSpan(20000l, 1001l, 1000l, 100l));
		listSpans.add(initZipkinSpan(20000l, 1002l, 1001l, 10000l));

		listSpans.add(initZipkinSpan(20001l, 1000l, null, 1400l + 100l));
		listSpans.add(initZipkinSpan(20001l, 1001l, 1000l, 100l));

		listSpans.add(initZipkinSpan(20002l, 1000l, null, 950l + 10000l));
		listSpans.add(initZipkinSpan(20002l, 1003l, 1000l, 10000l));

		listSpans.add(initZipkinSpan(20003l, 1000l, null, 100l + 10000l));
		listSpans.add(initZipkinSpan(20003l, 1002l, 1000l, 10000l));

		listSpans.add(initZipkinSpan(20004l, 1001l, null, 100l + 10000l + 10000l));
		listSpans.add(initZipkinSpan(20004l, 1002l, 1001l, 10000l));
		listSpans.add(initZipkinSpan(20004l, 1003l, 1001l, 10000l));

		listMicroservicesWithFaults.add("1002");
		listMicroservicesWithFaults.add("1003");
		analyser.runAnalyseAndEvaluation(listSpans, listMicroservicesWithFaults);
	}

	private ZipkinSpan initZipkinSpan(Long traceId, Long microserviceId, Long parentId, Long duration) {
		ZipkinSpan span = new ZipkinSpan();
		span.setDuration(duration);
		span.setEndpoint_ipv4(microserviceId);
		span.setTrace_id(traceId);
		span.setParent_id(parentId);
		span.setEndpoint_service_name(microserviceId.toString());
		return span;
	}

}
