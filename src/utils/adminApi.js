let csrfToken
const base = import.meta.env.VITE_API_BASE || ''

async function request(path, options = {}) {
  const controller = new AbortController()
  const timer = setTimeout(() => controller.abort(), 15000)
  try {
    const response = await fetch(`${base}/api${path}`, {
      ...options, credentials: 'same-origin', signal: controller.signal,
      headers: { Accept: 'application/json', ...options.headers }
    })
    if (response.status === 204) return null
    const data = await response.json().catch(() => null)
    if (!response.ok) {
      const error = new Error(data?.detail || '后台服务暂时不可用，请稍后重试。')
      error.status = response.status
      throw error
    }
    if (data === null) throw new Error('后台服务未返回有效数据，请检查服务状态。')
    return data
  } catch (error) {
    if (error.name === 'AbortError') throw new Error('请求超时，请刷新列表确认操作结果。')
    if (error instanceof TypeError) throw new Error('无法连接后台服务，请检查连接后重试。')
    throw error
  } finally { clearTimeout(timer) }
}
async function mutate(path, method, data, form = false) {
  csrfToken ||= await request('/auth/csrf')
  return request(path, {
    method,
    headers: { [csrfToken.headerName]: csrfToken.token, ...(data ? { 'Content-Type': form ? 'application/x-www-form-urlencoded' : 'application/json' } : {}) },
    body: data ? (form ? new URLSearchParams(data) : JSON.stringify(data)) : undefined
  }).catch(error => { if (error.status === 403) csrfToken = null; throw error })
}
export const adminApi = {
  me: () => request('/auth/me'),
  list: resource => request(`/admin/${resource}`),
  async login(username, password) {
    csrfToken = null
    await mutate('/auth/login', 'POST', { username, password }, true)
    csrfToken = null
    return request('/auth/me')
  },
  async logout() {
    await mutate('/auth/logout', 'POST')
    csrfToken = null
  },
  save: (resource, id, data) => mutate(`/admin/${resource}${id ? '/' + encodeURIComponent(id) : ''}`, id ? 'PUT' : 'POST', data),
  remove: (resource, id) => mutate(`/admin/${resource}/${encodeURIComponent(id)}`, 'DELETE')
}
