@echo off
cd /d "%~dp0"
set OUT=bin
set CP=%OUT%;resources;cards.csv;cells.csv;monsters.csv

if not defined JAVA_FX_HOME (
    echo ERROR: Set JAVA_FX_HOME to your JavaFX SDK "lib" folder.
    echo Example: set JAVA_FX_HOME=C:\javafx-sdk-21.0.11\lib
    exit /b 1
)

if not exist "%JAVA_FX_HOME%\javafx.controls.jar" (
    echo ERROR: javafx.controls.jar not found in:
    echo   %JAVA_FX_HOME%
    echo JAVA_FX_HOME must point to the "lib" folder inside the SDK zip.
    exit /b 1
)

set JFX_MODS=javafx.controls,javafx.graphics,javafx.base
set JFX_FLAGS=--module-path "%JAVA_FX_HOME%" --add-modules %JFX_MODS%

if not exist "%OUT%" mkdir "%OUT%"

echo Compiling engine and GUI...
javac %JFX_FLAGS% -encoding UTF-8 -d "%OUT%" -sourcepath "src" "src\game\gui\Main.java"
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)
image.png
echo Starting DoorDasH...
java %JFX_FLAGS% -cp "%CP%" game.gui.Main
