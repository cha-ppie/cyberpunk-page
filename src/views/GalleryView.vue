<script setup>
import { computed, nextTick, onBeforeUnmount, ref, watch } from 'vue'
import DossierEnd from '../components/DossierEnd.vue'
import ArrowIcon from '../components/ArrowIcon.vue'
import initialEntries from '../../content/gallery.json'
import { useContent } from '../composables/useContent'
import { imageUrl } from '../utils/assets'

const entries = useContent('gallery', initialEntries, items => items.map(({ image, ...item }) => {
  const { title } = item
  const [name, description] = title.split(' / ')
  const contextualPrefix = ['城市', '装备', '街头', '荒原', '义体'].includes(name)
  return {
    ...item, src: imageUrl(image),
    captionTitle: description && contextualPrefix ? description : name,
    captionPrefix: description && contextualPrefix ? name : '',
    captionSuffix: description && !contextualPrefix ? description : ''
  }
}))
const category = ref('全部')
const categories = ['全部','人物','城市','装备']
const visibleEntries = computed(() => entries.value.filter(item => category.value === '全部' || item.category === category.value))
const selectedIndex = ref(null)
const selected = computed(() => selectedIndex.value === null ? null : visibleEntries.value[selectedIndex.value])
const viewer = ref(null)
const closeButton = ref(null)
let trigger
let previousOverflow = ''
watch(entries, () => { if (selectedIndex.value !== null) closeImage() })

async function openImage(index,event) {
  trigger = event.currentTarget
  previousOverflow = document.body.style.overflow
  selectedIndex.value = index
  document.body.style.overflow = 'hidden'
  await nextTick()
  viewer.value?.showModal()
  closeButton.value?.focus()
}
function closeImage() {
  viewer.value?.close()
  selectedIndex.value = null
  document.body.style.overflow = previousOverflow
  nextTick(() => trigger?.focus())
}
function stepImage(direction) { selectedIndex.value = (selectedIndex.value + direction + visibleEntries.value.length) % visibleEntries.value.length }
function handleViewerKey(event) {
  if (event.key === 'ArrowLeft') { event.preventDefault(); stepImage(-1) }
  if (event.key === 'ArrowRight') { event.preventDefault(); stepImage(1) }
}
function filterCategory(value) { if (selected.value) closeImage(); category.value = value }
onBeforeUnmount(() => { if (selected.value) document.body.style.overflow = previousOverflow })
</script>

<template>
  <main class="page-shell dossier archive-page">
    <section class="archive-top" aria-labelledby="page-title"><div class="d-wrap archive-top-inner"><div><h1 id="page-title">CITY ARCHIVE</h1><h2>视觉档案</h2></div><p>脸孔、义体、街头的光。<br>把这座城市，重新看一遍。</p></div></section>
    <section class="d-wrap d-section"><div class="archive-meta"><div class="archive-filters" role="group" aria-label="筛选视觉档案"><button v-for="value in categories" :key="value" type="button" :aria-pressed="category === value" @click="filterCategory(value)">{{ value }}</button></div><p class="archive-count" aria-live="polite">{{ visibleEntries.length }} 幅画面 · 点击查看大图</p></div>
      <p v-if="!visibleEntries.length" role="status">该分类暂无已发布的图片。</p>
      <div class="archive-wall"><button v-for="(entry,index) in visibleEntries" :key="entry.src" class="archive-photo" type="button" :aria-label="'查看大图：' + entry.title" @click="openImage(index,$event)"><img :src="entry.src" :alt="entry.alt" :width="entry.width" :height="entry.height" loading="lazy"><span class="archive-caption"><span v-if="entry.captionPrefix" class="archive-caption-context">{{ entry.captionPrefix }} / </span><span class="archive-caption-title">{{ entry.captionTitle }}</span><span v-if="entry.captionSuffix" class="archive-caption-context"> / {{ entry.captionSuffix }}</span></span></button></div>
    </section>
    <DossierEnd title="照片之外，还有故事。" description="走近那些让夜之城被记住的人。" to="/edgerunners" action="浏览边缘行者" />
  </main>
  <Teleport to="body"><dialog v-if="selected" ref="viewer" class="archive-dialog" aria-label="图片查看器" aria-describedby="image-caption" @cancel.prevent="closeImage" @click.self="closeImage" @keydown="handleViewerKey"><button ref="closeButton" class="archive-close" type="button" @click="closeImage">关闭 ×</button><button class="archive-prev" type="button" aria-label="上一张" @click="stepImage(-1)"><ArrowIcon style="transform: rotate(180deg)" /></button><img :src="selected.src" :alt="selected.alt"><p id="image-caption">{{ selected.title }} · {{ selectedIndex + 1 }} / {{ visibleEntries.length }}</p><button class="archive-next" type="button" aria-label="下一张" @click="stepImage(1)"><ArrowIcon /></button></dialog></Teleport>
</template>
