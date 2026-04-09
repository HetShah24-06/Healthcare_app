package project1;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing SQLite database connection...");

        try {
            // Initialize database
            DBConnector.init();
            System.out.println("✓ Database initialized successfully");

            // Test patient operations
            PatientRecord testPatient = new PatientRecord(0, "John Doe", 30, "No history", "john@example.com");

            // Create patient
            int result = PatientDataManager.createPatient(testPatient);
            System.out.println("✓ Created patient, result: " + result);

            // Get all patients
            var patients = PatientDataManager.getAllPatients();
            System.out.println("✓ Retrieved " + patients.size() + " patients");

            // Test appointment operations
            if (!patients.isEmpty()) {
                Appointment testAppointment = new Appointment("John Doe", "2024-12-25", "Checkup",
                        patients.get(0).getID(), 0);

                // Create appointment
                result = AppointmentDataManager.createAppointment(testAppointment);
                System.out.println("✓ Created appointment, result: " + result);

                // Get all appointments
                var appointments = AppointmentDataManager.getAllAppointments();
                System.out.println("✓ Retrieved " + appointments.size() + " appointments");
            }

            System.out.println("All database tests passed! ✓");

        } catch (Exception e) {
            System.out.println("✗ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}