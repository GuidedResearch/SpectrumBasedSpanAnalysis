package org.ahp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.List;

import org.ahp.analyse.RankingAnalyser;
import org.ahp.structure.ZipkinSpan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RESTCaller {

	private static final String QUERY = "'select span.trace_id, span_id, parent_id, start_ts, duration, endpoint_ipv4, endpoint_service_name, name  from zipkin_annotations AS an, zipkin_spans AS span WHERE an.span_id = span.id AND span.trace_id = d7f6f2f661b23980'";
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.209.235", 8888));
		URLConnection conn = new URL("http://10.0.13.146:31856/executeQuery?query=" + QUERY).openConnection(proxy);
		// http://10.0.13.146:30202/executeQuery?query=SELECT
		conn.connect();
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
			StringBuffer buffer = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			ObjectMapper mapper = new ObjectMapper();
			List<ZipkinSpan> spans = mapper.readValue(buffer.toString(),new TypeReference<List<ZipkinSpan>>(){});
			
			for (ZipkinSpan zipkinSpan : spans) {
				System.out.println("::> " + zipkinSpan.getTrace_id());
			}
			
			RankingAnalyser analyser = new RankingAnalyser();
			analyser.calculateRankingIndizes(spans);
		}
	}

}
