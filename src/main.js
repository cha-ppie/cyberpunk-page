import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '../css/cyberpunk.css'

// 创建根应用，注册全局路由，并挂载到 index.html 中的 #app 节点。
createApp(App).use(router).mount('#app')
