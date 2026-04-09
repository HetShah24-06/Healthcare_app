@echo off
echo Setting up JavaFX and SQLite for Healthcare App...

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib

REM Download JavaFX SDK if not present
if not exist lib\javafx-sdk-17.0.6 (
    echo Downloading JavaFX SDK...
    powershell -Command "& {Invoke-WebRequest -Uri 'https://download2.gluonhq.com/openjfx/17.0.6/openjfx-17.0.6_windows-x64_bin-sdk.zip' -OutFile 'lib/javafx-sdk.zip'}"
    if not exist lib\javafx-sdk.zip (
        echo Failed to download JavaFX SDK
        pause
        exit /b 1
    )

    echo Extracting JavaFX SDK...
    powershell -Command "& {Expand-Archive -Path 'lib/javafx-sdk.zip' -DestinationPath 'lib' -Force}"
    move lib\javafx-sdk-17.0.6 lib\javafx-sdk-17.0.6_windows-x64_bin-sdk
)

REM Download SQLite JDBC driver if not present
if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    echo Downloading SQLite JDBC driver...
    curl -L -o lib/sqlite-jdbc-3.42.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar
)

REM Create bin directory
if not exist bin mkdir bin

REM Compile all Java files
echo Compiling JavaFX application...
javac --module-path "lib/javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/*.java

if %errorlevel% neq 0 (
    echo Compilation failed
    pause
    exit /b 1
)

echo Compilation successful!

REM Run the JavaFX application
echo Starting Healthcare Appointment System...
java --module-path "lib/javafx-sdk-17.0.6_windows-x64_bin-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.MainApp

pause