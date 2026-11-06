<script setup>
import { watch } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'

const route = useRoute()
// 所有页面使用可直接阅读的内容，不再启动循环背景粒子或隐藏式入场动画。
watch(() => route.fullPath, () => {
  document.body.dataset.theme = route.meta.theme || 'home'
  document.title = route.meta.title || 'Cyberpunk / 夜之城资料站'
}, { immediate: true })
</script>

<template>
  <AppHeader v-if="!route.meta.admin" />
  <RouterView />
  <AppFooter v-if="!route.meta.admin" />
</template>
