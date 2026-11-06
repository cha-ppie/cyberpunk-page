import { readdir, writeFile } from 'node:fs/promises'
import { fileURLToPath } from 'node:url'
import path from 'node:path'

const root = fileURLToPath(new URL('../images/', import.meta.url))
const assets = []
async function visit(directory) {
  for (const entry of await readdir(directory, { withFileTypes: true })) {
    const file = path.join(directory, entry.name)
    if (entry.isDirectory()) await visit(file)
    else if (entry.isFile() && /\.(jpe?g|png|webp|avif)$/i.test(entry.name)) assets.push(path.relative(root, file).split(path.sep).join('/'))
  }
}
await visit(root)
await writeFile(new URL('../content/assets.json', import.meta.url), JSON.stringify(assets.sort(), null, 2) + '\n', 'utf8')
console.log(`已同步 ${assets.length} 个图片素材。`)
