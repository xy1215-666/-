import { test, expect } from '@playwright/test'

test('管理员可以查看今日概览', async ({ page }) => {
  await page.goto('/')
  await expect(page.getByRole('heading', { name: '今日概览' })).toBeVisible()
  await expect(page.getByText('今日订单', { exact: true })).toBeVisible()
  await expect(page.getByRole('heading', { name: '今日订单趋势' })).toBeVisible()
})

test('管理员可以进入菜单与库存', async ({ page }) => {
  await page.goto('/')
  await page.getByRole('button', { name: '菜单与库存' }).click()
  await expect(page.getByRole('heading', { name: '菜单与库存' }).last()).toBeVisible()
  await expect(page.getByText('照烧鸡腿饭')).toBeVisible()
})

test('管理员可以查看订单处理页', async ({ page }) => {
  await page.goto('/')
  await page.getByRole('button', { name: '订单处理' }).click()
  await expect(page.getByRole('heading', { name: '订单处理' }).last()).toBeVisible()
  await expect(page.getByText('CF07281201A8')).toBeVisible()
})
