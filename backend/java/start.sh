#!/bin/bash

# JunYou SDK Backend - Java 版本启动脚本

echo "🚀 启动 JunYou SDK Backend (Java 版本)..."

# 检查 Java 是否安装
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java，请先安装 JDK 17 或更高版本"
    exit 1
fi

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 未找到 Maven，请先安装 Maven 3.6 或更高版本"
    exit 1
fi

# 进入项目目录
cd "$(dirname "$0")"

# 检查必需的环境变量
MISSING_VARS=0

if [ -z "$JUNYOU_ACCESS_ID" ]; then
    echo "❌ 错误: 未设置 JUNYOU_ACCESS_ID 环境变量（必需）"
    echo "请设置环境变量："
    echo "  export JUNYOU_ACCESS_ID=\"your-access-id\""
    MISSING_VARS=1
fi

if [ -z "$JUNYOU_ACCESS_KEY" ]; then
    echo "❌ 错误: 未设置 JUNYOU_ACCESS_KEY 环境变量（必需）"
    echo "请设置环境变量："
    echo "  export JUNYOU_ACCESS_KEY=\"your-access-key\""
    MISSING_VARS=1
fi

if [ -z "$JUNYOU_OPEN_ID" ]; then
    echo "⚠️  警告: 未设置 JUNYOU_OPEN_ID 环境变量（可选，用于签名功能）"
    echo "如需使用签名功能，请设置："
    echo "  export JUNYOU_OPEN_ID=\"your-open-id\""
fi

if [ $MISSING_VARS -eq 1 ]; then
    echo ""
    echo "请先设置必需的环境变量后再启动服务"
    exit 1
fi

# 设置环境变量
export JUNYOU_ACCESS_ID=${JUNYOU_ACCESS_ID:-""}
export JUNYOU_ACCESS_KEY=${JUNYOU_ACCESS_KEY:-""}
export JUNYOU_OPEN_ID=${JUNYOU_OPEN_ID:-""}

echo "📋 配置信息:"
if [ -n "$JUNYOU_ACCESS_ID" ]; then
    echo "   - Access ID: ${JUNYOU_ACCESS_ID:0:20}..."
fi
if [ -n "$JUNYOU_OPEN_ID" ]; then
    echo "   - Open ID: ${JUNYOU_OPEN_ID:0:20}..."
fi
echo "   ✓ 环境变量检查通过"
echo ""

# 编译并运行
echo "🔨 编译项目..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ 编译失败"
    exit 1
fi

echo ""
echo "✅ 编译成功"
echo "🌐 启动服务器在 http://localhost:8080"
echo ""

# 运行项目
mvn spring-boot:run

