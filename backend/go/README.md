# JunYou SDK Backend - Go 版本

这是 JunYou SDK 后端的 Go 实现版本，使用 Gin 框架。

## 项目结构

```
go/
├── main.go          # 主程序入口
├── config.go        # JunYou SDK 配置和初始化
├── models.go        # 数据模型定义
├── handlers.go      # 请求处理器（集成 SDK）
├── storage.go       # 存储层（内存存储）
├── go.mod           # Go 模块依赖
├── go.sum           # 依赖校验和
├── start.sh         # Linux/Mac 启动脚本
├── start.bat        # Windows 启动脚本
└── README.md        # 本文档
```

## 环境要求

- Go 1.21 或更高版本

## 配置说明

### JunYou SDK 配置

SDK 配置通过环境变量设置：

- `JUNYOU_ACCESS_ID`：访问 ID（必需）
- `JUNYOU_ACCESS_KEY`：访问密钥，Base64 编码（必需）
- `JUNYOU_OPEN_ID`：Open ID（可选，用于签名）

配置方式：

```bash
# Linux/Mac
export JUNYOU_ACCESS_ID="your-access-id"
export JUNYOU_ACCESS_KEY="your-access-key"
export JUNYOU_OPEN_ID="your-open-id"

# Windows PowerShell
$env:JUNYOU_ACCESS_ID="your-access-id"
$env:JUNYOU_ACCESS_KEY="your-access-key"
$env:JUNYOU_OPEN_ID="your-open-id"

# Windows CMD
set JUNYOU_ACCESS_ID=your-access-id
set JUNYOU_ACCESS_KEY=your-access-key
set JUNYOU_OPEN_ID=your-open-id
```

如果环境变量未设置，将使用默认值（仅用于开发测试）。

### 端口配置

默认端口为 `8080`，可以在 `main.go` 中修改：

```go
port := ":8080"  // 修改为你需要的端口
```

### CORS 配置

当前配置允许所有来源访问，生产环境建议修改 `main.go` 中的 CORS 配置：

```go
c.Writer.Header().Set("Access-Control-Allow-Origin", "*")  // 改为具体域名
```

## 编译和运行

### 使用启动脚本

```bash
# Linux/Mac
cd backend/go
chmod +x start.sh
./start.sh

# Windows
cd backend\go
start.bat
```

### 手动运行

```bash
# 进入项目目录
cd backend/go

# 安装依赖
go mod download

# 运行项目
go run .
```

### 编译为可执行文件

```bash
# Linux/Mac
cd backend/go
GOOS=linux GOARCH=amd64 go build -o junyou-backend

# Windows
cd backend\go
go build -o junyou-backend.exe
```

## API 接口

1. **POST** `/api/order` - 创建订单
2. **GET** `/api/release-message/{order_no}` - 获取释放权证数据
3. **POST** `/api/ewt/release-biz-no` - 存储业务编号并确认释放
4. **POST** `/api/signature-with-open-auth` - 获取 SDK 签名
5. **GET** `/health` - 健康检查

## 与 Java 版本的区别

1. **端口不同**：
   - Go 版本：8080
   - Java 版本：8081

2. **存储方式**：
   - 两个版本都使用内存存储，数据不共享

3. **性能**：
   - Go 版本通常具有更好的并发性能和更低的内存占用

## 数据库集成（可选）

当前使用内存存储，生产环境建议替换为数据库。可以：

1. 使用 GORM 连接 MySQL/PostgreSQL
2. 使用 Redis 作为缓存
3. 实现持久化存储

示例（使用 GORM + MySQL）：

```go
// 在 storage.go 中替换内存存储
import "gorm.io/gorm"

var db *gorm.DB

func InitStorage() {
    // 连接数据库
    db = connectDB()
    // 自动迁移
    db.AutoMigrate(&Order{}, &EwtReleaseBizNo{})
}
```

## 部署

### Docker 部署（可选）

创建 `Dockerfile`:

```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY . .
RUN go mod download
RUN go build -o junyou-backend

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/junyou-backend .
EXPOSE 8080
CMD ["./junyou-backend"]
```

构建和运行：

```bash
docker build -t junyou-backend .
docker run -p 8080:8080 junyou-backend
```

