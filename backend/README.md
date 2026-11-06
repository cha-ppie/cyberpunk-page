# 夜之城后端

按根目录 `ROADMAP.md` 的第二、三阶段实现：Spring Boot、MySQL 配置、公开资料查询、前端 `/api` 代理，以及第四阶段的管理员内容后台。

## 本地启动（项目根目录）

要求 Java 17+、本机 Maven 和已有 Maven 缓存。所有 npm 后端脚本均为离线命令，不安装 Maven，不下载依赖，也不修改全局配置。

```powershell
npm run backend
```

默认使用 `local` 配置，监听 `127.0.0.1:8080`，数据保存在 `backend/data/night-city.mv.db`，重启后保留。无需安装数据库即可联调。另开终端：

```powershell
npm run dev:api
```

访问终端输出的前端地址。Vite 将 `/api` 转发至 `http://127.0.0.1:8080`。`npm run dev` 和 `npm run build` 仍为静态模式；`npm run build:api` 生成启用接口的前端产物。

## MySQL

已在本机 MySQL 5.7.44 实例中完成建表、初始化、接口与重启验证，详见 [MYSQL_VERIFICATION.md](MYSQL_VERIFICATION.md)。驱动固定为本机已缓存的 Connector/J 8.0.33。

首次部署时在已有 MySQL 实例中创建空数据库（不自动创建数据库）：

```sql
CREATE DATABASE night_city CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

在项目根目录的 PowerShell 中设置当前会话变量：

```powershell
$env:SPRING_PROFILES_ACTIVE = 'mysql'
$env:DB_URL = 'jdbc:mysql://127.0.0.1:3306/night_city?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai'
$env:DB_USERNAME = '你的数据库用户名'
$env:DB_PASSWORD = '你的数据库密码'
npm run backend
```

此账号需具备建表、索引和读写权限，以运行数据库迁移。连接信息不写进源码。退出后按需移除这些会话变量，恢复 `local` 默认配置。已验证本机 MySQL 5.7.44；自动测试默认使用 H2 的 MySQL 兼容模式，也可通过下述变量连接专用 MySQL 测试库。

## 管理后台

后台入口为前端 `#/admin`，接口模式下页脚也提供“内容管理”入口。支持世界观、攻略和画廊的新增、编辑、草稿保存、发布、下线及删除，图片从项目素材库选择。

首次创建管理员，在启动后端前设置当前 PowerShell 会话变量：

```powershell
$env:ADMIN_USERNAME = '你的管理员用户名'
$env:ADMIN_PASSWORD = '你设置的随机密码'
npm run backend
```

用户名需为 3–64 位字母、数字、下划线或短横线；密码至少 12 个字符且不超过 72 个 UTF-8 字节。账号首次启动时写入当前数据库，只保存 BCrypt 12 轮摘要；账号已存在时不会覆盖密码。账号创建后即可移除 `ADMIN_*` 环境变量，后续启动从数据库验证凭据。未配置初始账号时，公开资料接口正常工作，后台没有默认密码。

前后端通过同源会话登录，浏览器 Cookie 为 HttpOnly、SameSite=Lax，30 分钟无活动后过期；登录时更新会话标识，退出时销毁会话。登录与全部写入操作均要求 CSRF 令牌；登录成功后需重新获取令牌。一个来源地址连续登录失败 5 次后，限制继续登录 5 分钟；记录位于当前服务进程内，重启后清空。

生产环境应配置 HTTPS，并设置 `SESSION_COOKIE_SECURE=true`；代理必须保留同源 Cookie。使用多个后端实例时还需共享会话与登录限流状态。当前实现没有密码找回、账号管理页面或访客注册功能。

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| GET | `/api/auth/csrf` | 获取安全令牌及其请求头名称 |
| POST | `/api/auth/login` | 表单格式 `username` / `password` 登录，成功 204 |
| GET | `/api/auth/me` | 当前登录账号 |
| POST | `/api/auth/logout` | 退出并销毁会话，成功 204 |
| GET | `/api/admin/articles`、`/api/admin/gallery` | 全部资料，包括草稿 |
| GET | `/api/admin/assets` | 可关联的本地图片目录 |
| POST | `/api/admin/articles`、`/api/admin/gallery` | 新建资料，成功 201 |
| PUT | `/api/admin/articles/{id}`、`/api/admin/gallery/{id}` | 保存资料与发布状态 |
| DELETE | `/api/admin/articles/{id}`、`/api/admin/gallery/{id}` | 永久删除，成功 204 |

后台接口仅允许 ADMIN 角色访问。文章请求的 `content` 是结构化字段：世界观需 `english`、`label`、`scene`；攻略需 `english`、`approach`、`attributes`、`equipment`、`steps`、`mixing`、`alt`。标题和简介使用 `title`、`description`，图片使用 `image`，排序使用 `sortOrder`，发布状态使用 `published`。创建后的 `id` 不可更改；非法请求返回 400，未登录 401，无权限或令牌失效 403，重复标识 409，登录限流 429。

## 公开接口

列表直接返回 JSON 数组，详情返回 JSON 对象；不存在或未发布的详情返回 404 和 `application/problem+json`。筛选无结果返回 `[]`。

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| GET | `/api/health` | 应用和数据库健康状态 |
| GET | `/api/categories` | 世界观和攻略栏目 |
| GET | `/api/articles?category=world` | 城区资料 |
| GET | `/api/articles?category=guide` | 攻略流派 |
| GET | `/api/articles` | 全部已发布文章 |
| GET | `/api/articles/{id}` | 单篇资料，例如 `netrunner`、`watson` |
| GET | `/api/gallery` | 画廊，支持 `?category=人物`、`城市`、`装备` |
| GET | `/api/gallery/{id}` | 单幅画面，例如 `gallery-01` |

文章保留前端需要的结构字段（如 `steps`、`equipment`），并提供 `title`、`category`。所有图片字段 `image` 都是相对于根目录 `images/` 的路径，由 Vite 解析为最终资源地址。新增图片需放入该目录，执行 `npm run assets:sync` 同步素材目录，并重新构建前后端。当前提供已有素材关联，不包含运行时文件上传。

## 数据维护

- Flyway 的 `db/migration/V1__content_schema.sql` 创建 `categories`、`articles`、`gallery_items`；后续表结构变更新增迁移文件，不修改已执行的迁移。
- `content/*.json` 是首批数据和静态模式的共同来源：7 个城区、6 个流派、18 张图片。
- 首次启动在事务中初始化空数据库；栏目已存在时跳过，重启不会覆盖修改或恢复删除的数据。
- `articles.title`、`summary`、`image_path` 为基础字段，`content` 保存其余结构化 JSON；`gallery_items` 使用独立字段。
- `published = FALSE` 的文章和图片不对外返回，`sort_order` 控制展示顺序。
- 静态资料仅在首次初始化时导入，后续修改 JSON 不会自动覆盖数据库。
- 管理员登录和内容后台已完成；标签、运行时图片上传、访客账号、收藏和评论属于后续阶段。

## 验证与打包

```powershell
npm run test:backend
npm run build:backend
```

默认测试使用隔离的内存数据库，13 项集成测试覆盖查询、分类筛选、初始化幂等、登录、会话与 CSRF、安全校验、草稿与发布、编辑和删除；同一套测试也在真实 MySQL 5.7 上验证。打包产物为 `backend/target/night-city-api-1.0.0.jar`。从 `backend` 目录启动该 jar，可保持本地数据库路径一致。

如需在专用 MySQL 测试库重复执行相同的 13 项集成测试，先创建空数据库，并设置：

```powershell
$env:TEST_DB_URL = 'jdbc:mysql://127.0.0.1:3306/night_city_test?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai'
$env:TEST_DB_USERNAME = '你的数据库用户名'
$env:TEST_DB_PASSWORD = '你的数据库密码'
npm run test:backend
```

测试数据在首次启动时导入，测试中的编辑与删除会事务回滚；测试依赖首批资料的固定数量，适合独立测试库。执行结束后移除上述 `TEST_DB_*` 会话变量，即恢复默认 H2 测试。

`maven-offline.xml` 只影响显式引用它的项目命令：强制离线，并兼容本机缓存中的 `central` / `aliyunmaven` 来源标记。为与本机完整缓存的安全模块保持一致，应用、启动和打包插件统一为 Spring Boot 4.1.0，使用 Spring Security 7.1、Jackson 3，保留已验证的 Flyway 11.7.2、H2 2.3.232 和 MySQL 驱动 8.0.33。Java 17 下完成离线编译、测试和打包。数据库迁移由 `DatabaseConfiguration` 显式注册，版本记录沿用原有 Flyway 表。

## 部署

前端与 API 使用同一域名，将 `/api` 反向代理到后端。开发代理不会进入生产构建。后端默认仅监听本机，确需其他主机访问时通过 `SERVER_ADDRESS` 设置监听地址；端口可通过 `SERVER_PORT` 设置。GitHub Pages 仅能托管静态前端，不能运行 Java 后端。

接口模式遇到网络失败或无效响应时使用本地资料，并在控制台记录提示；合法的空数组显示空状态。由于保留本地资料作为回退，这些数据不适合存放需要权限保护的内容。
