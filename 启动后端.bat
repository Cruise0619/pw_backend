@echo off
chcp 936 >nul
echo ========================================
echo    Starting Backend Service
echo ========================================
echo.

REM 设置 JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-17

REM 进入项目目录
cd /d d:\pw_backend\pw_backend

echo [1/2] Building project...
call mvn clean package -DskipTests

if errorlevel 1 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo [2/2] Starting application...
echo.
echo ========================================
echo Application is starting...
echo Access: http://localhost:8080
echo Press Ctrl+C to stop
echo ========================================
echo.

java -jar target\pw_backend-1.0.0.jar

if errorlevel 1 (
    echo.
    echo [ERROR] Application failed to start
    echo.
    echo Please check the error messages above
    echo.
)

pause
