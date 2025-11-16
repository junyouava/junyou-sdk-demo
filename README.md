# JunYou SDK Demo

JunYou SDK 演示项目，包含完整的前后端实现，用于演示释放权证（ReleaseWarrant）功能。

## 📋 项目简介

本项目是一个完整的 JunYou SDK 集成示例，包含：

- **前端演示页面**：基于 HTML + JavaScript 的交互式演示界面
- **后端服务**：提供 Go 和 Java 两个版本的实现
- **完整流程**：从订单创建到权证释放的完整业务流程演示

## 🏗️ 项目结构

```
junyou-sdk-demo/
├── index.html              # 前端演示页面
├── LICENSE                 # 许可证文件
├── README.md              # 本文档
│
└── backend/               # 后端服务
    ├── README.md          # 后端总览文档
    │
    ├── go/                # Go 版本（Gin 框架）
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
    └── java/              # Java 版本（Spring Boot）
        ├── pom.xml
        ├── src/
        │   └── main/
        │       ├── java/com/junyou/
        │       └── resources/
        ├── start.sh
        ├── start.bat
        └── README.md
```

## ✨ 功能特性

- ✅ **完整的释放权证流程演示**
  - 订单创建
  - 释放权证参数获取
  - SDK 调用释放权证
  - 业务编号存储和确认

- ✅ **双后端实现**
  - Go 版本：基于 Gin 框架，高性能、低内存占用
  - Java 版本：基于 Spring Boot，企业级生态

- ✅ **前端演示界面**
  - 美观的 UI 设计
  - 实时日志显示
  - 流程步骤指示
  - 错误处理和提示

- ✅ **SDK 集成**
  - 自动签名获取
  - OpenAuth 认证
  - 完整的错误处理

## 🚀 快速开始

### 前置要求

**Go 版本：**
- Go 1.21 或更高版本

**Java 版本：**
- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 1. 启动后端服务

选择其中一个版本启动：

#### Go 版本

```bash
cd backend/go

# Linux/Mac
chmod +x start.sh
./start.sh

# Windows
start.bat
```

#### Java 版本

```bash
cd backend/java

# 使用 Maven 运行
mvn spring-boot:run

# 或使用启动脚本
# Linux/Mac
chmod +x start.sh
./start.sh

# Windows
start.bat
```

**注意**：两个版本都使用 8080 端口，不能同时运行。请根据需要选择其中一个版本。

### 2. 配置环境变量（可选）

如果未设置环境变量，将不能使用（JUNYOU_OPEN_ID配置方式，仅用于开发测试）：

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

### 3. 打开前端页面

在浏览器中打开 `index.html` 文件，或使用本地服务器：

```bash
# 使用 Python 3
python -m http.server 8000

# 使用 Node.js (http-server)
npx http-server -p 8000

# 然后访问 http://localhost:8000
```

### 4. 开始演示

1. 前端页面会自动初始化 JunYou SDK
2. 点击"释放权证"按钮开始流程
3. 系统会自动执行以下步骤：
   - 创建订单
   - 获取释放权证参数
   - 调用 SDK 释放权证
   - 保存业务编号并确认释放

## 📖 使用说明

### 完整流程

1. **SDK 初始化**（页面加载时自动执行）
   - SDK 自动调用后端签名接口获取认证信息

2. **创建订单**
   - 接口：`POST /api/order`
   - 返回订单号和金额

3. **获取释放权证参数**
   - 接口：`GET /api/release-message/{order_no}`
   - 返回释放权证所需的参数

4. **调用 SDK 释放权证**
   - 使用 JunYou H5 SDK 的 `ReleaseWarrant` 方法
   - SDK 自动处理签名和认证

5. **保存业务编号**
   - 接口：`POST /api/ewt/release-biz-no`
   - 后端会自动调用 SDK 确认权证释放

### API 接口

后端提供以下 API 接口：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/order` | 创建订单 |
| GET | `/api/release-message/{order_no}` | 获取释放权证数据 |
| POST | `/api/ewt/release-biz-no` | 存储业务编号并确认释放 |
| POST | `/api/signature-with-open-auth` | 获取 SDK 签名 |
| GET | `/health` | 健康检查 |

详细接口文档请查看 [backend/README.md](./backend/README.md)

## 🔧 配置说明

### 后端配置

- **端口**：默认 8080（可在代码中修改）
- **CORS**：当前允许所有来源访问（生产环境建议修改）
- **存储**：使用内存存储（生产环境建议替换为数据库）

### 前端配置

- **API 地址**：默认 `http://localhost:8080/api`
- **SDK 地址**：使用 CDN 加载 `https://open-sdk.junyouchain.com/jysdk-h5-0.0.1.js`

## 📚 详细文档

- [后端总览文档](./backend/README.md) - 后端服务说明和版本对比
- [Go 版本文档](./backend/go/README.md) - Go 版本详细文档
- [Java 版本文档](./backend/java/README.md) - Java 版本详细文档

## 🎯 版本选择建议

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

## ⚠️ 注意事项

1. **端口冲突**：Go 和 Java 版本都使用 8080 端口，不能同时运行
2. **数据存储**：两个版本都使用内存存储，数据不共享，重启后数据会丢失
3. **生产环境**：
   - 建议替换内存存储为数据库
   - 修改 CORS 配置为具体域名
   - 配置正确的环境变量
   - 使用 HTTPS

## 🐛 问题排查

### 后端服务无法启动

1. 检查端口是否被占用
2. 检查环境变量配置是否正确
3. 查看控制台错误信息

### 前端无法连接后端

1. 确认后端服务已启动
2. 检查浏览器控制台是否有 CORS 错误
3. 确认 API 地址配置正确

### SDK 初始化失败

1. 检查 JunYou SDK 是否正常加载
2. 检查后端签名接口是否正常
3. 查看浏览器控制台和网络请求

## 📄 许可证

详见 [LICENSE](./LICENSE) 文件

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📞 支持

如有问题，请查看相关文档或提交 Issue。

