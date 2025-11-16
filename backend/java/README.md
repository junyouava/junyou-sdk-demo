# JunYou SDK Backend - Java 版本

这是 JunYou SDK 后端的 Java 实现版本，使用 Spring Boot 框架。

## 项目结构

```
java/
├── src/
│   └── main/
│       ├── java/com/junyou/
│       │   ├── Application.java              # 主启动类
│       │   ├── config/
│       │   │   └── JunyouConfig.java         # JunYou SDK 配置
│       │   ├── controller/
│       │   │   ├── OrderController.java      # 订单相关接口
│       │   │   ├── EwtController.java        # 权证相关接口
│       │   │   ├── SdkController.java        # SDK 签名接口
│       │   │   └── HealthController.java     # 健康检查接口
│       │   ├── model/
│       │   │   ├── Order.java                # 订单模型
│       │   │   ├── ReleaseMessage.java       # 释放权证数据模型
│       │   │   ├── EwtReleaseBizNo.java      # 业务编号模型
│       │   │   ├── ApiResponse.java          # API 响应模型
│       │   │   └── request/                  # 请求模型
│       │   ├── service/
│       │   │   └── JunyouSdkService.java     # JunYou SDK 服务
│       │   └── storage/
│       │       └── StorageService.java        # 存储服务（内存存储）
│       └── resources/
│           └── application.properties        # 配置文件
├── pom.xml                                   # Maven 依赖配置
└── README.md                                 # 本文档
```

## 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

## 配置说明

配置文件位于 `src/main/resources/application.properties`

### 服务器配置

- `server.port=8080` - Java 版本使用 8080 端口

### JunYou SDK 配置

可以通过环境变量或配置文件设置：

- `JUNYOU_ACCESS_ID` - 访问 ID
- `JUNYOU_ACCESS_KEY` - 访问密钥
- `JUNYOU_OPEN_ID` - Open ID

如果未设置环境变量，将使用默认值（仅用于开发测试）。

## 编译和运行

### 使用 Maven

```bash
# 进入 Java 项目目录
cd backend/java

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 打包运行

```bash
# 打包
mvn clean package

# 运行 JAR 文件
java -jar target/junyou-sdk-backend-java-1.0.0.jar
```

### 使用 IDE

直接运行 `Application.java` 的 main 方法即可。

## API 接口

Java 版本提供与 Go 版本相同的 API 接口：

1. **POST** `/api/order` - 创建订单
2. **GET** `/api/release-message/{order_no}` - 获取释放权证数据
3. **POST** `/api/ewt/release-biz-no` - 存储业务编号并确认释放
4. **POST** `/api/signature-with-open-auth` - 获取 SDK 签名
5. **GET** `/health` - 健康检查

## 与 Go 版本的区别

1. **端口相同**：
   - Go 版本：8080
   - Java 版本：8080

2. **存储方式**：
   - 两个版本都使用内存存储，数据不共享

3. **SDK 调用**：
   - Java 版本已集成真实的 JunYou SDK Java 库
   - 所有 SDK 调用都通过真实的 SDK 客户端完成

## 注意事项

1. **JunYou SDK Java 版本**：
   - 已集成真实的 JunYou SDK Java 库：`com.junyouava:junyou-sdk-java:1.0.0`
   - SDK 客户端在 `JunyouConfig` 中自动初始化
   - 所有 SDK 调用都通过真实的 SDK 客户端完成

2. **并发安全**：
   - 存储服务使用 `ConcurrentHashMap` 和 `ReadWriteLock` 保证线程安全

3. **CORS 配置**：
   - 当前配置允许所有来源访问（`@CrossOrigin(origins = "*")`）
   - 生产环境建议修改为具体域名

## 测试

启动服务后，可以通过以下方式测试：

```bash
# 健康检查
curl http://localhost:8080/health

# 创建订单
curl -X POST http://localhost:8080/api/order \
  -H "Content-Type: application/json" \
  -d '{"amount":"100.00"}'
```

## 与 Go 版本并存

**注意**：由于两个版本都使用 8080 端口，不能同时运行。请根据需要选择运行其中一个版本。

- Go 版本：`http://localhost:8080`
- Java 版本：`http://localhost:8080`

