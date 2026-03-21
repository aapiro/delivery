/**
 * Mensaje legible para pantallas de error del admin (Axios u otros).
 */
export function getAdminErrorMessage(error: unknown, fallback: string): string {
    if (error == null) return fallback;
    if (typeof error === 'string') return error;
    if (error instanceof Error && error.message) return error.message;

    if (typeof error === 'object' && 'response' in error) {
        const res = (error as { response?: { data?: { message?: unknown } } }).response?.data;
        const msg = res?.message;
        if (typeof msg === 'string') return msg;
    }

    return fallback;
}
