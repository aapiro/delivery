/**
 * Normaliza cuerpos JSON del backend Quarkus (objeto en raíz) frente a
 * envoltorios tipo `{ data: ... }` que a veces usa el front.
 */

/** Página estilo Spring/OpenAPI: content, totalElements, … */
export function unwrapPagedResponse(raw: unknown): {
    content: unknown[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
} {
    const body = raw as Record<string, unknown> | null | undefined;
    if (!body || typeof body !== 'object') {
        return { content: [], totalElements: 0, totalPages: 0, number: 0, size: 0 };
    }
    const page =
        Array.isArray((body as { content?: unknown }).content) ? body : (body.data as Record<string, unknown> | undefined);
    if (!page || typeof page !== 'object') {
        return { content: [], totalElements: 0, totalPages: 0, number: 0, size: 0 };
    }
    const content = Array.isArray(page.content) ? page.content : [];
    return {
        content,
        totalElements: Number(page.totalElements ?? content.length),
        totalPages: Number(page.totalPages ?? 1),
        number: Number(page.number ?? 0),
        size: Number(page.size ?? content.length),
    };
}

/** Entidad suelta o envuelta en `{ data: T }`. */
export function unwrapEntity<T = unknown>(raw: unknown): T | Record<string, never> {
    if (raw == null) return {};
    if (typeof raw !== 'object') return {} as T;
    const o = raw as Record<string, unknown>;
    if ('data' in o && o.data !== undefined && typeof o.data === 'object' && !('id' in o && o.id !== undefined)) {
        return o.data as T;
    }
    return raw as T;
}

/** Lista en raíz o dentro de `data`. */
export function unwrapList<T = unknown>(raw: unknown): T[] {
    if (raw == null) return [];
    if (Array.isArray(raw)) return raw as T[];
    const o = raw as Record<string, unknown>;
    if (Array.isArray(o.data)) return o.data as T[];
    return [];
}
