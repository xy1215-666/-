const API_BASE = import.meta.env.VITE_API_BASE_URL || ''

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  })
  if (!response.ok) {
    const body = await response.json().catch(() => ({}))
    throw new Error(body.message || `请求失败：${response.status}`)
  }
  return response.json()
}

export const api = {
  foods: () => request('/api/foods'),
  orders: () => request('/api/orders'),
  updateStock: (id, stock) => request(`/api/admin/foods/${id}/stock?stock=${stock}`, { method: 'PUT' })
}
