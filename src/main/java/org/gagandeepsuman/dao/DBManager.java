package org.gagandeepsuman.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	private static final String URL = "jdbc:postgresql://localhost:5432/SistemaScuolaLingue";
	private static final String USER = "gagandeepsuman"; // Sostituisci con il tuo utente PostgreSQL
	private static final String PASSWORD = "Gaggu123"; // Sostituisci con la tua password

	private Connection connection;

	public Connection getConnection() {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.connection;
	}

	public boolean closeConnection() {
		// TODO - implement DBManager.closeConnection
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		// throw new UnsupportedOperationException();
	}

}