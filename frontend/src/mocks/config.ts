/**
 * Mock system configuration
 * =========================
 *
 * Origen de la configuración (Create React App solo inyecta env en build):
 *   - `.env.development`  → usado con `npm start` (NODE_ENV=development)
 *   - `.env.local`        → overrides locales (no commitear secretos)
 *   - `.env`              → valores por defecto
 *
 * Variables:
 *   REACT_APP_USE_MOCK=true   → datos locales (sin backend). Cambia a false para Quarkus.
 *   REACT_APP_API_URL=...     → base URL del API (p. ej. http://localhost:8080/api)
 *   REACT_APP_MOCK_DELAY=400  → latencia simulada en mocks (ms)
 *
 * Override en runtime (consola del navegador):
 *   window.__DELIVERY_MOCK__ = true;   // activar mocks
 *   window.__DELIVERY_MOCK__ = false;  // desactivar mocks
 */

declare global {
    interface Window {
        __DELIVERY_MOCK__?: boolean;
    }
}

/** `true` si debemos usar datos mock (no llamar al backend). */
export const isMockEnabled = (): boolean => {
    if (typeof window !== 'undefined' && window.__DELIVERY_MOCK__ !== undefined) {
        return window.__DELIVERY_MOCK__;
    }
    return process.env.REACT_APP_USE_MOCK === 'true';
};

/** Latencia simulada (ms) cuando REACT_APP_USE_MOCK=true. */
export const getMockDelay = (): number => {
    const raw = process.env.REACT_APP_MOCK_DELAY;
    const parsed = raw ? parseInt(raw, 10) : NaN;
    return Number.isNaN(parsed) ? 300 : parsed;
};

export const delay = (ms?: number): Promise<void> =>
    new Promise((resolve) => setTimeout(resolve, ms ?? getMockDelay()));
