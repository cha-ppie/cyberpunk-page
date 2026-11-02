// 让 Vite 在构建阶段收集 images 目录中的资源，并返回最终可访问的 URL。
const images = import.meta.glob('../../images/**/*', {
  eager: true,
  import: 'default',
  query: '?url'
})

export function imageUrl(path) {
  // 页面只需传入相对于 images 的路径，不必关心构建后的文件名哈希。
  return images[`../../images/${path}`]
}
