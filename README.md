# 权限管理系统

基于 Spring Boot 3 和 Vue 2 的前后端分离权限管理系统，包含用户、角色、菜单、部门、岗位以及日志管理。

## 技术栈

- 后端：Java 17、Spring Boot 3.2、MyBatis-Plus、MySQL、Redis、JWT、Knife4j
- 前端：Vue 2、Vue Router、Vuex、Element UI、Axios
- 构建：Maven、Vue CLI

## 功能

- 用户管理：分页查询、新增、修改、删除、状态切换、角色分配
- 角色管理：分页查询、新增、修改、删除、批量删除、菜单权限分配
- 菜单管理：树形查询、新增、修改、删除
- 部门管理：树形查询、新增、修改、删除、状态切换
- 岗位管理：分页查询、新增、修改、删除、状态切换
- 登录日志：分页查询、时间筛选、详情
- 操作日志：分页查询、时间筛选、详情
- 登录认证：JWT 令牌登录、用户信息查询、退出登录

## 项目结构

```text
heli-auth-parent
├── common
│   ├── common-util
│   └── service-util
├── model
├── service-system
├── vs projet
│   └── heli-auth-ui
├── pom.xml
└── README.md
```

## 环境要求

- JDK 17
- Maven 3.8+
- Node.js 18+
- MySQL 8
- Redis 6+

## 后端配置

数据库配置位于：

```text
service-system/src/main/resources/application-dev.yml
```

复制示例配置并填写本机数据库信息：

```text
service-system/src/main/resources/application-dev.example.yml
```

后端默认端口为 `8080`。

## 前端配置

开发环境使用 `/dev-api` 作为接口前缀，开发服务器将请求代理到后端。

```text
vs projet/heli-auth-ui/.env.development
vs projet/heli-auth-ui/vue.config.js
```

前端开发服务器默认端口为 `9528`。

## 启动后端

```bash
mvn -DskipTests package
java -jar service-system/target/service-system-1.0.jar
```

接口文档：

```text
http://localhost:8080/doc.html
```

## 启动前端

```bash
cd "vs projet/heli-auth-ui"
npm install
npm run dev
```

前端地址：

```text
http://localhost:9528
```

## 默认账号

```text
用户名：admin
密码：111111
```

## 生产构建

```bash
cd "vs projet/heli-auth-ui"
npm run build:prod
```

构建产物位于：

```text
vs projet/heli-auth-ui/dist
```
