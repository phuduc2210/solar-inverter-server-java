#!/bin/bash

JAVA_DIR="/home/haind400/solar-inverter/solar-inverter-server-java/src/main/java/solar/server"

cd "$JAVA_DIR" || exit 1

echo "Fixing package declarations..."

# Function to fix package in a file
fix_package() {
    local file="$1"
    local package="$2"
    
    if [ -f "$file" ]; then
        # Remove existing package line if present
        sed -i '/^package /d' "$file"
        # Add correct package at the beginning
        sed -i "1i package $package;" "$file"
    fi
}

echo "Fixing cache/"
for file in cache/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.cache"
done

echo "Fixing configs/"
for file in configs/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.configs"
done

echo "Fixing dao/"
for file in dao/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.dao"
done

echo "Fixing models/"
for file in models/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.models"
done

echo "Fixing socket/handlers/"
for file in socket/handlers/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.socket.handlers"
done

echo "Fixing socket/tcp/"
for file in socket/tcp/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.socket.tcp"
done

echo "Fixing util/"
for file in util/*.java; do
    [ -f "$file" ] && fix_package "$file" "solar.server.util"
done

echo "Fixing Main.java"
fix_package "Main.java" "solar.server"

echo "Done! Package declarations fixed."