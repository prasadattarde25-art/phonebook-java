const { test } = require('@playwright/test');

test.setTimeout(120000);

test('Add 200 contacts', async ({ page }) => {

    await page.goto('http://localhost:5173');

    await page.getByPlaceholder('Name').waitFor();

    for (let i = 1; i <= 200; i++) {

        const name = `Test User ${i}`;
        const phone = `900000${String(i).padStart(4, '0')}`;
        const email = `user${i}@test.com`;
        const address = `Pune ${i}`;

        await page.getByPlaceholder('Name').fill(name);
        await page.getByPlaceholder('Phone Number').fill(phone);
        await page.getByPlaceholder('Email').fill(email);
        await page.getByPlaceholder('Address').fill(address);

        await page.getByRole('button', { name: 'Add Contact' }).click();

        // Give API request time to complete
        await page.waitForTimeout(200);
    }

    console.log('Successfully added 200 contacts');
});