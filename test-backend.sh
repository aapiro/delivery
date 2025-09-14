#!/bin/bash

echo "=== Verificación de Backend ==="

# Cambiar al directorio del backend
cd /workspace/delivery/nodejs-backend

echo "1. Verificando estado del servidor..."
ps aux | grep node

echo ""
echo "2. Probando rutas disponibles..."

# Probar la ruta principal
echo "Probando GET /:"
curl -s http://localhost:8080/ || echo "No se puede conectar al puerto 8080"

echo ""
echo "3. Verificando base de datos..."
python3 verify_db.py

echo ""
echo "4. Probando rutas API (si el servidor está corriendo)..."
if curl -s http://localhost:8080/api/auth/register > /dev/null 2>&1; then
    echo "✓ Ruta /api/auth/register accesible"
else
    echo "✗ Ruta /api/auth/register no accesible"
fi

echo ""
echo "=== Instrucciones para probar ==="
echo "Para probar el sistema de autenticación:"
echo "1. Asegúrate que el servidor Node.js está corriendo en puerto 8080"
echo "2. Abre http://localhost:3000 en tu navegador"
echo "3. Prueba los formularios de registro y login"

# Mostrar información sobre cómo iniciar el frontend
echo ""
echo "=== Iniciar Frontend ==="
echo "En otro terminal:"
echo "cd /workspace/delivery/frontend && npm start"