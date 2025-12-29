import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { Admin, AdminPermission, AdminRole } from '../types';
import { adminAuth } from '../services/admin';
import { CACHE_KEYS } from '../constants';

interface AdminState {
    // Auth state
    admin: Admin | null;
    token: string | null;
    refreshToken: string | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    error: string | null;

    // Actions
    login: (email: string, password: string, rememberMe?: boolean) => Promise<void>;
    logout: () => Promise<void>;
    refreshAuth: () => Promise<void>;
    updateProfile: (data: Partial<Admin>) => Promise<void>;
    clearError: () => void;

    // Permission helpers
    hasPermission: (permission: AdminPermission) => boolean;
    hasRole: (role: AdminRole) => boolean;
    hasAnyPermission: (permissions: AdminPermission[]) => boolean;
    hasAllPermissions: (permissions: AdminPermission[]) => boolean;
}

export const useAdminStore = create<AdminState>()(
    persist(
        (set, get) => ({
            // Initial state
            admin: null,
            token: null,
            refreshToken: null,
            isAuthenticated: false,
            isLoading: false,
            error: null,

            // Actions
            login: async (email: string, password: string, rememberMe = false) => {
                set({ isLoading: true, error: null });
                
                try {
                    const response = await adminAuth.login({ email, password, rememberMe });
                    
                    set({
                        admin: response.admin,
                        token: response.token,
                        refreshToken: response.refreshToken,
                        isAuthenticated: true,
                        isLoading: false,
                        error: null,
                    });

                    // Set token in axios defaults for future requests
                    const { api } = await import('../services/api');
                    api.setAuthToken(response.token);
                    
                } catch (error: any) {
                    set({
                        admin: null,
                        token: null,
                        refreshToken: null,
                        isAuthenticated: false,
                        isLoading: false,
                        error: error.response?.data?.message || 'Error al iniciar sesión',
                    });
                    throw error;
                }
            },

            logout: async () => {
                set({ isLoading: true });
                
                try {
                    await adminAuth.logout();
                } catch (error) {
                    // Continue with logout even if API call fails
                    console.error('Error during logout:', error);
                } finally {
                    set({
                        admin: null,
                        token: null,
                        refreshToken: null,
                        isAuthenticated: false,
                        isLoading: false,
                        error: null,
                    });

                    // Remove token from axios defaults
                    const { api } = await import('../services/api');
                    api.removeAuthToken();
                }
            },

            refreshAuth: async () => {
                const { refreshToken } = get();
                
                if (!refreshToken) {
                    throw new Error('No refresh token available');
                }

                set({ isLoading: true, error: null });
                
                try {
                    const response = await adminAuth.refreshToken();
                    
                    set({
                        admin: response.admin,
                        token: response.token,
                        refreshToken: response.refreshToken,
                        isAuthenticated: true,
                        isLoading: false,
                        error: null,
                    });

                    // Update token in axios defaults
                    const { api } = await import('../services/api');
                    api.setAuthToken(response.token);
                    
                } catch (error: any) {
                    set({
                        admin: null,
                        token: null,
                        refreshToken: null,
                        isAuthenticated: false,
                        isLoading: false,
                        error: error.response?.data?.message || 'Error al renovar sesión',
                    });
                    throw error;
                }
            },

            updateProfile: async (data: Partial<Admin>) => {
                set({ isLoading: true, error: null });
                
                try {
                    const updatedAdmin = await adminAuth.updateProfile(data);
                    
                    set({
                        admin: updatedAdmin,
                        isLoading: false,
                        error: null,
                    });
                } catch (error: any) {
                    set({
                        isLoading: false,
                        error: error.response?.data?.message || 'Error al actualizar perfil',
                    });
                    throw error;
                }
            },

            clearError: () => {
                set({ error: null });
            },

            // Permission helpers
            /*hasPermission: (permission: AdminPermission): boolean => { //todo descomentar este bloque cuando se programe la autorizacion
                const { admin } = get();
                if (!admin || !admin.isActive) return false;
                
                // Super admin has all permissions
                if (admin.role === AdminRole.SUPER_ADMIN) return true;
                
                return admin.permissions.includes(permission);
            },*/
            hasPermission: (permission: AdminPermission) => {
                // return state.permissions.includes(permission); // Lógica real
                return true; // Lógica temporal para ver todo mientras programas
            },

            hasRole: (role: AdminRole): boolean => {
                const { admin } = get();
                if (!admin || !admin.isActive) return false;
                
                return admin.role === role;
            },

            hasAnyPermission: (permissions: AdminPermission[]): boolean => {
                const { admin } = get();
                if (!admin || !admin.isActive) return false;
                
                // Super admin has all permissions
                if (admin.role === AdminRole.SUPER_ADMIN) return true;
                
                return permissions.some(permission => admin.permissions.includes(permission));
            },

            hasAllPermissions: (permissions: AdminPermission[]): boolean => {
                const { admin } = get();
                if (!admin || !admin.isActive) return false;
                
                // Super admin has all permissions
                if (admin.role === AdminRole.SUPER_ADMIN) return true;
                
                return permissions.every(permission => admin.permissions.includes(permission));
            },
        }),
        {
            name: `${CACHE_KEYS.TOKEN}_admin`,
            partialize: (state) => ({
                admin: state.admin,
                token: state.token,
                refreshToken: state.refreshToken,
                isAuthenticated: state.isAuthenticated,
            }),
        }
    )
);

// Initialize auth token on store creation
const initializeAuth = async () => {
    const { token } = useAdminStore.getState();
    if (token) {
        const { api } = await import('../services/api');
        api.setAuthToken(token);
    }
};

initializeAuth();

export default useAdminStore;