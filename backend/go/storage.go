package main

import (
	"log"
	"sync"
)

// 内存存储（生产环境应使用数据库）
var (
	orders      = make(map[string]*Order)
	ewtBizNos   = make(map[string]*EwtReleaseBizNo)
	storageLock sync.RWMutex
)

// InitStorage 初始化存储
func InitStorage() {
	// 可以在这里初始化一些测试数据
	log.Println("存储初始化完成")
}

// 订单存储操作
func saveOrder(order *Order) {
	storageLock.Lock()
	defer storageLock.Unlock()
	orders[order.OrderNo] = order
}

func getOrderByNo(orderNo string) (*Order, bool) {
	storageLock.RLock()
	defer storageLock.RUnlock()
	order, exists := orders[orderNo]
	return order, exists
}

// 业务编号存储操作
func saveEwtReleaseBizNo(ewtBizNo *EwtReleaseBizNo) {
	storageLock.Lock()
	defer storageLock.Unlock()
	ewtBizNos[ewtBizNo.OrderNo] = ewtBizNo
}

func getEwtReleaseBizNoByOrderNo(orderNo string) (*EwtReleaseBizNo, bool) {
	storageLock.RLock()
	defer storageLock.RUnlock()
	ewtBizNo, exists := ewtBizNos[orderNo]
	return ewtBizNo, exists
}
