import React, { createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../hooks/useAuthStore';
import { apiService } from '../../services/api';

interface AuthContextType {
  user: any | null;
  isAuthenticated: boolean;
  login: (userData: any) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<any | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const navigate = useNavigate();
  const { login: storeLogin, logout: storeLogout } = useAuthStore();

  useEffect(() => {
    // Verificar si hay un token guardado en localStorage
    const token = localStorage.getItem('token');
    if (token) {
      // Aquí podrías hacer una llamada al backend para verificar el token
      setIsAuthenticated(true);
      
      // Obtener información del usuario desde localStorage o hacer llamada a API
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        setUser(JSON.parse(storedUser));
        storeLogin(JSON.parse(storedUser));
      }
    }
  }, [storeLogin]);

  const login = (userData: any) => {
    setUser(userData);
    setIsAuthenticated(true);
    storeLogin(userData);
    
    // Guardar en localStorage
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    setIsAuthenticated(false);
    storeLogout();
    
    // Limpiar localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    
    // Redirigir al login
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};