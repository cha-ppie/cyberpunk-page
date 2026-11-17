import { randomUUID } from 'node:crypto'
import { readFile, rename, rm, stat, writeFile } from 'node:fs/promises'
import { isContentList } from '../src/utils/contentSchema.js'

const contentDir = new URL('../content/', import.meta.url)
const imagesDir = new URL('../images/', import.meta.url)
const output = new URL('published.json', contentDir)
const temporary = new URL(`.published-${randomUUID()}.tmp`, contentDir)
const base = (process.env.PAGES_CONTENT_API_URL || 'http://127.0.0.1:8080').replace(/\/+$/, '')

async function readJson(file) {
  return JSON.parse(await readFile(new URL(file, contentDir), 'utf8'))
}

async function readPublicList(path) {
  const response = await fetch(`${base}/api/${path}`, {
    headers: { Accept: 'application/json' }, cache: 'no-store', signal: AbortSignal.timeout(15_000)
  })
  if (!response.ok) throw new Error(`${path} 返回 HTTP ${response.status}`)
  const items = await response.json()
  if (!Array.isArray(items) || items.some(item => item?.published !== true)) {
    throw new Error(`${path} 必须只返回已发布资料`)
  }
  return items
}

async function exportContent() {
  const [articles, gallery, districtExamples, buildExamples, galleryExamples, assets] = await Promise.all([
    readPublicList('articles'), readPublicList('gallery'), readJson('districts.json'), readJson('builds.json'),
    readJson('gallery.json'), readJson('assets.json')
  ])
  const sections = {
    districts: [articles.filter(item => item.category === 'world'), districtExamples[0]],
    builds: [articles.filter(item => item.category === 'guide'), buildExamples[0]],
    gallery: [gallery, galleryExamples[0]]
  }
  const snapshot = {}
  for (const [name, [items, example]] of Object.entries(sections)) {
    if (!isContentList(items, example)) throw new Error(`${name} 的字段或资料标识无效`)
    // 按现有页面的数据结构导出，排除后台字段。
    const keys = Object.keys(example)
    snapshot[name] = items.map(item => Object.fromEntries(keys.map(key => [key, item[key]])))
  }

  const assetPaths = new Set(assets)
  const selectedImages = new Set(Object.values(snapshot).flatMap(items => items.map(item => item.image)))
  await Promise.all([...selectedImages].map(async image => {
    if (!assetPaths.has(image)) throw new Error(`素材 ${image} 未同步到 content/assets.json`)
    const imageUrl = new URL(image.split('/').map(encodeURIComponent).join('/'), imagesDir)
    const file = await stat(imageUrl).catch(() => null)
    if (!file?.isFile()) throw new Error(`构建目录缺少素材 ${image}`)
  }))

  try {
    await writeFile(temporary, JSON.stringify(snapshot, null, 2) + '\n', { encoding: 'utf8', flag: 'wx' })
    await rename(temporary, output)
  } finally {
    await rm(temporary, { force: true })
  }
  const { districts, builds, gallery: photos } = snapshot
  console.log(`已导出 content/published.json（${districts.length} 个城区、${builds.length} 套流派、${photos.length} 张图片）。`)
}

try {
  await exportContent()
} catch (error) {
  console.error(`导出失败，已保留原有发布快照。${error.message}${error.cause ? `（${error.cause.message}）` : ''}`)
  process.exitCode = 1
}
