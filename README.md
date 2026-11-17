# CYBERPUNK NIGHTCITY WEB

> 夜之城 · Vue 3 + Spring Boot 资料站

> 「The city of dreams.」

## 项目简介

以 Vue 3 重构的赛博朋克主题资料站，包含夜之城世界观、游戏系统、攻略流派、边缘行者专题与视觉档案。

## 技术栈

- Vue 3（Composition API）
- Vue Router（Hash 路由，适配静态托管）
- Vite
- Spring Boot、JDBC、Flyway；本地 H2 / MySQL
- 原有 CSS、字体与本地图片资源

## 目录说明

- `src/views/`：六个页面视图
- `src/components/`：公共页头、页脚和图标
- `src/composables/`：滚动显现、首屏视差与背景粒子动效
- `backend/`：后端接口、数据库迁移与测试
- `content/`：首批资料和静态回退数据
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

城区和攻略切换时会更新网址，可直接分享具体资料，例如 `#/world?district=watson` 和 `#/guide?build=netrunner`。

## 前后端联调

使用本机已有 Java 17、Maven 和缓存依赖，在两个终端中分别运行：

```powershell
npm run backend
```

```powershell
npm run dev:api
```

后端默认使用本地持久化数据库，健康接口为 `http://127.0.0.1:8080/api/health`。画廊、城区和攻略通过数据库接口读取，页面外观保持原有设计。

`npm run test:backend` 执行后端集成测试，`npm run build:api` 构建接口模式前端，`npm run build:backend` 打包后端。后端命令强制离线，不额外下载工具或依赖。

内容管理入口为 `#/admin`，支持管理员登录、资料编辑、发布与下线。首次账号通过 `ADMIN_USERNAME` / `ADMIN_PASSWORD` 环境变量创建，配置说明见后端文档。

MySQL 连接、接口及数据维护见 [backend/README.md](backend/README.md)，后续阶段见 [ROADMAP.md](ROADMAP.md)。

## 发布到 GitHub Pages

后台发布、下线和删除会立即改变数据库公开接口。GitHub Pages 要在重新导出并部署后才会更新。静态构建优先使用 `content/published.json`，仓库尚无快照时使用内置资料。

- **后端在本机**。保持 `npm run backend` 运行，另开终端执行 `npm run content:export`。检查生成的 `content/published.json` 后提交并推送到 `main`，GitHub Pages 会使用这份快照。每次后台发布或下线后重新执行导出。
- **后端已有可从 GitHub 访问的 HTTPS 地址**。在仓库 **Settings → Secrets and variables → Actions → Variables** 设置 `PAGES_CONTENT_API_URL`（填写后端根地址，如 `https://content.example.com`）。后台改动后，在 **Actions → Deploy Vue site to GitHub Pages → Run workflow** 手动部署，也可由推送到 `main` 触发。工作流会在构建前读取最新资料。

导出只使用公开 API，只接受已发布记录，并保留空栏目。接口失败、返回无效，或构建目录缺少资料引用的图片时，导出会失败并保留原有快照。通过本机导出发布时，新增素材须和快照一起提交。

## 画廊缩略图

仓库已包含现有画廊的 WebP 缩略图。GitHub Pages 会为导出的画廊生成缩略图；本机修改图片时，可用已安装 Pillow 的 Python 运行 `python scripts/generate-gallery-thumbnails.py`。接口模式可加 `--all-assets`，为后台可选的全部素材生成缩略图。画廊会在素材缺少缩略图时使用原图。

> ⚠️ 项目仅用于课程作业学习，素材版权归原作者所有，非商用。
