# CYBERPUNK NIGHTCITY WEB

> 夜之城 · Vue 3 前端展示站

> 「The city of dreams.」

## 项目简介

以 Vue 3 重构的赛博朋克主题资料站，包含夜之城世界观、游戏系统、攻略流派、边缘行者专题与视觉档案。

## 技术栈

- Vue 3（Composition API）
- Vue Router（Hash 路由，适配静态托管）
- Vite
- 原有 CSS、字体与本地图片资源

## 目录说明

- `src/views/`：六个页面视图
- `src/components/`：公共页头、页脚和图标
- `src/composables/`：滚动显现、首屏视差与背景粒子动效
- `images/`、`fonts/`、`css/`：保留的本地资源与视觉样式

## 本地运行

```powershell
npm install
npm run dev
```

生产构建：

```powershell
npm run build
```

构建产物位于 `dist/`。站内页面通过 Hash 路由访问，例如 `#/world`、`#/gallery`，可直接部署到 GitHub Pages 等静态托管服务。

后续的 Vue、Spring Boot 和数据库实施计划见 [ROADMAP.md](ROADMAP.md)。

> ⚠️ 项目仅用于课程作业学习，素材版权归原作者所有，非商用。
