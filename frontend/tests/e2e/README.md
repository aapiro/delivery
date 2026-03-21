# Frontend E2E (Playwright)

Este proyecto usa **Playwright** para pruebas E2E del panel de administración.

## Requisitos

1. Instala navegadores:
   - `npx playwright install chromium`

## Ejecutar

- `npm run test:e2e`

## Nota sobre la API

Los tests incluidos hacen **mock** de endpoints críticos (`/admin/auth/login` y `/admin/dashboard/stats`) para que el flujo UI sea estable incluso si el backend no está levantado.

Cuando quieras integrar con backend real, se puede quitar/condicionar el mock por entorno.

