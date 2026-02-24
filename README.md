# SillyTavern Android

一个完整还原SillyTavern功能的Android应用程序，支持Android 8.0及以上系统版本。

## 功能特性

### 核心功能
- **角色卡管理**: 支持导入PNG嵌入数据、JSON格式的角色卡
- **聊天功能**: 流式响应、Markdown渲染、消息编辑
- **世界书(Lorebook)**: 创建、编辑、导入导出世界书条目
- **预设管理**: 导入、编辑、应用生成预设配置
- **正则表达式**: Regex脚本导入和管理
- **多API后端**: OpenAI、Claude、KoboldAI、NovelAI等

### 支持的API
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

### 数据导入
- 角色卡 (PNG with embedded data, JSON)
- 世界书/ Lorebook (JSON)
- 预设文件 (JSON)
- 正则表达式脚本 (JSON)

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose + Material 3
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **数据库**: Room
- **网络**: Retrofit + OkHttp
- **异步**: Kotlin Coroutines + Flow
- **图片加载**: Coil
- **序列化**: Kotlinx Serialization + Gson

## 项目结构

```
app/
├── src/main/
│   ├── java/com/sillytavern/android/
│   │   ├── data/
│   │   │   ├── local/          # 本地数据库、偏好设置
│   │   │   ├── model/          # 数据模型
│   │   │   ├── remote/         # API服务
│   │   │   └── repository/     # 数据仓库
│   │   ├── di/                 # 依赖注入模块
│   │   ├── ui/
│   │   │   ├── components/     # 可复用UI组件
│   │   │   ├── navigation/     # 导航
│   │   │   ├── screens/        # 屏幕/页面
│   │   │   ├── theme/          # 主题配置
│   │   │   └── viewmodel/      # ViewModel
│   │   ├── util/               # 工具类
│   │   ├── MainActivity.kt
│   │   └── SillyTavernApp.kt
│   └── res/                    # 资源文件
└── build.gradle.kts
```

## 构建说明

### 环境要求
- JDK 17+
- Android SDK 34
- Gradle 8.2+

### 构建步骤

#### Windows
```batch
build.bat
```

#### Linux/macOS
```bash
chmod +x build.sh
./build.sh
```

#### 手动构建
```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

### 输出位置
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

## 签名发布版本

1. 生成签名密钥:
```bash
keytool -genkey -v -keystore sillytavern.keystore -alias sillytavern -keyalg RSA -keysize 2048 -validity 10000
```

2. 在 `app/build.gradle.kts` 中配置签名:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("sillytavern.keystore")
            storePassword = "your_password"
            keyAlias = "sillytavern"
            keyPassword = "your_password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

3. 构建签名APK:
```bash
./gradlew assembleRelease
```

## 使用指南

### 首次使用
1. 安装APK
2. 打开应用，进入设置
3. 配置API设置（API类型、URL、密钥）
4. 导入角色卡开始使用

### 导入角色卡
1. 点击右上角的"+"按钮
2. 选择PNG或JSON文件
3. 角色卡将自动解析并导入

### 开始聊天
1. 点击角色卡进入详情页
2. 点击"Start Chat"开始新对话
3. 输入消息并发送

## 兼容性

- **最低SDK**: Android 8.0 (API 26)
- **目标SDK**: Android 14 (API 34)
- **支持架构**: arm64-v8a, armeabi-v7a, x86, x86_64

## 许可证

本项目基于 GNU Affero General Public License v3.0 许可证开源。

原SillyTavern项目: https://github.com/SillyTavern/SillyTavern

## 致谢

- SillyTavern团队 - 原始项目
- 所有贡献者

## 免责声明

本项目仅供学习和研究目的。使用本应用程序时，请遵守相关法律法规和服务条款。开发者不对任何滥用行为负责。
