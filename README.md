# Healthcare Appointment Management System

A JavaFX desktop application for managing patient records and appointments. This repository contains a course project built with Java 21, JavaFX 17, and SQLite for local storage.

---

## Features

- **Login screen** with basic credential flow
- **Dashboard** for patients and appointments
- **Patient management**: create, view, edit, delete
- **Appointment management**: create, view, edit, delete
- **Export support** for saving records to files
- **Background database operations** using `ExecutorService`
- **Embedded SQLite** for quick, local setup

---

## Tech Stack

| Layer        | Technology                              |
| ------------ | --------------------------------------- |
| Language     | Java 21                                 |
| UI Framework | JavaFX 17                               |
| Database     | SQLite (embedded)                       |
| Concurrency  | `ExecutorService` + `Platform.runLater` |
| Build        | Maven                                   |

---

## Getting Started

### Prerequisites

- Java 21 or newer
- Windows terminal: `cmd.exe` or PowerShell
- Internet access on first run to download dependencies

### Run the app

Open a terminal in the project root and execute:

```cmd
cd C:\Users\HET\OneDrive\Desktop\JavaProject\healthcare-app
run_app.bat
```

This script will:
- download the JavaFX SDK if needed
- download the SQLite JDBC driver if needed
- compile the project
- launch the application

### Verify the installation

To confirm the environment and application startup:

```cmd
test_app.bat
```

This script checks dependencies, compiles the app, and performs a simple startup verification.

### Run manually

To compile and start the app manually:

```cmd
javac --module-path "lib\\javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/*.java
java --module-path "lib\\javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.MainApp
```

### Upload to GitHub

After creating a repository on GitHub, push this local project:

```cmd
cd C:\Users\HET\OneDrive\Desktop\JavaProject\healthcare-app
git remote add origin https://github.com/<your-username>/<repo-name>.git
git push -u origin main
```

---

## Project Structure

```
src/main/java/project1/
├── MainApp.java                # Application entry point and navigation manager
├── LoginScreen.java            # Login UI and credential check
├── Dashboard.java              # Main dashboard view
├── PatientForm.java            # Patient CRUD UI
├── AppointmentForm.java        # Appointment CRUD UI
├── PatientRecord.java          # Patient data model
├── Appointment.java            # Appointment data model
├── PatientDataManager.java     # Patient DB operations
├── AppointmentDataManager.java # Appointment DB operations
├── DBConnector.java            # SQLite connection helper
└── DBUtils.java                # SQL utility helpers
```

---

## Database Schema

The application uses SQLite and creates tables automatically on first run:

```sql
CREATE TABLE IF NOT EXISTS patients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    age INTEGER,
    medical_history TEXT,
    contact_info TEXT
);

CREATE TABLE IF NOT EXISTS appointments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    patient_id INTEGER,
    appointment_date TEXT,
    reason TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);
```

---

## Architecture Notes

### Scene navigation

`MainApp` centralizes navigation and manages screen switching. Each view is displayed in a new `Stage` and uses the parent app object to call `showX()` methods.

### Background database operations

Database tasks use a fixed `ExecutorService` thread pool. UI updates after database work are dispatched with `Platform.runLater()`.

### SQL safety

Data operations use `PreparedStatement` with parameterized queries.

---

## Screenshots

> _(Add screenshots of Login, Dashboard, Patient Form, and Appointment Form here)_

---

## License

Developed for academic purposes at Humber Polytechnic.
Not intended for production use.
