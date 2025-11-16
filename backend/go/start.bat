@echo off
chcp 65001 >nul
echo ========================================
echo   JunYou SDK 后端服务启动脚本 (Go 版本)
echo ========================================
echo.

REM 检查 Go 是否安装
where go >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 未找到 Go，请先安装 Go 1.21 或更高版本
    echo 下载地址: https://golang.org/dl/
    pause
    exit /b 1
)

echo [1/3] 检查 Go 版本...
go version
echo.

REM 检查必需的环境变量
set MISSING_VARS=0

if "%JUNYOU_ACCESS_ID%"=="" (
    echo [❌ 错误] 未设置 JUNYOU_ACCESS_ID 环境变量（必需）
    echo 请设置环境变量：
    echo   set JUNYOU_ACCESS_ID=your-access-id
    echo 或在 PowerShell 中：
    echo   $env:JUNYOU_ACCESS_ID="your-access-id"
    set MISSING_VARS=1
)

if "%JUNYOU_ACCESS_KEY%"=="" (
    echo [❌ 错误] 未设置 JUNYOU_ACCESS_KEY 环境变量（必需）
    echo 请设置环境变量：
    echo   set JUNYOU_ACCESS_KEY=your-access-key
    echo 或在 PowerShell 中：
    echo   $env:JUNYOU_ACCESS_KEY="your-access-key"
    set MISSING_VARS=1
)

if "%JUNYOU_OPEN_ID%"=="" (
    echo [⚠️  警告] 未设置 JUNYOU_OPEN_ID 环境变量（可选，用于签名功能）
    echo 如需使用签名功能，请设置：
    echo   set JUNYOU_OPEN_ID=your-open-id
    echo 或在 PowerShell 中：
    echo   $env:JUNYOU_OPEN_ID="your-open-id"
)

if %MISSING_VARS%==1 (
    echo.
    echo 请先设置必需的环境变量后再启动服务
    pause
    exit /b 1
)

echo [✓] 环境变量检查通过

REM 进入项目目录
cd /d "%~dp0"

echo [2/3] 安装依赖...
go mod download
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 依赖安装失败
    pause
    exit /b 1
)
echo.

echo [3/3] 启动服务...
echo.
echo ========================================
echo   服务将在 http://localhost:8080 启动
echo   按 Ctrl+C 停止服务
echo ========================================
echo.

go run .

pause

