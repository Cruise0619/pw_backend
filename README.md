# 电竞陪玩点单小程序后端服务
测试github push
## 📋 项目简介

基于 Spring Boot + MongoDB 的电竞陪玩点单小程序后端服务，提供完整的 RESTful API 接口和可视化管理后台。
222
## 🚀 快速开始

### 环境要求

- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ MongoDB 4.0+

### 一键启动

```bash
启动后端.bat
```

启动脚本会自动：
1. 设置 JAVA_HOME 环境变量
2. 编译并打包项目
3. 启动 Spring Boot 应用
4. 在 http://localhost:8081 运行

启动成功后，访问以下地址：
- **API文档**: http://localhost:8081/
- **管理后台**: http://localhost:8081/admin.html

## 📊 项目结构

```
d:\pw_backend\
├── 启动后端.bat          # 一键启动脚本 ⭐
├── 测试指南.md             # API 测试指南
├── 优化后的项目结构.md  # 项目说明
├── database\               # 测试数据
└── pw_backend\             # 后端源代码
    ├── src\main\java\com\pwbackend\
    │   ├── entity\      # 实体类（8 个）
    │   ├── controller\   # 控制器（9 个）
    │   ├── service\      # 服务层（9 个）
    │   ├── repository\   # 数据访问层（8 个）
    │   └── config\       # 配置类
    ├── pom.xml             # Maven 配置
    └── application.properties  # 应用配置
```

## 🌐 API 接口

### 基础信息
- **基础地址**: `http://localhost:8081`
- **API 前缀**: `/api`
- **完整地址**: `http://localhost:8081/api`
- **管理后台**: `http://localhost:8081/admin.html`

### 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/login` | 用户登录/注册 |
| GET | `/api/user/{openid}` | 获取用户信息 |
| PUT | `/api/user/{openid}` | 更新用户信息 |
| GET | `/api/user/{openid}/isOps` | 检查是否为客服 |

### 商品接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/items` | 获取所有商品 |
| GET | `/api/items/{id}` | 获取商品详情 |
| GET | `/api/items/game/{game}` | 按游戏获取商品 |
| GET | `/api/items/random` | 获取随机商品 |
| GET | `/api/items/game/{game}/random` | 按游戏随机获取商品 |

### 订单接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/orders` | 获取所有订单 |
| GET | `/api/orders/{id}` | 获取订单详情 |
| GET | `/api/orders/user/{openid}` | 获取用户订单 |
| GET | `/api/orders/status/{status}` | 按状态获取订单 |
| POST | `/api/orders` | 创建订单 |
| PUT | `/api/orders/{id}/status` | 更新订单状态 |
| DELETE | `/api/orders/{id}` | 删除订单 |

### 玩家接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/players` | 获取所有玩家 |
| GET | `/api/players/{id}` | 获取玩家详情 |
| GET | `/api/players/game/{gameId}` | 按游戏获取玩家 |
| GET | `/api/players/approved` | 获取已认证玩家 |
| GET | `/api/players/approved/{gameId}` | 按游戏获取已认证玩家 |

### 评分接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/scores` | 获取所有评分 |
| GET | `/api/scores/{id}` | 获取评分详情 |
| GET | `/api/scores/player/{playerId}` | 获取玩家评分 |
| GET | `/api/scores/order/{orderId}` | 获取订单评分 |
| POST | `/api/scores` | 提交评分 |
| DELETE | `/api/scores/{id}` | 删除评分 |

### 分类接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/categories` | 获取所有分类 |
| GET | `/api/categories/game/{gameId}` | 按游戏获取分类 |

### 折扣接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/discounts` | 获取所有折扣 |
| GET | `/api/discounts/active` | 获取当前折扣 |
| GET | `/api/discounts/{id}` | 获取折扣详情 |
| POST | `/api/discounts` | 创建折扣 |
| PUT | `/api/discounts/{id}` | 更新折扣 |
| DELETE | `/api/discounts/{id}` | 删除折扣 |

### 数据导入接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/import/all` | 导入所有数据 |
| POST | `/api/import/collections` | 指定导入的集合 |

## 💾 数据库

### MongoDB 集合

| 集合名 | 说明 | Java 实体类 |
|---------|------|-----------|
| users | 用户信息 | User |
| items | 商品/服务 | Item |
| orders | 订单 | Order |
| mockPlayers | 陪玩玩家 | MockPlayer |
| score_rec | 评分记录 | ScoreRecord |
| categorylist | 分类列表 | CategoryList |
| disconts | 折扣活动 | Discount |
| ops | 客服账号 | Ops |

### 配置

```properties
# 服务器端口
server.port=8080

# MongoDB 配置
spring.data.mongodb.uri=mongodb://localhost:27017/pw_backend
spring.data.mongodb.database=pw_backend
```

## 🔧 开发指南

### 启动服务

```bash
# 方式一：一键启动（推荐）
启动后端.bat

# 方式二：手动启动
cd d:\pw_backend\pw_backend
set JAVA_HOME=C:\Program Files\Java\jdk-17
mvn spring-boot:run
```

### 导入测试数据

```bash
# 方式一：通过 API 导入
curl -X POST http://localhost:8080/api/import/all \
  -H "Content-Type: application/json"

# 方式二：使用 MongoDB 命令
mongoimport --db pw_backend --collection users --file ../database/database_users.json --jsonArray
```

### 测试 API

```bash
# 使用浏览器
http://localhost:8080/api/items

# 使用 curl
curl http://localhost:8080/api/items

# 使用 Postman
导入接口到 Postman 进行测试
```

## 📝 代码规范

### 命名规范
- 包名：`com.pwbackend`
- 实体类：使用 `@Document` 注解
- 控制器：使用 `@RestController` 注解
- 服务层：使用 `@Service` 注解
- 数据访问层：使用 `@Repository` 注解

### 注释规范
- 类和方法添加 `/** */` 注释
- 复杂逻辑添加行内注释
- 保持注释简洁明了

### 代码风格
- 使用 Lombok 简化代码
- 使用 Spring Boot 自动配置
- 遵循 RESTful API 设计规范

## 🛠️ 故障排除

### 常见问题

#### 1. 端口被占用

**错误信息**：
```
Port 8080 is already in use
```

**解决方案**：
```bash
# Windows
netstat -ano | findstr "8080"

# 查找占用进程并关闭
taskkill /pid <进程ID>
```

#### 2. MongoDB 连接失败

**错误信息**：
```
Failed to connect to MongoDB
```

**解决方案**：
```bash
# 启动 MongoDB 服务
net start MongoDB

# 检查服务状态
sc query MongoDB
```

#### 3. Java 版本不兼容

**错误信息**：
```
Unsupported class file major version 55
```

**解决方案**：
```bash
# 安装 Java 17
https://www.oracle.com/java/technologies/downloads/
```

#### 4. Maven 依赖下载慢

**解决方案**：
```bash
# 配置国内镜像（已配置）
# 增加 Maven 内存
set MAVEN_OPTS=-Xmx1024m
```

## 📈 性能优化

### JVM 参数

```bash
# 启动时设置 JVM 参数
java -Xmx1024m -Xms512m -jar pw_backend-1.0.0.jar
```

### MongoDB 优化

```bash
# 创建索引
db.orders.createIndex({"openid": 1})
db.orders.createIndex({"createTime": -1})
db.users.createIndex({"openid": 1})
```

## 🔒 安全建议

1. **配置 CORS**：已配置允许跨域访问
2. **输入验证**：使用 Spring Validation
3. **异常处理**：统一异常处理机制
4. **日志记录**：重要操作日志记录
5. **数据验证**：API 参数验证

## 📞 技术栈

- **后端框架**: Spring Boot 2.6.15
- **数据库**: MongoDB 4.0+
- **开发语言**: Java 17
- **构建工具**: Maven 3.6+
- **ORM**: Spring Data MongoDB
- **日志框架**: SLF4J（Spring Boot 默认）

## 🎯 项目特色

- ✅ 完整的 RESTful API
- ✅ 清晰的分层架构
- ✅ MongoDB 文档存储
- ✅ 跨域支持（CORS）
- ✅ 异常统一处理
- ✅ 完善的代码注释
- ✅ 一键启动脚本

## 📞 支持的游戏

- CS2（反恐精英 2）
- VAL（无畏契约）
- DELTA（三角洲行动）
- LOL（英雄联盟）

## 📝 更新日志

### 版本 1.0.0（2026-04-12）

- ✅ 初始化项目
- ✅ 完成核心功能
- ✅ 优化项目结构
- ✅ 创建启动脚本
- ✅ 添加 API 文档

---

**开始使用：运行 `启动后端.bat` 启动应用！** 🚀
