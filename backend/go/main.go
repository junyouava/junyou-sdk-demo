package main

import (
	"log"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

func main() {
	// 初始化存储
	InitStorage()

	// 初始化 JunYou SDK
	if err := InitJunyouSDK(); err != nil {
		log.Fatalf("❌ JunYou SDK 初始化失败: %v", err)
	}

	// 创建 Gin 路由
	r := gin.Default()

	// 配置 CORS（允许前端跨域访问）
	r.Use(corsMiddleware())

	// API 路由组
	api := r.Group("/api")
	{
		// 订单相关接口
		api.POST("/order", createOrder)

		// 释放权证数据接口
		api.GET("/release-message/:order_no", getReleaseMessage)

		// 业务编号存储接口（会调用 SDK 确认权证释放）
		api.POST("/ewt/release-biz-no", storeEwtReleaseBizNo)

		// SDK 签名接口（用于前端 SDK 初始化）
		api.POST("/signature-with-open-auth", getSDKSignatureWithOpenAuth)
	}

	// 健康检查接口
	r.GET("/health", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"status":  "ok",
			"message": "服务运行正常",
			"time":    time.Now().Format("2006-01-02 15:04:05"),
		})
	})

	// 启动服务器
	port := ":8080"
	log.Printf("🚀 服务器启动在 http://localhost%s", port)

	if err := r.Run(port); err != nil {
		log.Fatal("服务器启动失败:", err)
	}
}

// CORS 中间件
func corsMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		c.Writer.Header().Set("Access-Control-Allow-Credentials", "true")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization, accept, origin, Cache-Control, X-Requested-With")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS, GET, PUT, DELETE")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204)
			return
		}

		c.Next()
	}
}
