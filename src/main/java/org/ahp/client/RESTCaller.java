package org.ahp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class RESTCaller {

	public static void main(String[] args) throws MalformedURLException, IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.209.235", 8888));
		URLConnection conn = new URL("http://10.0.13.146:30202/getSpans/33284775251760003").openConnection(proxy);
		// http://10.0.13.146:30202/executeQuery?query=SELECT
		conn.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}
		in.close();
	}

}
