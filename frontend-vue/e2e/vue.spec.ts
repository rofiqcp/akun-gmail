import { test, expect } from '@playwright/test'

test('redirects unauthenticated users to login page', async ({ page }) => {
  await page.goto('/')
  await expect(page).toHaveURL(/\/login/)
  await expect(page.locator('h1')).toHaveText('AI Chatbot')
})

test('login page shows Google sign-in prompt', async ({ page }) => {
  await page.goto('/login')
  await expect(page.locator('h1')).toHaveText('AI Chatbot')
  await expect(page.locator('p').first()).toContainText('Google')
})

test('authenticated user can view dashboard', async ({ page }) => {
  await page.goto('/login')
  await page.evaluate(() => {
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify({ email: 'test@gmail.com', name: 'Test User' }))
  })
  await page.goto('/dashboard')
  await expect(page.locator('h2').first()).toContainText('AI Chatbot Dashboard')
})

test('authenticated user can view chatbot page', async ({ page }) => {
  await page.goto('/login')
  await page.evaluate(() => {
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify({ email: 'test@gmail.com', name: 'Test User' }))
  })
  await page.goto('/chatbot')
  await expect(page.locator('h2')).toContainText('AI Chatbot')
  await expect(page.locator('textarea')).toBeVisible()
})
