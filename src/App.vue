<script setup>
import { nextTick, onBeforeUnmount, watch } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from './components/AppHeader.vue'
import AppFooter from './components/AppFooter.vue'
import { useVisualEffects } from './composables/useVisualEffects'

const route = useRoute()
const { refreshPageEffects, stopPageEffects, startAmbient, stopAmbient } = useVisualEffects()

// 背景粒子属于整个站点，只需在根组件创建时启动一次。
startAmbient()

// 路由切换后同步页面主题和标题，并等待新页面渲染完成后重新绑定视觉动效。
watch(
  () => route.fullPath,
  async () => {
    document.body.dataset.theme = route.meta.theme || 'home'
    document.title = route.meta.title || 'Syberpunk / 夜之城资料站'
    await nextTick()
    refreshPageEffects()
  },
  { immediate: true }
)

onBeforeUnmount(() => {
  // 释放观察器、动画帧和事件监听，避免热更新或卸载后重复运行。
  stopPageEffects()
  stopAmbient()
})
</script>

<template>
  <!-- 页头和页脚由所有路由共享，中间区域由当前路由组件填充。 -->
  <AppHeader />
  <RouterView />
  <AppFooter />
</template>
