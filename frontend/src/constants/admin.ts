/**
 * Configuración del panel de administración (Fase 0 — integración Quarkus).
 * El cliente público y el admin comparten la misma base URL (`REACT_APP_API_URL`)
 * pero usan instancias HTTP distintas para no mezclar tokens.
 *
 * Debe coincidir con `persist.name` en `store/adminStore` → `${CACHE_KEYS.TOKEN}_admin`
 */
export const ADMIN_ZUSTAND_STORAGE_KEY = 'delivery_token_admin' as const;

/** Rutas de auth admin que no deben llevar Bearer (hasta que existan en Quarkus). */
export const ADMIN_AUTH_PUBLIC_PATHS = ['/admin/auth/login', '/admin/auth/refresh'] as const;

export function isAdminAuthPublicPath(url?: string): boolean {
    if (!url) return false;
    return ADMIN_AUTH_PUBLIC_PATHS.some((p) => url.includes(p));
}
