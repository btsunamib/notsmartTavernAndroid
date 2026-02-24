# GitHub Setup for SillyTavern Android

## 快速开始

### 1. 创建GitHub仓库

1. 访问 https://github.com/new
2. 仓库名称: `SillyTavern-Android`
3. 选择 "Public"
4. 点击 "Create repository"

### 2. 推送代码到GitHub

```bash
# 在 SillyTavernAndroid 目录下执行:

# 初始化Git（如果还没有）
git init

# 添加所有文件
git add .

# 提交更改
git commit -m "Initial commit: SillyTavern Android App"

# 添加远程仓库（替换 YOUR_USERNAME 为你的GitHub用户名）
git remote add origin https://github.com/YOUR_USERNAME/SillyTavern-Android.git

# 推送到GitHub
git push -u origin main
```

### 3. 启用GitHub Actions

推送代码后，GitHub Actions会自动开始构建APK：

1. 访问你的GitHub仓库
2. 点击 "Actions" 标签
3. 你会看到 "Build Android APK" 工作流正在运行
4. 等待构建完成（约5-10分钟）

### 4. 下载APK

构建成功后：
- **Debug APK**: 点击构建 → Artifacts → `debug-apk`
- **Release APK**: 需要配置签名密钥（可选）

## 配置签名发布版本（可选）

要生成签名APK，需要配置密钥库：

1. 生成签名密钥：
```bash
keytool -genkey -v -keystore sillytavern.keystore -alias sillytavern -keyalg RSA -keysize 2048 -validity 10000
```

2. 将密钥库转换为Base64：
```bash
# Windows
certutil -encodefile sillytavern.keystore keystore.b64

# Linux/Mac
base64 sillytavern.keystore > keystore.b64
```

3. 在GitHub仓库中创建密钥（Settings → Secrets and variables → Actions）：
   - `KEYSTORE_BASE64`: 密钥库的Base64内容
   - `KEYSTORE_PASSWORD`: 密钥库密码
   - `KEY_ALIAS`: 密钥别名（sillytavern）
   - `KEY_PASSWORD`: 密钥密码

4. 创建一个发布版本标签：
```bash
git tag v1.0.0
git push origin v1.0.0
```

## 自动构建触发

每次以下操作都会触发自动构建：
- 推送到 `main` 分支
- 创建Pull Request
- 手动触发（Workflow dispatch）
- 创建版本标签（如 `v1.0.0`）

## 项目结构

```
SillyTavernAndroid/
├── .github/
│   └── workflows/
│       └── build.yml       # GitHub Actions工作流
├── app/
│   └── src/main/
│       └── java/...        # 应用代码
├── gradle/                 # Gradle配置
├── build.gradle.kts        # 项目构建配置
├── app/build.gradle.kts   # App模块配置
└── README.md              # 项目文档
```

## 功能支持

| 功能 | 状态 |
|------|------|
| 角色卡导入 (PNG/JSON) | ✅ |
| 世界书 (Lorebook) | ✅ |
| 预设管理 | ✅ |
| 正则表达式脚本 | ✅ |
| 多API后端 | ✅ |
| 扩展框架 | ✅ |
| TTS语音 | ✅ |
| 图片生成 (SD API) | ✅ |
| 翻译 | ✅ |
| 自动翻译 | ✅ |

## 支持的API后端

- OpenAI (GPT-4, GPT-3.5)
- Claude (Anthropic)
- Mistral AI
- Google AI
- KoboldAI / KoboldCPP
- NovelAI
- Oobabooga / Text Generation WebUI
- TabbyAPI
- OpenRouter
- 自定义API端点

## 许可证

本项目基于 AGPL-3.0 许可证开源。
