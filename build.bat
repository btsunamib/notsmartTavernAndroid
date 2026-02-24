@echo off
echo Building SillyTavern Android APK...
echo.

REM Check if gradlew exists
if not exist "gradlew.bat" (
    echo Downloading Gradle Wrapper...
    call gradle wrapper
)

REM Build the APK
echo Building Debug APK...
call gradlew assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo APK location: app\build\outputs\apk\debug\app-debug.apk
    echo.
    
    REM Build Release APK (requires signing)
    echo Building Release APK...
    call gradlew assembleRelease
    
    if %ERRORLEVEL% EQU 0 (
        echo Release APK location: app\build\outputs\apk\release\app-release-unsigned.apk
        echo Note: Release APK needs to be signed before distribution.
    )
) else (
    echo.
    echo Build failed. Please check the error messages above.
)

pause
