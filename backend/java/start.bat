@echo off
REM JunYou SDK Backend - Java 版本启动脚本 (Windows)

echo 🚀 启动 JunYou SDK Backend (Java 版本)...

REM 检查 Java 是否安装
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ 错误: 未找到 Java，请先安装 JDK 17 或更高版本
    pause
    exit /b 1
)

REM 检查 Maven 是否安装
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ 错误: 未找到 Maven，请先安装 Maven 3.6 或更高版本
    pause
    exit /b 1
)

REM 进入项目目录
cd /d "%~dp0"

REM 检查必需的环境变量
set MISSING_VARS=0

if "%JUNYOU_ACCESS_ID%"=="" (
    echo ❌ 错误: 未设置 JUNYOU_ACCESS_ID 环境变量（必需）
    echo 请设置环境变量：
    echo   set JUNYOU_ACCESS_ID=your-access-id
    echo 或在 PowerShell 中：
    echo   $env:JUNYOU_ACCESS_ID="your-access-id"
    set MISSING_VARS=1
)

if "%JUNYOU_ACCESS_KEY%"=="" (
    echo ❌ 错误: 未设置 JUNYOU_ACCESS_KEY 环境变量（必需）
    echo 请设置环境变量：
    echo   set JUNYOU_ACCESS_KEY=your-access-key
    echo 或在 PowerShell 中：
    echo   $env:JUNYOU_ACCESS_KEY="your-access-key"
    set MISSING_VARS=1
)

if "%JUNYOU_OPEN_ID%"=="" (
    echo ⚠️  警告: 未设置 JUNYOU_OPEN_ID 环境变量（可选，用于签名功能）
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

REM 设置环境变量
if "%JUNYOU_ACCESS_ID%"=="" set JUNYOU_ACCESS_ID=
if "%JUNYOU_ACCESS_KEY%"=="" set JUNYOU_ACCESS_KEY=
if "%JUNYOU_OPEN_ID%"=="" set JUNYOU_OPEN_ID=

echo 📋 配置信息:
if not "%JUNYOU_ACCESS_ID%"=="" (
    echo    - Access ID: %JUNYOU_ACCESS_ID:~0,20%...
)
if not "%JUNYOU_OPEN_ID%"=="" (
    echo    - Open ID: %JUNYOU_OPEN_ID:~0,20%...
)
echo    ✓ 环境变量检查通过
echo.

REM 编译并运行
echo 🔨 编译项目...
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo ❌ 编译失败
    pause
    exit /b 1
)

echo.
echo ✅ 编译成功
echo 🌐 启动服务器在 http://localhost:8080
echo.

REM 运行项目
call mvn spring-boot:run

pause

