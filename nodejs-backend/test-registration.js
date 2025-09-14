// Script de prueba para verificar el funcionamiento del registro

const sqlite3 = require('sqlite3').verbose();
const bcrypt = require('bcrypt');

async function testRegistration() {
    console.log("=== Prueba de Registro ===");
    
    // Conectar a la base de datos
    const db = new sqlite3.Database('./database.sqlite');
    
    try {
        // Verificar que existen usuarios
        console.log("\nUsuarios actuales:");
        db.serialize(() => {
            db.all('SELECT * FROM users', [], (err, rows) => {
                if (err) {
                    console.error("Error al consultar usuarios:", err);
                    return;
                }
                
                rows.forEach(row => {
                    console.log(`ID: ${row.id}, Nombre: ${row.name}, Email: ${row.email}`);
                });
            });
        });
        
        // Probar registro de nuevo usuario
        const newUser = {
            name: 'Test User',
            email: 'test@example.com',
            password: 'password123'
        };
        
        console.log("\nRegistrando nuevo usuario...");
        
        // Hash de contraseña
        const hashedPassword = await bcrypt.hash(newUser.password, 10);
        console.log("Contraseña hasheada:", hashedPassword);
        
        db.serialize(() => {
            const stmt = db.prepare('INSERT INTO users (name, email, password) VALUES (?, ?, ?)');
            stmt.run([newUser.name, newUser.email, hashedPassword], function(err) {
                if (err) {
                    console.error("Error al registrar usuario:", err);
                } else {
                    console.log(`Usuario registrado con ID: ${this.lastID}`);
                    
                    // Verificar el nuevo registro
                    db.all('SELECT * FROM users WHERE id = ?', [this.lastID], (err, rows) => {
                        if (err) {
                            console.error("Error al consultar usuario recién creado:", err);
                        } else {
                            console.log("\nUsuario registrado:");
                            rows.forEach(row => {
                                console.log(`ID: ${row.id}, Nombre: ${row.name}, Email: ${row.email}`);
                            });
                        }
                    });
                }
            });
            
            stmt.finalize();
        });
        
    } catch (error) {
        console.error("Error en la prueba:", error);
    } finally {
        db.close();
    }
}

// Ejecutar la prueba
testRegistration().catch(console.error);