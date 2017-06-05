package org.ahp;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ahp.structure.ZipkinSpan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@EnableAutoConfiguration
public class RESTfulService {
	private static final String DEFAULT_QUERY = "select span.trace_id, span_id, parent_id, start_ts, duration, endpoint_ipv4, endpoint_service_name, name  from zipkin_annotations AS an, zipkin_spans AS span WHERE an.span_id = span.id limit 20;";
	private static final String RETURN_VALUES = "span.trace_id, span_id, parent_id, start_ts, duration, endpoint_ipv4, endpoint_service_name, name";

	/**
	 * Returns all spans
	 * 
	 * @param x
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/getAllSpans")
	@ResponseBody
	String getSpans() throws SQLException {
		DBConnector connector = new DBConnector();
		connector.connect();
		ResultSet result = connector.executeQuery(String.format("select %s from zipkin_annotations AS an, zipkin_spans AS span WHERE an.span_id = span.id",
				RETURN_VALUES));

		String spans = parseSpans(result);

		connector.disconnect();
		return spans;
	}

	/**
	 * Returns spans with given traceId
	 * 
	 * @param x
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(method = GET, value = "/getSpans/{traceId}")
	@ResponseBody
	String getSpans(@PathVariable String traceId) throws SQLException {
		DBConnector connector = new DBConnector();
		connector.connect();
		ResultSet result = connector.executeQuery(String.format(
				"select %s  from zipkin_annotations AS an, zipkin_spans AS span WHERE an.span_id = span.id AND span.trace_id=%s", RETURN_VALUES, traceId));
		String spans = parseSpans(result);

		connector.disconnect();
		return spans;
	}
	
	/**
	 * Analyses spans with given traceIds
	 * 
	 * @param x
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(method = GET, value = "/executeQuery")
	@ResponseBody
	String executeQueryAndAnalyze(@RequestParam(value = "query", required = false, defaultValue = DEFAULT_QUERY) String query) throws SQLException {
		DBConnector connector = new DBConnector();
		connector.connect();
		ResultSet result = connector.executeQuery(query);
		String spans = parseSpans(result);
		connector.disconnect();
		return spans;
	}

	private String parseSpans(ResultSet result) throws SQLException {

		List<ZipkinSpan> listSpans = convertToZipkinSpans(result);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(listSpans);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	private List<ZipkinSpan> convertToZipkinSpans(ResultSet result) throws SQLException {

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

	public static void main(String[] args) {
		SpringApplication.run(RESTfulService.class, args);
	}
}
