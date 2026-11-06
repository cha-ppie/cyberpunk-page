import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import GameView from '../views/GameView.vue'
import WorldView from '../views/WorldView.vue'
import EdgerunnersView from '../views/EdgerunnersView.vue'
import GuideView from '../views/GuideView.vue'
import GalleryView from '../views/GalleryView.vue'

// meta.theme 控制 body 的页面配色，meta.title 用于更新浏览器标签标题。
const routes = [
  { path: '/', name: 'home', component: HomeView, meta: { theme: 'home', title: 'Cyberpunk / 夜之城资料站' } },
  { path: '/game', name: 'game', component: GameView, meta: { theme: 'game', title: 'Games / 游戏系统 / Cyberpunk' } },
  { path: '/world', name: 'world', component: WorldView, meta: { theme: 'world', title: 'World / 夜之城世界观 / Cyberpunk' } },
  { path: '/edgerunners', name: 'edgerunners', component: EdgerunnersView, meta: { theme: 'edgerunners', title: 'Edgerunners / 边缘行者 / Cyberpunk' } },
  { path: '/guide', name: 'guide', component: GuideView, meta: { theme: 'guide', title: 'Community / 攻略流派 / Cyberpunk' } },
  { path: '/gallery', name: 'gallery', component: GalleryView, meta: { theme: 'gallery', title: 'Archive / 夜之城视觉档案 / Cyberpunk' } },
  { path: '/admin', name: 'admin', component: () => import('../views/AdminView.vue'), meta: { theme: 'admin', title: '内容管理 / Cyberpunk', admin: true } },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

export default createRouter({
  // Hash 路由无需服务器额外配置，适合 GitHub Pages 等静态托管环境。
  history: createWebHashHistory(),
  routes,
  // 页面切换默认回到顶部；带锚点时平滑滚动到指定内容区。
  scrollBehavior(to) {
    if (to.hash) {
      const headerHeight = Number.parseInt(getComputedStyle(document.documentElement).getPropertyValue('--header-height'), 10) || 72
      const behavior = window.matchMedia('(prefers-reduced-motion: reduce)').matches ? 'auto' : 'smooth'
      return { el: to.hash, top: headerHeight + 24, behavior }
    }
    return { top: 0, behavior: 'smooth' }
  }
})
