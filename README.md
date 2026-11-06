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

> ⚠️ 项目仅用于课程作业学习，素材版权归原作者所有，非商用。
