# Riru - QQ Lite

[Riru](https://github.com/RikkaApps/Riru) 模块. 移除 QQ 的一些 UI 组件

## 最低要求

[Riru](https://github.com/RikkaApps/Riru) 版本 > 19 .

## 建议

建议配合 "简洁模式" 使用

## 构建

1. 安装 JDK ,Gradle ,Android SDK ,Android NDK

2. 创建文件 `local.properties` 在项目根目录
   ```properties
   sdk.dir=/path/to/android-sdk
   ndk.dir=/path/to/android-ndk
   cmake.dir=/path/to/android-cmake/*version*
   ```
   
3. 运行命令
   ```bash
   ./gradlew build
   ```

4. 在 module/build/outputs 获取 riru-qq-lite.zip 

