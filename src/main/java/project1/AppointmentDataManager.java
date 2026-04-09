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

public class AppointmentDataManager {

    final static String table = "appointments";
    final static String patientsTable = "patients";
    private static final String TEXT_FILE = "appointment_records.txt";

    public synchronized static ArrayList<Appointment> getAllAppointments() throws SQLException {
        ArrayList<Appointment> records = new ArrayList<>();
        String query = "SELECT appointments.id AS appointment_id, appointments.patient_id, "
                + "appointments.appointment_date, appointments.reason, patients.name, patients.id AS patient_id "
                + "FROM " + table + " JOIN " + patientsTable + " ON appointments.patient_id = patients.id";

        try (
                Connection conn = DBConnector.createConnection();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Appointment rec = new Appointment(
                        resultSet.getString("name"),
                        resultSet.getString("appointment_date"),
                        resultSet.getString("reason"),
                        resultSet.getInt("patient_id"),
                        resultSet.getInt("appointment_id"));
                records.add(rec);
            }
        }
        return records;
    }

    public static class GetAllAppointmentsTask implements Runnable {
        private ArrayList<Appointment> records;
        private Runnable onComplete;
        private Runnable onError;

        public GetAllAppointmentsTask(ArrayList<Appointment> records, Runnable onComplete, Runnable onError) {
            this.records = records;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                ArrayList<Appointment> newRecords = AppointmentDataManager.getAllAppointments();
                records.clear();
                records.addAll(newRecords);
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int createAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO " + table + " (patient_id, appointment_date, reason) VALUES (?, ?, ?)";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setString(2, appointment.getAppointmentDateTime());
            stmt.setString(3, appointment.getVisitReason());
            return stmt.executeUpdate();
        }
    }

    public static class CreateAppointmentTask implements Runnable {
        private Appointment appointment;
        private Runnable onComplete;
        private Runnable onError;

        public CreateAppointmentTask(Appointment appointment, Runnable onComplete, Runnable onError) {
            this.appointment = appointment;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = AppointmentDataManager.createAppointment(appointment);
                if (result != 1)
                    throw new Exception("Failed to create appointment");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int updateAppointment(Appointment appointment) throws SQLException {
        String query = "UPDATE " + table + " SET appointment_date = ?, reason = ? WHERE id = ?";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, appointment.getAppointmentDateTime());
            stmt.setString(2, appointment.getVisitReason());
            stmt.setInt(3, appointment.getId());
            return stmt.executeUpdate();
        }
    }

    public static class UpdateAppointmentTask implements Runnable {
        private Appointment appointment;
        private Runnable onComplete;
        private Runnable onError;

        public UpdateAppointmentTask(Appointment appointment, Runnable onComplete, Runnable onError) {
            this.appointment = appointment;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = AppointmentDataManager.updateAppointment(appointment);
                if (result != 1)
                    throw new Exception("Failed to update appointment");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static int deleteAppointment(int ID) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE id = ?";

        try (
                Connection conn = DBConnector.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ID);
            return stmt.executeUpdate();
        }
    }

    public static class DeleteAppointmentTask implements Runnable {
        private int ID;
        private Runnable onComplete;
        private Runnable onError;

        public DeleteAppointmentTask(int ID, Runnable onComplete, Runnable onError) {
            this.ID = ID;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                int result = AppointmentDataManager.deleteAppointment(ID);
                if (result != 1)
                    throw new Exception("Failed to delete appointment");
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }

    public synchronized static void exportAppointments(ArrayList<Appointment> appointments) throws IOException {
        try (PrintWriter output = new PrintWriter(new FileWriter(new File(TEXT_FILE)))) {
            output.println("Appointment Data: " + appointments.size() + " entries");
            output.println("---");
            for (Appointment appt : appointments) {
                output.println("Patient Name: " + appt.patientName);
                output.println("Appointment Date: " + appt.AppointmentDateTime);
                output.println("Visit Reason: " + appt.visitReason);
                output.println("---");
            }
        }
    }

    public static class ExportRecordsTask implements Runnable {
        private ArrayList<Appointment> records;
        private Runnable onComplete;
        private Runnable onError;

        public ExportRecordsTask(ArrayList<Appointment> records, Runnable onComplete, Runnable onError) {
            this.records = records;
            this.onComplete = onComplete;
            this.onError = onError;
        }

        public void run() {
            try {
                AppointmentDataManager.exportAppointments(records);
                Platform.runLater(() -> onComplete.run());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> onError.run());
            }
        }
    }
}
