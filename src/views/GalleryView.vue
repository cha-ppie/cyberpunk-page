<script setup>
import { nextTick, onBeforeUnmount, ref, watch } from 'vue'
import { imageUrl } from '../utils/assets'

// 每项依次为图片地址、无障碍替代文本和灯箱标题。
const entries = [
  [imageUrl('characters/V.jpg'), 'V 的近景画面', 'V / city lights'], [imageUrl('characters/Judy.jpg'), 'Judy的角色画面', 'Judy / after dark'], [imageUrl('characters/Johnny Silver Hand.png'), 'Johnny Silver Hand的角色画面', 'Johnny / never fade away'], [imageUrl('city-photos/20260202000029_1.jpg'), '夜之城高架与人物剪影', 'Night City / transit line'], [imageUrl('city-photos/20260202105333_1.jpg'), '武器陈列墙', 'Weapons / loaded'], [imageUrl('city-photos/photomode_21012026_202313.png'), '雨夜中的人物小队', 'The crew / rainy ground'], [imageUrl('characters/Song Zhaomei.jpg'), '宋昭美的角色画面', 'Talia / chrome and gold'], [imageUrl('characters/Oda.jpg'), '小田的角色画面', 'Oda / high security'], [imageUrl('characters/Jack.png'), 'Jack的角色画面', 'Jackie / street legend'], [imageUrl('places/Panam.png'), 'Panam的角色画面', 'Panam / desert road'], [imageUrl('characters/Rogue.jpg'), 'Rogue的角色画面', 'Rogue / afterlife'], [imageUrl('characters/gallery (3).jpg'), '夜之城霓虹雨景', 'Night City / neon rain'], [imageUrl('characters/Johnny.png'), '义体改造展示', 'Cyberware / chrome'], [imageUrl('characters/Mobile Unit.jpg'), '义体改造展示', 'Cyberware / chrome'], [imageUrl('places/20260130104420_1.jpg'), '夜之城霓虹雨景', 'Night City / neon rain'], [imageUrl('places/20260202120930_1.jpg'), '义体改造展示', 'Cyberware / chrome'], [imageUrl('city-photos/c6cd9e72bbb54dc7f120dee6cf59b3bb.jpg'), '义体改造展示', 'Cyberware / chrome'], [imageUrl('places/20260616222833_1.jpg'), '夜之城霓虹雨景', 'Night City / neon rain']
]
const selected = ref(null)
const closeButton = ref(null)

function closeLightbox() { selected.value = null }
function handleKeydown(event) { if (event.key === 'Escape') closeLightbox() }

// 灯箱打开后把焦点移到关闭按钮，方便键盘用户立即退出。
watch(selected, async (item) => { if (item) { await nextTick(); closeButton.value?.focus() } })
// 全局监听 Esc 关闭灯箱，并在组件卸载时清理监听。
onBeforeUnmount(() => document.removeEventListener('keydown', handleKeydown))
document.addEventListener('keydown', handleKeydown)
</script>

<template>
  <main class="page-shell">
    <section class="page-hero" aria-labelledby="page-title"><div class="page-hero-media" aria-hidden="true"><img src="/images/characters/gallery.jpg" alt=""></div><div class="page-hero-content"><h1 id="page-title" class="page-title glitch" data-text="NIGHT CITY">NIGHT CITY<span>视觉档案</span></h1><p class="page-lede">一座城市的记忆不会只留在地图上。它藏在脸、武器、车灯、雨水和每一块被霓虹擦亮的玻璃里。</p></div></section>
    <section class="section section-soft"><div class="container"><h2 class="section-title">档案已<span>上线</span></h2><p class="section-description">选择任意画面打开全尺寸查看器。图片来自项目本地截图资源，按人物、场景与装备重新编排。</p><div class="gallery-grid"><button v-for="entry in entries" :key="entry[0]" class="gallery-item reveal" type="button" :data-caption="entry[2]" @click="selected = entry"><img :src="entry[0]" :alt="entry[1]"></button></div></div></section>
  </main>
  <!-- 点击遮罩本身或按 Esc 可关闭灯箱，点击图片不会触发关闭。 -->
  <div v-if="selected" class="lightbox is-open" role="dialog" aria-modal="true" aria-label="图片查看器" @click.self="closeLightbox"><button ref="closeButton" class="lightbox-close" type="button" @click="closeLightbox">关闭 / CLOSE</button><img :src="selected[0]" :alt="selected[1]"><span class="lightbox-caption">{{ selected[2] }}</span></div>
</template>
