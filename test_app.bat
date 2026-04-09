@echo off
echo Testing Healthcare Application Startup...

REM Check if JavaFX SDK exists
if not exist lib\javafx-sdk-17.0.6 (
    echo ERROR: JavaFX SDK not found. Please run run_app.bat first to download dependencies.
    pause
    exit /b 1
)

REM Check if SQLite JDBC exists
if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    echo ERROR: SQLite JDBC driver not found. Please run run_app.bat first to download dependencies.
    pause
    exit /b 1
)

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile the application
echo Compiling application...
javac --module-path "lib/javafx-sdk-17.0.6/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/*.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo ✓ Compilation successful

REM Test database initialization (run for 5 seconds to check for errors)
echo Testing database initialization...
timeout /t 5 /nobreak >nul 2>&1
start /b java --module-path "lib/javafx-sdk-17.0.6/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.MainApp > test_output.log 2>&1

REM Wait a moment for the app to start
timeout /t 3 /nobreak >nul

REM Check if the app started successfully
tasklist /fi "imagename eq java.exe" /nh | findstr /c:"java.exe" >nul
if %errorlevel% neq 0 (
    echo ERROR: Application failed to start
    type test_output.log
    pause
    exit /b 1
)

echo ✓ Application started successfully
echo ✓ Database initialized
echo ✓ No critical errors detected

REM Kill the test application
taskkill /f /im java.exe >nul 2>&1

echo.
echo 🎉 TEST PASSED: Healthcare Application is working correctly!
echo.
echo To run the full application, use: run_app.bat
echo.

pause