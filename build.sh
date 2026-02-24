#!/bin/bash

echo "Building SillyTavern Android APK..."
echo ""

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "Downloading Gradle Wrapper..."
    gradle wrapper
fi

# Make gradlew executable
chmod +x gradlew

# Build the APK
echo "Building Debug APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    
    # Build Release APK (requires signing)
    echo "Building Release APK..."
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        echo "Release APK location: app/build/outputs/apk/release/app-release-unsigned.apk"
        echo "Note: Release APK needs to be signed before distribution."
    fi
else
    echo ""
    echo "Build failed. Please check the error messages above."
fi
