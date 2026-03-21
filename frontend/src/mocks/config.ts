/**
 * Mock system configuration.
 *
 * Reads REACT_APP_USE_MOCK from the environment.  The flag can also be toggled
 * at runtime through the browser console:
 *   window.__DELIVERY_MOCK__ = true;   // activar mocks
 *   window.__DELIVERY_MOCK__ = false;  // desactivar mocks
 */

declare global {
    interface Window {
        __DELIVERY_MOCK__?: boolean;
    }
}

/** Returns `true` when mock mode is active. */
export const isMockEnabled = (): boolean => {
    // Runtime override takes precedence
    if (typeof window !== 'undefined' && window.__DELIVERY_MOCK__ !== undefined) {
        return window.__DELIVERY_MOCK__;
    }
    return process.env.REACT_APP_USE_MOCK === 'true';
};

/** Simulated network delay (ms). */
export const getMockDelay = (): number => {
    const raw = process.env.REACT_APP_MOCK_DELAY;
    const parsed = raw ? parseInt(raw, 10) : NaN;
    return Number.isNaN(parsed) ? 300 : parsed;
};

/** Helper: pause for the configured mock delay. */
export const delay = (ms?: number): Promise<void> =>
    new Promise((resolve) => setTimeout(resolve, ms ?? getMockDelay()));

