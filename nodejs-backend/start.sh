#!/bin/bash

# Script para iniciar el servidor Node.js con autenticación de usuarios implementada

echo "Iniciando servidor backend..."

# Verificar que existen los archivos necesarios
if [ ! -f "package.json" ]; then
    echo "Error: No se encontró package.json"
    exit 1
fi

# Instalar dependencias si no están presentes
if [ ! -d "node_modules" ]; then
    echo "Instalando dependencias..."
    npm install || { echo "Error al instalar dependencias"; exit 1; }
fi

# Iniciar el servidor Node.js
echo "Iniciando servidor en http://localhost:8080"
node start-server.js