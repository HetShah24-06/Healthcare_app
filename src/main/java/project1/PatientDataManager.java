package project1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.application.Platform;

public class PatientDataManager {

    final static String table = "patients";
    private static final String TEXT_FILE = "patient_records.txt";

    public synchronized static ArrayList<PatientRecord> getAllPatients() throws SQLException {
        ArrayList<PatientRecord> records = new ArrayList<>();
        String query = "SELECT id, name, age, medical_history, contact_info FROM " + table;

        try (
                Connection conn = DBConnector.createConnection();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PatientRecord rec = new PatientRecord(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("medical_history"),
                        resultSet.getString("contact_info"));
                records.add(rec);
            }
        }
        return records;
    }

    public static class GetAllPatientsTask implements Runnable {
        private ArrayList<PatientRecord> records;
        private Runnable onComplete;
        private Runnable onError;

        public GetAllPatientsTask(ArrayList<PatientRecord> records, Runnable onComplete, Runnable onError) {
            this.records = records;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                ArrayList<PatientRecord> newRecords = PatientDataManager.getAllPatients();
                records.clear();
                records.addAll(newRecords);
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int createPatient(PatientRecord patient) throws SQLException {
        String query = "INSERT INTO " + table + " (name, age, medical_history, contact_info) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getMedicalHistory());
            stmt.setString(4, patient.getContactInformation());
            return stmt.executeUpdate();
        }
    }

    public static class CreatePatientTask implements Runnable {
        private PatientRecord patient;
        private Runnable onComplete;
        private Runnable onError;

        public CreatePatientTask(PatientRecord patient, Runnable onComplete, Runnable onError) {
            this.patient = patient;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = PatientDataManager.createPatient(patient);
                if (result != 1)
                    throw new Exception("Failed to create patient");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int updatePatient(PatientRecord patient) throws SQLException {
        String query = "UPDATE " + table + " SET name = ?, age = ?, medical_history = ?, contact_info = ? WHERE id = ?";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getMedicalHistory());
            stmt.setString(4, patient.getContactInformation());
            stmt.setInt(5, patient.getID());
            return stmt.executeUpdate();
        }
    }

    public static class UpdatePatientTask implements Runnable {
        private PatientRecord patient;
        private Runnable onComplete;
        private Runnable onError;

        public UpdatePatientTask(PatientRecord patient, Runnable onComplete, Runnable onError) {
            this.patient = patient;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = PatientDataManager.updatePatient(patient);
                if (result != 1)
                    throw new Exception("Failed to update patient");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int deletePatient(int ID) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE id = ?";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ID);
            return stmt.executeUpdate();
        }
    }

    public static class DeletePatientTask implements Runnable {
        private int patientID;
        private Runnable onComplete;
        private Runnable onError;

        public DeletePatientTask(int patientID, Runnable onComplete, Runnable onError) {
            this.patientID = patientID;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = PatientDataManager.deletePatient(patientID);
                if (result != 1)
                    throw new Exception("Failed to delete patient");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static void exportRecords(ArrayList<PatientRecord> records) throws IOException {
        try (PrintWriter output = new PrintWriter(new FileWriter(new File(TEXT_FILE)))) {
            output.println("ID, Name, Age, Medical History, Contact Info");
            for (PatientRecord p : records) {
                output.println(p.getID() + ", " + p.getName() + ", " + p.getAge() + ", "
                        + p.getMedicalHistory() + ", " + p.getContactInformation());
            }
        }
    }

    public static class ExportRecordsTask implements Runnable {
        private ArrayList<PatientRecord> records;
        private Runnable onComplete;
        private Runnable onError;

        public ExportRecordsTask(ArrayList<PatientRecord> records, Runnable onComplete, Runnable onError) {
            this.records = records;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                PatientDataManager.exportRecords(records);
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }
}
