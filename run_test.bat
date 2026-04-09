@echo off
echo Setting up SQLite JDBC driver...

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib

REM Download SQLite JDBC driver
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar' -OutFile 'lib/sqlite-jdbc-3.42.0.0.jar'}"

if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    echo Failed to download SQLite JDBC driver
    pause
    exit /b 1
)

echo SQLite JDBC driver downloaded successfully

REM Create bin directory
if not exist bin mkdir bin

REM Compile only the simple test
echo Compiling simple database test...
javac -cp "lib/sqlite-jdbc-3.42.0.0.jar" -d bin src/main/java/project1/SimpleDatabaseTest.java

if %errorlevel% neq 0 (
    echo Compilation failed
    pause
    exit /b 1
)

echo Compilation successful

REM Run the simple database test
echo Running simple database test...
java -cp "bin;lib/sqlite-jdbc-3.42.0.0.jar" project1.SimpleDatabaseTest

pause