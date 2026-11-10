import { onMounted, onBeforeUnmount, shallowRef } from 'vue'

// 静态托管继续使用随构建发布的内容；开发/服务部署可启用 API。
export function useContent(resource, staticItems, transform = items => items) {
  const apiEnabled = import.meta.env.VITE_API_ENABLED === 'true'
  const content = shallowRef(apiEnabled ? [] : transform(staticItems))
  const status = shallowRef(apiEnabled ? 'loading' : 'ready')
  const controller = new AbortController()
  let disposed = false
  onMounted(async () => {
    if (!apiEnabled) return
    const timeout = setTimeout(() => controller.abort(), 5000)
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE || ''}/api/${resource}`, {
        signal: controller.signal,
        cache: 'no-store',
        headers: { Accept: 'application/json' }
      })
      if (!response.ok) throw new Error(`HTTP ${response.status}`)
      const items = await response.json()
      const fields = Object.entries(staticItems[0] ?? {})
      if (!Array.isArray(items) || items.some(item => !item || fields.some(([key, value]) =>
        Array.isArray(value) ? !Array.isArray(item[key]) : typeof item[key] !== typeof value
      )) || new Set(items.map(item => item.id)).size !== items.length) {
        throw new Error('Invalid content response')
      }
      if (!disposed) {
        content.value = transform(items)
        status.value = 'ready'
      }
    } catch (error) {
      if (!disposed) {
        status.value = 'error'
        console.warn(`内容接口 ${resource} 不可用。`, error.message)
      }
    } finally {
      clearTimeout(timeout)
    }
  })
  onBeforeUnmount(() => { disposed = true; controller.abort() })
  return { content, status }
}
