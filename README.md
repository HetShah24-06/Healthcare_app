# Healthcare Appointment Management System

A JavaFX desktop application for managing patient records and appointments, backed by an Oracle database. Built as a course project at Humber Polytechnic.

---

## Features

- **Login Screen** — Credential-based authentication (configurable via environment variables)
- **Dashboard** — Live side-by-side view of all patients and upcoming appointments
- **Patient Management** — Full CRUD (Create, Read, Update, Delete) for patient records, plus CSV export
- **Appointment Management** — Full CRUD for appointments linked to patients, plus text file export
- **Async Database Operations** — All DB calls run on a background thread pool (`ExecutorService`) to keep the UI responsive
- **Oracle DB Integration** — Connects via JDBC; all queries use `PreparedStatement` to prevent SQL injection

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

- Java 21+
- JavaFX SDK 17 (automatically downloaded by the run script)
- SQLite JDBC driver (automatically downloaded by the run script)
- Git installed when uploading to GitHub

### Run the app (recommended)

From the project root in a Windows terminal:

```cmd
cd C:\Users\HET\OneDrive\Desktop\JavaProject\healthcare-app
run_app.bat
```

This script will:
- download the JavaFX SDK if needed
- download the SQLite JDBC driver if needed
- compile the project
- start the JavaFX application

### Run tests / verify environment

From the same folder:

```cmd
test_app.bat
```

This script checks that the JavaFX and SQLite dependencies are present, compiles the app, and performs a basic startup health check.

### Run manually

If you prefer to compile and run the project yourself:

```cmd
javac --module-path "lib\javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/*.java
java --module-path "lib\javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.MainApp
```

### Upload to GitHub

To publish this project to GitHub after creating a repository on github.com:

```cmd
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/<your-username>/<repo-name>.git
git push -u origin main
```

---

## Project Structure

```
src/main/java/project1/
├── MainApp.java                # Application entry point, scene/stage manager
├── LoginScreen.java            # Login UI and credential check
├── Dashboard.java              # Main dashboard: lists patients and appointments
├── PatientForm.java            # Patient CRUD UI
├── AppointmentForm.java        # Appointment CRUD UI
├── PatientRecord.java          # Patient data model
├── Appointment.java            # Appointment data model
├── PatientDataManager.java     # DB operations for patients (+ async Runnable tasks)
├── AppointmentDataManager.java # DB operations for appointments (+ async Runnable tasks)
├── DBConnector.java            # SQLite database connection factory
└── DBUtils.java                # SQL utility helpers (legacy)
```

---

## Database Schema

The application uses SQLite with automatic table creation. Tables are created on first run:

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

## Setup

### Prerequisites

- Java 21+
- JavaFX SDK 17 (automatically downloaded by run script)
- SQLite JDBC driver (automatically downloaded by run script)

### Quick Start (Windows)

1. **Run the application:**

   ```bash
   run_app.bat
   ```

   This script will:
   - Download JavaFX SDK 17
   - Download SQLite JDBC driver
   - Compile the project
   - Start the application

### Manual Setup (Alternative)

If you prefer manual setup:

1. **Download JavaFX SDK 17:**
   - Download from: https://gluonhq.com/products/javafx/
   - Extract to a folder (e.g., `C:\javafx-sdk-17.0.6`)

2. **Compile and run:**

   ```bash
   # Set JavaFX path
   set JAVAFX_PATH=C:\path\to\javafx-sdk-17.0.6\lib

   # Compile
   javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/*.java

   # Run
   java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.MainApp
   ```

### Database

The application uses SQLite with automatic database creation:

- Database file: `healthcare.db` (created in project root)
- Tables are created automatically on first run
- No additional setup required!

---

## Architecture Notes

### Scene Navigation

`MainApp` is the central navigator. It holds references to all four screens and exposes `showX()` methods that each screen uses to switch views. Every navigation call closes the current `Stage` and opens a fresh one.

### Async DB Pattern

Database operations are wrapped in inner `Runnable` task classes (e.g. `GetAllPatientsTask`, `CreateAppointmentTask`). These are submitted to a fixed-size `ExecutorService` thread pool defined in `MainApp`. On success or failure, `Platform.runLater()` is used to route the callback back onto the JavaFX UI thread safely.

### SQL Safety

All insert/update/delete operations use `PreparedStatement` with parameterized queries, eliminating SQL injection risk.

---

## Screenshots

> _(Add screenshots of Login, Dashboard, Patient Form, and Appointment Form here)_

---

## License

Developed for academic purposes at Humber Polytechnic. Not intended for production use.
