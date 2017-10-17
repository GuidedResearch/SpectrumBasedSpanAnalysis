package org.ahp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ahp.Configuration;
import org.ahp.DBConnector;
import org.ahp.sbfl.AnalyseEvaluationStarter;
import org.ahp.structure.ZipkinSpan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RESTCaller {

	private static final String QUERY = "SELECT span.trace_id, span.id, span.parent_id, span.start_ts, span.duration, endpoint_ipv4, endpoint_service_name, span.name "
			+ "FROM (SELECT span_id, endpoint_ipv4, endpoint_service_name FROM zipkin_annotations GROUP BY span_id) AS an, (SELECT * FROM zipkin_spans WHERE name NOT LIKE '%health%' "
			+ "AND start_ts >= UNIX_TIMESTAMP('2017-08-31 17:40:00') * 1000000 AND start_ts <= UNIX_TIMESTAMP('2017-08-31 17:50:00') * 1000000) AS span "
			+ "WHERE an.span_id = span.id ORDER BY start_ts";

	// 1x1: start_ts >= UNIX_TIMESTAMP('2017-08-31 14:50:00') * 1000000 AND
	// start_ts <= UNIX_TIMESTAMP('2017-08-31 15:00:00') * 1000000
	// 3x3: start_ts >= UNIX_TIMESTAMP('2017-08-31 15:20:00') * 1000000 AND
	// start_ts <= UNIX_TIMESTAMP('2017-08-31 15:30:00') * 1000000
	// 6x6: start_ts >= UNIX_TIMESTAMP('2017-08-31 15:40:00') * 1000000 AND
	// start_ts <= UNIX_TIMESTAMP('2017-08-31 15:50:00') * 1000000

	public static void main(String[] args) throws MalformedURLException, IOException, SQLException {

		List<ZipkinSpan> spans = null;
		if (Configuration.USE_REST_INTERFACE) {
			spans = getSpansFromREST();
		} else {
			DBConnector connector = new DBConnector();
			connector.connect();
			spans = convertToZipkinSpans(connector.executeQuery(QUERY));
			connector.disconnect();
		}

		for (ZipkinSpan zipkinSpan : spans) {
			System.out.println("::> " + zipkinSpan.getTrace_id());
		}

		AnalyseEvaluationStarter analyser = new AnalyseEvaluationStarter();

		for (String element : Configuration.HIT_AT_X_ELEMENT) {
			analyser.runAnalyseAndEvaluation(spans, Arrays.asList(element));
		}
	}

	private static List<ZipkinSpan> getSpansFromREST() throws MalformedURLException, IOException {

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.209.235", 8888));
		URLConnection conn = new URL("http://10.0.13.146:31856/executeQuery?query=" + URLEncoder.encode(QUERY, "UTF-8")).openConnection(proxy);
		// http://10.0.13.146:30202/executeQuery?query=SELECT

		conn.connect();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuffer buffer = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(buffer.toString(), new TypeReference<List<ZipkinSpan>>() {
			});
		}
	}

	private static List<ZipkinSpan> convertToZipkinSpans(ResultSet result) throws SQLException {

		List<ZipkinSpan> listSpans = new ArrayList<ZipkinSpan>();
		while (result.next()) {

			ZipkinSpan span = new ZipkinSpan();
			span.setTrace_id(result.getLong(1));
			span.setSpan_id(result.getLong(2));
			span.setParent_id(result.getLong(3));
			span.setStart_ts(result.getLong(4));
			span.setDuration(result.getLong(5));
			span.setEndpoint_ipv4(result.getLong(6));
			span.setEndpoint_service_name(result.getString(7));
			span.setName(result.getString(8));

			listSpans.add(span);
		}

		return listSpans;
	}
}
