// 页面位图交由 Vite 收集；撕边 SVG 由对应 CSS 单独引入。
const images = import.meta.glob('../../images/**/*.{jpg,jpeg,png,webp,avif}', {
  eager: true,
  import: 'default',
  query: '?url'
})

export function imageUrl(path) {
  // 页面只需传入相对于 images 的路径，不必关心构建后的文件名哈希。
  return images[`../../images/${path}`]
}
