package org.ahp;

public class Configuration {

	// Source of spans
	public static final boolean USE_REST_INTERFACE = false;

	// Database connection
	public static final String HOSTNAME = "localhost";
	public static final String PORT = "3306";
	public static final String DB_NAME = "zipkin";
	public static final String USER = "root";
	public static final String PASSWORD = "";

	// public static final String HOSTNAME =
	// System.getProperty("zipkin.mysql.hostname");
	// public static final String PORT =
	// System.getProperty("zipkin.mysql.port");
	// public static final String DB_NAME =
	// System.getProperty("zipkin.mysql.dbname");
	// public static final String USER =
	// System.getProperty("zipkin.mysql.user");
	// public static final String PASSWORD =
	// System.getProperty("zipkin.mysql.password");

	// SBFL techniques limits - standard and fuzzy
	public static final int THRESHOLD_DURATION_EACH_SPAN_HIGH = 150000;
	public static final int THRESHOLD_DURATION_EACH_SPAN_LOW = (THRESHOLD_DURATION_EACH_SPAN_HIGH / 2);

	// Evaluation Hit@X
	public static final String[] HIT_AT_X_ELEMENT = { "shipping" };
	public static final int[] HIT_AT_X = { 3 };

}
