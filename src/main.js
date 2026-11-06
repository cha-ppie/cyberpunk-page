import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '../css/cyberpunk.css'
import '../css/home-fracture.css'
import '../css/dossier.css'
import '../css/rupture.css'
import '../css/hero-compositions.css'
import '../css/build-explorer.css'

// 创建根应用，注册全局路由，并挂载到 index.html 中的 #app 节点。
createApp(App).use(router).mount('#app')
