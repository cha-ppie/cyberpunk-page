# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Stack

Vue 3 + Vue Router + Vite; Spring Boot 4.1 + Spring Security + JDBC + Flyway; H2 / MySQL; local assets and CSS

## Users

以中文为主、对《赛博朋克 2077》及其衍生内容感兴趣的玩家与观众。他们会在桌面或移动浏览器中快速了解世界观、游戏系统、构筑攻略、边缘行者和图片内容。

## Product Purpose

Cyberpunk 是一个面向中文读者的《赛博朋克 2077》粉丝向资料站，帮助访客在进入夜之城前建立世界观，在游玩过程中查阅系统与流派，在游玩后浏览角色与场景档案。成功标准是六个内容页之间形成清晰、可浏览的入口关系，并让访客在首屏内理解站点主题与下一步动作。

## Positioning

它将游戏、动画与截图档案整理在同一个中文导航框架中，用接近官方 cyberpunk.net 的粗野主义赛博朋克视觉语言呈现，而不是普通的霓虹科技主题页。

## Operating Context

这是通过 Vite 本地预览、构建后静态托管的网站，资源来自项目本地目录；第二部角色图来自作品官网。访客通过固定导航和首屏行动按钮进入六个页面，在桌面端浏览宽幅视觉内容，在移动端以单列阅读和折叠菜单为主。

## Capabilities and Constraints

- 页面路由：首页、world、game、guide、edgerunners、gallery。
- 使用 Vue 3 单文件组件、Vue Router 与 Vite；不依赖 CDN。
- 保留本地 Rajdhani 与 Tomorrow 字体、角色图像、城市截图与页面互链。
- 支持现代桌面和移动浏览器，主要断点为 760px 与 1100px；尊重 `prefers-reduced-motion`。
- 允许重制信息架构与文案，但不应添加真实商业、销量或官方背书等未被项目事实确认的声明。

## Brand Commitments

- 名称：Cyberpunk；内容主题：Cyberpunk 2077 / Night City。
- 用户明确要求整体布局、风格和内容贴近官方网站，允许重制项目内全部内容。
- 项目需求文档将近黑、霓虹黄/青信号色、硬切角、宽字距、全屏首屏、克制动效和中文信息站列为长期方向。
- 这是粉丝向整理，非 CD PROJEKT RED 官方站点。

## Evidence on Hand

- 现有内容页与文案：`index.html`、`world.html`、`game.html`、`guide.html`、`edgerunners.html`、`gallery.html`。
- 本地图片资源：`images/city-photos/`、`images/characters/`。
- 本地字体资源：`fonts/rajdhani-*.woff2`、`fonts/tomorrow-*.woff2`。
- 需求文档：`赛博朋克2077官网风格静态网页需求文档.docx`。
- 已实现城区、攻略、画廊的数据库查询接口，支持静态模式和接口模式；后端启动说明见 `backend/README.md`。
- 已提供管理员登录及资料编辑后台（`/admin`），支持已有图片关联；访客账号、收藏、评论和搜索服务尚未提供，不应暗示已存在。

## Product Principles

1. 先让访客理解世界，再让访客选择入口。
2. 信息密度服务于浏览，不以装饰压过内容。
3. 动效表现城市持续运转，而不是抢夺注意力。
4. 官方风格是布局和设计语言参考，不制造官方授权暗示。

## Accessibility & Inclusion

保证可读对比度、可见键盘焦点、语义化标题与替代文本；在 `prefers-reduced-motion: reduce` 下关闭打字机、视差与背景运动。
