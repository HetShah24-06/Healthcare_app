# Healthcare Appointment Management System

A JavaFX desktop application for managing patient records and appointments. Built with Java 21, JavaFX 17, and SQLite for local data storage.

---

## Features

- **Authentication** — Login screen with credential-based access control
- **Dashboard** — Unified view for patients and appointments
- **Patient management** — Create, view, edit, and delete patient records
- **Appointment management** — Create, view, edit, and delete appointments
- **Data export** — Save records to local files
- **Concurrent DB operations** — Background tasks via `ExecutorService` + `Platform.runLater`
- **Embedded SQLite** — Zero-config local database, auto-initialized on first run

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 17 |
| Database | SQLite (embedded via JDBC) |
| Concurrency | `ExecutorService` + `Platform.runLater` |
| Build | Maven |

---

## Getting Started

### Prerequisites

- Java 21 or newer
- Windows (cmd or PowerShell)
- Internet access on first run (to download JavaFX SDK and SQLite JDBC driver)

### Quick Start

Clone the repository and run the provided batch script from the project root:

```cmd
run_app.bat
```

This script will:
1. Download the JavaFX SDK if not present
2. Download the SQLite JDBC driver if not present
3. Compile the project
4. Launch the application

### Verify Setup

To confirm the environment is configured correctly:

```cmd
test_app.bat
```

This checks dependencies, compiles the project, and performs a startup verification.

### Manual Compilation

If you prefer to compile and run manually:

```cmd
javac --module-path "lib\javafx-sdk-17.0.6_windows-x64_bin-sdk\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  -cp "lib\sqlite-jdbc-3.42.0.0.jar" ^
  -d bin src\main\java\project1\*.java

java --module-path "lib\javafx-sdk-17.0.6_windows-x64_bin-sdk\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  -cp "bin;lib\sqlite-jdbc-3.42.0.0.jar" project1.MainApp
```

### Push to GitHub

After creating a repository on GitHub:

```cmd
git remote add origin https://github.com/<your-username>/<repo-name>.git
git push -u origin main
```

---

## Project Structure

```
src/main/java/project1/
├── MainApp.java                # Entry point and scene navigation manager
├── LoginScreen.java            # Login UI and credential validation
├── Dashboard.java              # Main dashboard view
├── PatientForm.java            # Patient CRUD UI
├── AppointmentForm.java        # Appointment CRUD UI
├── PatientRecord.java          # Patient data model
├── Appointment.java            # Appointment data model
├── PatientDataManager.java     # Patient DB operations
├── AppointmentDataManager.java # Appointment DB operations
├── DBConnector.java            # SQLite connection helper
└── DBUtils.java                # SQL utility methods
```

---

## Database Schema

Tables are created automatically on first run:

```sql
CREATE TABLE IF NOT EXISTS patients (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    name             TEXT    NOT NULL,
    age              INTEGER,
    medical_history  TEXT,
    contact_info     TEXT
);

CREATE TABLE IF NOT EXISTS appointments (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    patient_id       INTEGER,
    appointment_date TEXT,
    reason           TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);
```

---

## Architecture

### Scene Navigation

`MainApp` centralizes screen management. Each view is rendered in a `Stage` and navigates via `showX()` methods on the parent application object.

### Background Database Operations

All database work runs on a fixed-size `ExecutorService` thread pool. UI updates after async DB calls are dispatched using `Platform.runLater()` to comply with JavaFX's single-threaded rendering model.

### SQL Injection Prevention

All data operations use `PreparedStatement` with parameterized queries — no raw string concatenation.

---

## Screenshots

**Login Screen**

<img width="427" alt="Login Screen" src="https://github.com/user-attachments/assets/383ffb80-5001-412a-af18-8941acfe8808" />

**Dashboard**

<img width="1130" alt="Dashboard Screen" src="https://github.com/user-attachments/assets/0219621b-123a-4a44-be22-c84f7f016e60" />

**Manage Appointments**

<img width="632" alt="Appointment Management Screen" src="https://github.com/user-attachments/assets/a2f3b5c8-aa6f-4a5e-ac42-054a6f031769" />


---

## License

Developed for academic purposes at Humber Polytechnic. Not intended for production use.
