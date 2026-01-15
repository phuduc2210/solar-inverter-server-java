#!/bin/bash

JAVA_DIR="/home/haind400/solar-inverter/solar-inverter-server-java/src/main/java/solar/server"

cd "$JAVA_DIR"

echo "Fixing package declarations..."

# Fix cache
for file in cache/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.cache;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix configs
for file in configs/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.configs;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix dao
for file in dao/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.dao;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix models
for file in models/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.models;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix socket handlers
for file in socket/handlers/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.socket.handlers;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix socket tcp
for file in socket/tcp/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.socket.tcp;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

# Fix util
for file in util/*.java; do
    if [ -f "$file" ]; then
        sed -i '1s/^/package solar.server.util;\n/' "$file"
        sed -i '2s/^package.*//' "$file"
    fi
done

echo "Done!"