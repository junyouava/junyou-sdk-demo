# JunYou SDK Backend

JunYou SDK 后端服务，提供 Go 和 Java 两个版本的实现。

## 项目结构

```
backend/
├── go/              # Go 版本（使用 Gin 框架）
│   ├── main.go
│   ├── config.go
│   ├── handlers.go
│   ├── models.go
│   ├── storage.go
│   ├── go.mod
│   ├── start.sh
│   ├── start.bat
│   └── README.md
│
└── java/            # Java 版本（使用 Spring Boot）
    ├── pom.xml
    ├── src/
    │   └── main/
    │       ├── java/com/junyou/
    │       └── resources/
    ├── start.sh
    ├── start.bat
    └── README.md
```

## 版本对比

| 特性 | Go 版本 | Java 版本 |
|------|---------|-----------|
| 端口 | 8080 | 8080 |
| 框架 | Gin | Spring Boot |
| SDK | junyou-sdk-go | junyou-sdk-java |
| 启动方式 | `go run .` | `mvn spring-boot:run` |
| 性能 | 高并发、低内存 | 企业级、生态丰富 |

## 快速开始

### Go 版本

```bash
# 进入 Go 项目目录
cd backend/go

# 设置环境变量（可选，有默认值）
export JUNYOU_ACCESS_ID="your-access-id"
export JUNYOU_ACCESS_KEY="your-access-key"
export JUNYOU_OPEN_ID="your-open-id"

# 使用启动脚本
./start.sh        # Linux/Mac
start.bat         # Windows

# 或手动运行
go mod download
go run .
```

详细文档请查看 [go/README.md](./go/README.md)

### Java 版本

```bash
# 进入 Java 项目目录
cd backend/java

# 设置环境变量（可选，有默认值）
export JUNYOU_ACCESS_ID="your-access-id"
export JUNYOU_ACCESS_KEY="your-access-key"
export JUNYOU_OPEN_ID="your-open-id"

# 使用启动脚本
./start.sh        # Linux/Mac
start.bat         # Windows

# 或手动运行
mvn clean compile
mvn spring-boot:run
```

详细文档请查看 [java/README.md](./java/README.md)

## API 接口

两个版本提供相同的 API 接口：

1. **POST** `/api/order` - 创建订单
2. **GET** `/api/release-message/{order_no}` - 获取释放权证数据
3. **POST** `/api/ewt/release-biz-no` - 存储业务编号并确认释放
4. **POST** `/api/signature-with-open-auth` - 获取 SDK 签名
5. **GET** `/health` - 健康检查

## 配置说明

### 环境变量

两个版本都支持通过环境变量配置：

- `JUNYOU_ACCESS_ID` - 访问 ID（必需）
- `JUNYOU_ACCESS_KEY` - 访问密钥，Base64 编码（必需）
- `JUNYOU_OPEN_ID` - Open ID（可选，用于签名）

如果未设置环境变量，将使用默认值（仅用于开发测试）。

### 端口配置

- **Go 版本**：默认 8080，在 `go/main.go` 中修改
- **Java 版本**：默认 8080，在 `java/src/main/resources/application.properties` 中修改

## 运行说明

**注意**：由于两个版本都使用 8080 端口，不能同时运行。请根据需要选择运行其中一个版本。

```bash
# 运行 Go 版本
cd backend/go
./start.sh

# 或运行 Java 版本
cd backend/java
./start.sh
```

两个版本都运行在：`http://localhost:8080`

前端可以通过修改 `API_BASE_URL` 来切换使用哪个版本进行测试（需要先停止当前运行的版本）。

## 功能特性

- ✅ **集成官方 SDK**：Go 版本使用 `junyou-sdk-go`，Java 版本使用 `junyou-sdk-java`
- ✅ 订单管理（创建、查询）
- ✅ 释放权证数据获取
- ✅ 权证释放业务编号存储
- ✅ **自动确认权证释放**：存储业务编号时自动调用 SDK 确认释放
- ✅ CORS 跨域支持
- ✅ 完整的错误处理
- ✅ 内存存储（可替换为数据库）

## 选择建议

### 选择 Go 版本，如果：
- 需要更高的并发性能
- 需要更低的内存占用
- 团队熟悉 Go 语言
- 需要快速启动和部署

### 选择 Java 版本，如果：
- 团队熟悉 Java/Spring Boot
- 需要丰富的企业级生态
- 需要与现有 Java 系统集成
- 需要 Spring 的各种功能（如 Spring Security、Spring Data 等）

## 开发建议

1. **开发阶段**：根据需要选择运行其中一个版本进行测试
2. **生产环境**：根据团队技术栈和性能需求选择其中一个版本
3. **数据存储**：两个版本都使用内存存储，数据不共享。生产环境建议替换为数据库

## 详细文档

- [Go 版本文档](./go/README.md)
- [Java 版本文档](./java/README.md)
