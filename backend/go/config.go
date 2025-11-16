package main

import (
	"log"
	"os"

	junyousdk "github.com/junyouava/junyou-sdk-go"
)

var junyouClient *junyousdk.Client
var junyouOpenId string

// InitJunyouSDK 初始化 JunYou SDK 客户端
func InitJunyouSDK() error {
	// 从环境变量获取配置
	accessId := os.Getenv("JUNYOU_ACCESS_ID")
	accessKey := os.Getenv("JUNYOU_ACCESS_KEY")
	openId := os.Getenv("JUNYOU_OPEN_ID")

	// 如果环境变量未设置，使用默认值（仅用于开发测试）
	if accessId == "" {
		accessId = ""
		log.Println("⚠️  警告: 使用默认 AccessId，请设置环境变量 JUNYOU_ACCESS_ID")
	}
	if accessKey == "" {
		accessKey = ""
		log.Println("⚠️  警告: 使用默认 AccessKey，请设置环境变量 JUNYOU_ACCESS_KEY")
	}
	if openId == "" {
		openId = ""
		log.Println("⚠️  警告: 使用默认 OpenId，请设置环境变量 JUNYOU_OPEN_ID")
	}

	// 保存 openId 到全局变量
	junyouOpenId = openId

	// 创建配置
	config := junyousdk.DefaultConfig().
		WithAccessId(accessId).
		WithAccessKey(accessKey)

	// 可选：自定义 API 地址（如果需要）
	// config = config.WithAddress("https://your-custom-api-address.com")

	// 创建客户端
	client, err := junyousdk.NewClient(config)
	if err != nil {
		return err
	}

	junyouClient = client
	log.Println("✅ JunYou SDK 客户端初始化成功")
	return nil
}

// GetJunyouClient 获取 JunYou SDK 客户端
func GetJunyouClient() *junyousdk.Client {
	return junyouClient
}

// GetJunyouOpenId 获取 JunYou OpenId
func GetJunyouOpenId() string {
	return junyouOpenId
}
