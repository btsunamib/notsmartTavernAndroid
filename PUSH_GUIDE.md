# Git 推送指南

由于环境限制，请手动执行以下命令来完成推送：

## 打开 PowerShell 或 CMD，然后运行：

```powershell
cd "c:\Users\22148\Documents\代码\还原酒馆测试\SillyTavernAndroid"

# 1. 提交代码
git commit -m "Initial commit"

# 2. 推送到 GitHub
git push -u origin main
```

---

## 首次推送后，以后的更新命令：

```powershell
cd "c:\Users\22148\Documents\代码\还原酒馆测试\SillyTavernAndroid"

# 添加所有更改
git add .

# 提交更改
git commit -m "Your message"

# 推送到 GitHub
git push
```

---

## 推送成功后

1. 访问 https://github.com/btsunamib/notsmartTavernAndroid
2. 点击 "Actions" 标签
3. 等待自动构建完成（约5-10分钟）
4. 在 Artifacts 中下载 APK 文件

---

## 如果推送时需要输入用户名和密码

请使用 GitHub Personal Access Token：
1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token (classic)"
3. 勾选 "repo" 权限
4. 生成后复制 token 作为密码使用
