import { test, expect } from '@playwright/test';

test('Admin login smoke (mocked API) -> dashboard -> profile', async ({ page }) => {
    // Reset persisted Zustand admin state from previous runs.
    await page.goto('/');
    await page.evaluate(() => {
        localStorage.clear();
    });

    const adminEmail = 'admin@delivery.local';
    const adminPassword = 'admin123';

    const adminPayload = {
        admin: {
            id: 1,
            email: adminEmail,
            name: 'Administrador',
            role: 'SUPER_ADMIN',
            permissions: [
                'VIEW_RESTAURANTS',
                'VIEW_DISHES',
                'VIEW_ORDERS',
                'VIEW_USERS',
                'VIEW_CATEGORIES',
                'VIEW_ANALYTICS',
                'SYSTEM_SETTINGS',
            ],
            isActive: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
        },
        token: 'e2e-token',
        refreshToken: 'e2e-refresh-token',
        expiresIn: 900,
    };

    // Login endpoint (front calls /api/admin/auth/login via adminApiClient).
    await page.route('**/api/admin/auth/login', async (route) => {
        if (route.request().method() !== 'POST') {
            return route.fallback();
        }

        const raw = route.request().postData();
        const body = raw ? (JSON.parse(raw) as { email?: string; password?: string }) : {};
        const emailOk = body.email === adminEmail;
        const passwordOk = body.password === adminPassword;

        if (!emailOk || !passwordOk) {
            return route.fulfill({
                status: 401,
                contentType: 'application/json',
                body: JSON.stringify({ error: 'Invalid admin credentials' }),
            });
        }

        return route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(adminPayload),
        });
    });

    // Dashboard stats (React Query calls it right after login).
    await page.route('**/api/admin/dashboard/stats', async (route) => {
        if (route.request().method() !== 'GET') {
            return route.fallback();
        }

        return route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({}),
        });
    });

    await page.goto('/admin/login');

    await page.getByPlaceholder('Email').fill(adminEmail);
    await page.getByPlaceholder('Contraseña').fill(adminPassword);
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    await expect(page).toHaveURL(/\/admin\/?$/);
    await expect(page.getByText('Dashboard')).toBeVisible();

    await page.getByRole('link', { name: 'Mi perfil' }).click();
    await expect(page.getByRole('heading', { name: 'Mi perfil', level: 1 })).toBeVisible();
    await expect(page.getByText(adminEmail)).toBeVisible();
});

