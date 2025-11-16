#!/bin/bash

echo "========================================"
echo "  JunYou SDK 后端服务启动脚本 (Go 版本)"
echo "========================================"
echo ""

# 检查 Go 是否安装
if ! command -v go &> /dev/null; then
    echo "[错误] 未找到 Go，请先安装 Go 1.21 或更高版本"
    echo "下载地址: https://golang.org/dl/"
    exit 1
fi

echo "[1/3] 检查 Go 版本..."
go version
echo ""

# 检查必需的环境变量
MISSING_VARS=0

if [ -z "$JUNYOU_ACCESS_ID" ]; then
    echo "[❌ 错误] 未设置 JUNYOU_ACCESS_ID 环境变量（必需）"
    echo "请设置环境变量："
    echo "  export JUNYOU_ACCESS_ID=\"your-access-id\""
    MISSING_VARS=1
fi

if [ -z "$JUNYOU_ACCESS_KEY" ]; then
    echo "[❌ 错误] 未设置 JUNYOU_ACCESS_KEY 环境变量（必需）"
    echo "请设置环境变量："
    echo "  export JUNYOU_ACCESS_KEY=\"your-access-key\""
    MISSING_VARS=1
fi

if [ -z "$JUNYOU_OPEN_ID" ]; then
    echo "[⚠️  警告] 未设置 JUNYOU_OPEN_ID 环境变量（可选，用于签名功能）"
    echo "如需使用签名功能，请设置："
    echo "  export JUNYOU_OPEN_ID=\"your-open-id\""
fi

if [ $MISSING_VARS -eq 1 ]; then
    echo ""
    echo "请先设置必需的环境变量后再启动服务"
    exit 1
fi

echo "[✓] 环境变量检查通过"

# 进入项目目录
cd "$(dirname "$0")"

echo "[2/3] 安装依赖..."
go mod download
if [ $? -ne 0 ]; then
    echo "[错误] 依赖安装失败"
    exit 1
fi
echo ""

echo "[3/3] 启动服务..."
echo ""
echo "========================================"
echo "  服务将在 http://localhost:8080 启动"
echo "  按 Ctrl+C 停止服务"
echo "========================================"
echo ""

go run .

