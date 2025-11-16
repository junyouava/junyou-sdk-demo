package main

import (
	"time"
)

// Order 订单模型
type Order struct {
	OrderNo  string    `json:"order_no"`
	Amount   string    `json:"amount"`
	Status   string    `json:"status"`
	CreateAt time.Time `json:"create_at"`
}

// ReleaseMessage 释放权证数据模型
type ReleaseMessage struct {
	Amount       string `json:"amount"`
	Ratio        string `json:"ratio"`
	Level1Ratio  string `json:"level1_ratio"`
	Level2Ratio  string `json:"level2_ratio"`
	Level1OpenID string `json:"level1_open_id"`
	Level2OpenID string `json:"level2_open_id"`
}

// EwtReleaseBizNo 权证释放业务编号模型
type EwtReleaseBizNo struct {
	EwtBizNo string    `json:"ewt_biz_no"`
	OrderNo  string    `json:"order_no"`
	CreateAt time.Time `json:"create_at"`
}

// API 响应结构
type ApiResponse struct {
	Code    int         `json:"code"`
	Message string      `json:"message,omitempty"`
	Data    interface{} `json:"data,omitempty"`
}

// 创建订单请求
type CreateOrderRequest struct {
	Amount string `json:"amount" binding:"required"`
}

// 存储业务编号请求
type StoreEwtReleaseBizNoRequest struct {
	EwtBizNo string `json:"ewt_biz_no" binding:"required"`
	OrderNo  string `json:"order_no" binding:"required"`
}
