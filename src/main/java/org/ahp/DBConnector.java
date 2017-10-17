package org.ahp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnector {

	private Connection conn = null;

	public void connect() {

		try {
			System.out.println("* Treiber laden");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.err.println("Unable to load driver.");
			e.printStackTrace();
		}
		try {
			System.out.println("* Verbindung aufbauen");
			String url = "jdbc:mysql://" + Configuration.HOSTNAME + ":" + Configuration.PORT + "/" + Configuration.DB_NAME;
			conn = DriverManager.getConnection(url, Configuration.USER, Configuration.PASSWORD);
			System.out.println("* Verbindung hergestellt");

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState: " + sqle.getSQLState());
			System.out.println("VendorError: " + sqle.getErrorCode());
			sqle.printStackTrace();
		}

	}

	public void disconnect() {
		System.out.println("* Datenbank-Verbindung beenden");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			System.out.println("Execute Query: " + query);
			return conn.createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
