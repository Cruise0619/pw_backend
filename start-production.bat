@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul
title PwBackend Production Server

REM ============================================================
REM  PwBackend - Production Startup Script
REM
REM  Features:
REM   - Automatic SSL detection in THIS folder:
REM       1. an existing keystore (*.p12 / *.pfx / *.jks), or
REM       2. a PEM certificate (*.crt / *.pem) plus a private
REM          key (*.key). In that case a PKCS12 keystore is
REM          generated on the fly by tools\MakeKeystore.java.
REM     If neither exists the server starts in HTTP mode.
REM   - Builds the production jar automatically when missing.
REM   - Writes the application log to  logs\pw_backend.log
REM
REM  Overridable environment variables (set before running):
REM     JAVA_HOME             - JDK 17 home (used if auto-detect fails)
REM     APP_HTTPS_PORT        - HTTPS port when SSL is used (default 8443)
REM     APP_HTTP_PORT         - HTTP port used as SSL fallback (default 8081)
REM     JVM_MEM               - JVM memory flags (default -Xms256m -Xmx1g)
REM     SSL_KEYSTORE_PASSWORD - password for the generated keystore
REM                             (default pwbackend-ssl-pass)
REM     REBUILD               - set to 1 to force rebuilding the jar
REM ============================================================

set "APP_DIR=%~dp0"

echo ============================================================
echo    PwBackend Production Server
echo ============================================================
echo.

REM ---------- 1. Switch to the script directory ----------
cd /d "%APP_DIR%"

REM ---------- 2. Locate Java 17 ----------
set "JDK17=C:\Program Files\JAVA\jdk-17.0.2+8"
if not exist "%JDK17%\bin\java.exe" set "JDK17=C:\Program Files\Java\jdk-17"
if not exist "%JDK17%\bin\java.exe" (
    if defined JAVA_HOME (
        set "JDK17=%JAVA_HOME%"
    )
)
if not exist "%JDK17%\bin\java.exe" (
    echo [ERROR] Java 17 was not found.
    echo         Install JDK 17 or set JAVA_HOME to a JDK 17 install.
    echo.
    pause
    exit /b 1
)
set "JAVA_CMD=%JDK17%\bin\java.exe"
set "JAVA_HOME=%JDK17%"
echo [INFO ] Using Java  : %JAVA_CMD%
echo.

REM ---------- 3. Locate or build the production jar ----------
set "JAR_FILE=%APP_DIR%pw_backend\target\pw_backend-1.0.0.jar"
if "%REBUILD%"=="1" (
    echo [INFO ] Rebuild requested. Rebuilding project...
    goto :build
)
if not exist "%JAR_FILE%" (
    echo [INFO ] Production jar not found. Building project...
    goto :build
)
goto :jar_ready

:build
where mvn >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Maven was not found on PATH. Cannot build the jar.
    echo.
    pause
    exit /b 1
)
echo [1/2] Building project (mvn clean package -DskipTests)...
call mvn -f "%APP_DIR%pw_backend\pom.xml" clean package -DskipTests
if errorlevel 1 (
    echo [ERROR] Build failed. Fix the compilation errors and try again.
    echo.
    pause
    exit /b 1
)
if not exist "%JAR_FILE%" (
    echo [ERROR] Build finished but the jar is missing: %JAR_FILE%
    echo.
    pause
    exit /b 1
)

:jar_ready
echo [INFO ] Using jar : %JAR_FILE%
echo.

REM ---------- 4. Auto-detect SSL material in this folder ----------
set "KEYSTORE_FILE="
set "SSL_CERT="
set "SSL_KEY="
set "SSL_OK="
set "SSL_MODE=HTTP"
if not defined SSL_KEYSTORE_PASSWORD set "SSL_KEYSTORE_PASSWORD=pwbackend-ssl-pass"

REM Priority 1: an existing keystore next to this script.
for %%F in ("%APP_DIR%*.p12" "%APP_DIR%*.pfx" "%APP_DIR%*.jks") do set "KEYSTORE_FILE=%%~fF"
if defined KEYSTORE_FILE (
    set "SSL_OK=1"
) else (
    REM Priority 2: PEM certificate + private key.
    for %%F in ("%APP_DIR%*.crt") do set "SSL_CERT=%%~fF"
    if not defined SSL_CERT (
        for %%F in ("%APP_DIR%*.pem") do set "SSL_CERT=%%~fF"
    )
    for %%F in ("%APP_DIR%*.key") do set "SSL_KEY=%%~fF"
    if defined SSL_CERT if defined SSL_KEY set "SSL_OK=1"
)

if defined SSL_OK (
    if not defined KEYSTORE_FILE (
        echo [INFO ] Certificate found : !SSL_CERT!
        echo [INFO ] Private key found : !SSL_KEY!
        if not exist "%APP_DIR%tools\MakeKeystore.class" (
            echo [INFO ] Compiling keystore helper...
            "%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -d "%APP_DIR%tools" "%APP_DIR%tools\MakeKeystore.java"
            if errorlevel 1 (
                echo [ERROR] Failed to compile the keystore helper.
                echo        Continuing in HTTP mode.
                set "SSL_OK="
            )
        )
        if defined SSL_OK (
            echo [INFO ] Generating PKCS12 keystore from PEM files...
            "%JAVA_CMD%" -cp "%APP_DIR%tools" MakeKeystore "!SSL_CERT!" "!SSL_KEY!" "%APP_DIR%ssl\pw_backend.p12" "!SSL_KEYSTORE_PASSWORD!"
            if errorlevel 1 (
                echo [ERROR] Failed to generate the SSL keystore.
                echo        Continuing in HTTP mode.
                set "SSL_OK="
            )
        )
        if defined SSL_OK set "KEYSTORE_FILE=%APP_DIR%ssl\pw_backend.p12"
    ) else (
        echo [INFO ] Keystore found : !KEYSTORE_FILE!
    )
    if defined SSL_OK (
        set "KEYSTORE_TYPE=PKCS12"
        set "KS_EXT=!KEYSTORE_FILE:~-4!"
        if /i "!KS_EXT!"==".jks" set "KEYSTORE_TYPE=JKS"
        set "SSL_MODE=HTTPS"
    )
) else (
    if defined SSL_CERT (
        echo [WARN ] Certificate found but no matching .key file.
        echo        Starting in HTTP mode.
    ) else (
        if defined SSL_KEY (
            echo [WARN ] Private key found but no matching .crt/.pem file.
            echo        Starting in HTTP mode.
        ) else (
            echo [INFO ] No SSL certificate or keystore found.
            echo        Starting in HTTP mode.
        )
    )
)
echo.

REM ---------- 5. Ports and JVM settings ----------
if not defined APP_HTTPS_PORT set "APP_HTTPS_PORT=8443"
if not defined APP_HTTP_PORT  set "APP_HTTP_PORT=8081"
if not defined JVM_MEM         set "JVM_MEM=-Xms256m -Xmx1g"

set "APP_ARGS=--logging.file.name=logs/pw_backend.log"
if "%SSL_MODE%"=="HTTPS" (
    set "KS_URI=file:%KEYSTORE_FILE:\=/%"
    set "SSL_ARGS=--server.ssl.enabled=true --server.ssl.key-store=!KS_URI! --server.ssl.key-store-password=!SSL_KEYSTORE_PASSWORD! --server.ssl.key-store-type=!KEYSTORE_TYPE!"
    set "APP_ARGS=!APP_ARGS! --server.port=!APP_HTTPS_PORT! !SSL_ARGS!"
    set "ACCESS_URL=https://localhost:!APP_HTTPS_PORT!"
) else (
    set "APP_ARGS=!APP_ARGS! --server.port=!APP_HTTP_PORT!"
    set "ACCESS_URL=http://localhost:!APP_HTTP_PORT!"
)

REM ---------- 6. Launch ----------
echo ============================================================
echo    Mode    : %SSL_MODE%
echo    Access  : %ACCESS_URL%
echo    Log     : %APP_DIR%logs\pw_backend.log
echo    Press Ctrl+C to stop
echo ============================================================
echo.

"%JAVA_CMD%" %JVM_MEM% -jar "%JAR_FILE%" %APP_ARGS%
set "EXIT_CODE=%ERRORLEVEL%"

echo.
if "%EXIT_CODE%"=="0" (
    echo [INFO ] Application stopped normally.
) else (
    echo [ERROR] Application exited with code %EXIT_CODE%.
    echo        Review the log above or logs\pw_backend.log for details.
)
echo.
pause
endlocal
