import { defineConfig, devices } from '@playwright/test';

const baseURL = process.env.E2E_BASE_URL || 'http://localhost:3000';

export default defineConfig({
    testDir: './tests/e2e',
    timeout: 60_000,
    fullyParallel: true,
    retries: process.env.CI ? 2 : 0,
    use: {
        baseURL,
        headless: true,
        trace: 'retain-on-failure',
        viewport: { width: 1280, height: 720 },
    },
    webServer: {
        // Levanta la app CRA para que las pruebas sean realmente E2E en UI.
        command: 'npm run start',
        url: baseURL,
        timeout: 120_000,
        reuseExistingServer: !process.env.CI,
        env: {
            PORT: '3000',
        },
    },
    projects: [
        {
            name: 'chromium',
            use: { ...devices['Desktop Chrome'] },
        },
    ],
});

