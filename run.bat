@echo off

cd /d "C:\Users\dell\Downloads\CODE Source (2)-20260610T002038Z-3-001\CODE Source (2)\CODE Source"

echo Cleaning old class files...
del /s /q *.class

echo Compiling Java files...
javac --module-path "C:\Users\dell\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml *.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Starting application...
java --module-path "C:\Users\dell\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics SmartFarmingApp

pause