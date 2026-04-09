package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	private static final String DB_URL = "jdbc:sqlite:healthcare.db";

	// initializes a connection to a database, creating tables if they don't exist
	public static void init() {
		try (Connection conn = createConnection();
				Statement stmt = conn.createStatement()) {

			// Create patients table
			String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"name TEXT NOT NULL," +
					"age INTEGER," +
					"medical_history TEXT," +
					"contact_info TEXT" +
					")";

			// Create appointments table
			String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"patient_id INTEGER," +
					"appointment_date TEXT," +
					"reason TEXT," +
					"FOREIGN KEY (patient_id) REFERENCES patients(id)" +
					")";

			stmt.execute(createPatientsTable);
			stmt.execute(createAppointmentsTable);

			System.out.println("Database initialized successfully");

		} catch (Exception e) {
			System.out.println("Error initializing database: " + e.getMessage());
		}
	}

	// creates a new connection to the SQLite database
	public static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}
}