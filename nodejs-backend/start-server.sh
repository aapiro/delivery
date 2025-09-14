#!/bin/bash

echo "Iniciando servidor Node.js..."

# Cambiar al directorio del backend
cd /workspace/delivery/nodejs-backend

# Verificar que el archivo server.js existe
if [ ! -f "server.js" ]; then
    echo "Creando archivo server.js..."
    
    cat > server.js << 'EOF'
const express = require('express');
const cors = require('cors');
const path = require('path');

// Importar modelos y rutas
const User = require('./src/models/User');
const authRoutes = require('./src/routes/auth');

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(cors());
app.use(express.json());

// Rutas de autenticación
app.use('/api/auth', authRoutes);

// Ruta principal (para frontend)
app.get('/', (req, res) => {
  res.send('Servidor API corriendo');
});

console.log(`Servidor iniciado en http://localhost:${PORT}`);
app.listen(PORT, () => {
  console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
EOF
fi

echo "Iniciando servidor..."
node server.js