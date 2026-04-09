package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SimpleDatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing SQLite database connection...");

        try {
            // Test basic SQLite connection
            Connection conn = DriverManager.getConnection("jdbc:sqlite:healthcare.db");
            System.out.println("✓ SQLite connection established");

            // Create tables
            Statement stmt = conn.createStatement();
            String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "age INTEGER," +
                    "medical_history TEXT," +
                    "contact_info TEXT" +
                    ")";

            String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patient_id INTEGER," +
                    "appointment_date TEXT," +
                    "reason TEXT," +
                    "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                    ")";

            stmt.execute(createPatientsTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("✓ Tables created successfully");

            // Test inserting a patient
            String insertPatient = "INSERT INTO patients (name, age, medical_history, contact_info) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertPatient);
            pstmt.setString(1, "John Doe");
            pstmt.setInt(2, 30);
            pstmt.setString(3, "No history");
            pstmt.setString(4, "john@example.com");
            int result = pstmt.executeUpdate();
            System.out.println("✓ Patient inserted, result: " + result);

            // Test selecting patients
            String selectPatients = "SELECT id, name, age FROM patients";
            ResultSet rs = stmt.executeQuery(selectPatients);
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("✓ Found patient: " + rs.getString("name") + ", age: " + rs.getInt("age"));
            }
            System.out.println("✓ Total patients found: " + count);

            // Test inserting an appointment
            String insertAppointment = "INSERT INTO appointments (patient_id, appointment_date, reason) VALUES (?, ?, ?)";
            PreparedStatement pstmt2 = conn.prepareStatement(insertAppointment);
            pstmt2.setInt(1, 1); // patient_id from the inserted patient
            pstmt2.setString(2, "2024-12-25");
            pstmt2.setString(3, "Checkup");
            result = pstmt2.executeUpdate();
            System.out.println("✓ Appointment inserted, result: " + result);

            // Test joining tables
            String joinQuery = "SELECT p.name, a.appointment_date, a.reason FROM appointments a JOIN patients p ON a.patient_id = p.id";
            ResultSet rs2 = stmt.executeQuery(joinQuery);
            count = 0;
            while (rs2.next()) {
                count++;
                System.out.println(
                        "✓ Found appointment: " + rs2.getString("name") + " - " + rs2.getString("appointment_date"));
            }
            System.out.println("✓ Total appointments found: " + count);

            // Clean up
            rs.close();
            rs2.close();
            pstmt.close();
            pstmt2.close();
            stmt.close();
            conn.close();

            System.out.println("All database tests passed! ✓");

        } catch (Exception e) {
            System.out.println("✗ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}