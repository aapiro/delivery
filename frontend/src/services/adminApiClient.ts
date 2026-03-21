/**
 * Cliente Axios dedicado al panel admin.
 * - Misma base URL que la app cliente (`API_CONFIG.BASE_URL`).
 * - Token del admin leído del persist de Zustand (no interfiere con `delivery_token` del cliente).
 */
import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { API_CONFIG } from '../constants';
import { ADMIN_ZUSTAND_STORAGE_KEY, isAdminAuthPublicPath } from '../constants/admin';

function readPersistedAdminToken(): string | null {
    if (typeof window === 'undefined') return null;
    try {
        const raw = localStorage.getItem(ADMIN_ZUSTAND_STORAGE_KEY);
        if (!raw) return null;
        const parsed = JSON.parse(raw) as { state?: { token?: string | null } };
        const t = parsed?.state?.token;
        return t && typeof t === 'string' ? t : null;
    } catch {
        return null;
    }
}

export const adminApiClient: AxiosInstance = axios.create({
    baseURL: API_CONFIG.BASE_URL,
    timeout: API_CONFIG.TIMEOUT,
    headers: {
        'Content-Type': 'application/json',
    },
});

adminApiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const path = config.url ?? '';
    if (!isAdminAuthPublicPath(path)) {
        const token = readPersistedAdminToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
    }
    return config;
});
