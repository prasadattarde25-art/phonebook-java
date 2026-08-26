const { test, expect } = require('@playwright/test');

test.skip(
    ({ browserName }) => browserName !== 'chromium',
    'Add 200 contacts load test runs only on Chromium'
);

test.setTimeout(300000);

test('Add 200 contacts', async ({ page }) => {

    // ==========================================
    // 1. Open Login Page
    // ==========================================

    await page.goto('/login');

    // ==========================================
    // 2. Login
    // ==========================================

    await page.getByPlaceholder('Username').fill('admin');
    await page.getByPlaceholder('Password').fill('admin123');

    await page.getByRole('button', { name: 'Login' }).click();

    await page.waitForURL('**/');

    // ==========================================
    // 3. Verify Contact Form
    // ==========================================

    await expect(
        page.getByPlaceholder('Name')
    ).toBeVisible();

    await expect(
        page.getByPlaceholder('Phone Number')
    ).toBeVisible();

    await expect(
        page.getByPlaceholder('Email')
    ).toBeVisible();

    await expect(
        page.getByPlaceholder('Address')
    ).toBeVisible();

    // ==========================================
    // 4. Generate unique ID for this test run
    // ==========================================

    const runId = String(Date.now()).slice(-6);

    // ==========================================
    // 5. Add 200 Contacts
    // ==========================================

    for (let i = 1; i <= 200; i++) {

        const name = `Playwright User ${runId}-${i}`;

        // 10-digit unique phone number
        const phone =
            `9${runId}${String(i).padStart(3, '0')}`;

        const email =
            `playwright${runId}_${i}@test.com`;

        const address =
            `Pune Test ${runId}-${i}`;

        await page
            .getByPlaceholder('Name')
            .fill(name);

        await page
            .getByPlaceholder('Phone Number')
            .fill(phone);

        await page
            .getByPlaceholder('Email')
            .fill(email);

        await page
            .getByPlaceholder('Address')
            .fill(address);

        // Wait for POST request
        const responsePromise = page.waitForResponse(
            response =>
                response.url().includes('/api/contacts') &&
                response.request().method() === 'POST'
        );

        await page
            .getByRole('button', { name: 'Add Contact' })
            .click();

        const response = await responsePromise;

        expect(response.status()).toBe(200);

        console.log(
            `Added contact ${i}/200`
        );
    }

    console.log(
        `Successfully added 200 contacts - Run ID: ${runId}`
    );
});