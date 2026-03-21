/**
 * Acceso HTTP del panel admin sobre `adminApiClient`.
 * Contratos del frontend: muchas respuestas vienen como `{ data: T }` (ApiResponse) o planas (Quarkus).
 */
import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { adminApiClient } from './adminApiClient';

/**
 * Desenvuelve `{ data: T }` típico de ApiResponse sin romper:
 * - páginas Spring/OpenAPI (`content`)
 * - `PaginatedResponse` del front (`pagination`)
 */
export function pickData<T>(body: unknown): T {
    if (body == null || typeof body !== 'object') return body as T;
    const o = body as Record<string, unknown>;
    if (
        'data' in o &&
        o.data !== undefined &&
        !('content' in o) &&
        !('pagination' in o)
    ) {
        return o.data as T;
    }
    return body as T;
}

async function handle<T>(p: Promise<AxiosResponse<T>>): Promise<T> {
    const res = await p;
    return res.data;
}

export const adminHttp = {
    get: <T>(url: string, config?: AxiosRequestConfig): Promise<T> => handle(adminApiClient.get<T>(url, config)),

    post: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        handle(adminApiClient.post<T>(url, data, config)),

    put: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        handle(adminApiClient.put<T>(url, data, config)),

    patch: <T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> =>
        handle(adminApiClient.patch<T>(url, data, config)),

    delete: <T>(url: string, config?: AxiosRequestConfig): Promise<T> =>
        handle(adminApiClient.delete<T>(url, config)),
};
