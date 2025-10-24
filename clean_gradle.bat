@echo off
echo Cleaning Gradle cache and temporary files...
echo.

REM Clean project build directories
echo Cleaning project build directories...
call gradlew.bat clean

REM Clean Gradle cache (optional - uncomment if needed)
REM echo Cleaning Gradle cache...
REM rmdir /s /q %USERPROFILE%\.gradle\caches

echo.
echo Done! Temporary files have been cleaned.
echo Note: Next build will take longer as it needs to rebuild everything.
pause

