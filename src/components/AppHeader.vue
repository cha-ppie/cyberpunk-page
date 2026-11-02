<script setup>
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const menuOpen = ref(false)
const scrolled = ref(false)

// 桌面端和移动端共用同一份导航配置，避免两处菜单内容不一致。
const navigation = [
  { to: '/game', label: 'GAMES', subtitle: '游戏系统' },
  { to: '/world', label: 'WORLD', subtitle: '世界观' },
  { to: '/edgerunners', label: 'SHOWS', subtitle: '边缘行者' },
  { to: '/guide', label: 'COMMUNITY', subtitle: '攻略流派' },
  { to: '/gallery', label: 'ARCHIVE', subtitle: '画廊' }
]

function updateHeader() { scrolled.value = window.scrollY > 64 }
function closeMenu() { menuOpen.value = false }
function handleKeydown(event) { if (event.key === 'Escape' && menuOpen.value) closeMenu() }

// 进入新页面后自动收起移动端菜单。
watch(() => route.fullPath, closeMenu)
onMounted(() => {
  // 根据滚动位置切换页头背景，同时支持 Esc 键关闭菜单。
  updateHeader()
  window.addEventListener('scroll', updateHeader, { passive: true })
  document.addEventListener('keydown', handleKeydown)
})
onBeforeUnmount(() => {
  // 组件卸载时移除全局监听，防止重复注册。
  window.removeEventListener('scroll', updateHeader)
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<template>
  <!-- 桌面端主导航。 -->
  <header class="site-header" :class="{ scrolled }">
    <div class="header-inner">
      <RouterLink class="brand" to="/" aria-label="Syberpunk 首页"><span class="brand-mark">//</span><span>SYBERPUNK</span><span class="brand-status" aria-hidden="true"></span></RouterLink>
      <nav class="site-nav" aria-label="主导航">
        <RouterLink v-for="item in navigation" :key="item.to" class="nav-link" :to="item.to" active-class="active">{{ item.label }} <small>{{ item.subtitle }}</small></RouterLink>
      </nav>
      <button class="nav-toggle" :class="{ 'is-open': menuOpen }" type="button" :aria-expanded="menuOpen" aria-controls="mobile-panel" @click="menuOpen = !menuOpen"><span class="nav-toggle-bars" aria-hidden="true"><span></span><span></span><span></span></span><span>菜单</span></button>
    </div>
  </header>
  <!-- 小屏幕导航面板；inert 会阻止隐藏状态下的元素获得焦点。 -->
  <aside id="mobile-panel" class="mobile-panel" :class="{ 'is-open': menuOpen }" aria-label="移动端导航" :aria-hidden="!menuOpen" :inert="!menuOpen">
    <nav class="mobile-nav">
      <RouterLink to="/" active-class="active" @click="closeMenu">HOME <span>首页</span></RouterLink>
      <RouterLink v-for="item in navigation" :key="item.to" :to="item.to" active-class="active" @click="closeMenu">{{ item.label }} <span>{{ item.subtitle }}</span></RouterLink>
    </nav>
  </aside>
</template>
