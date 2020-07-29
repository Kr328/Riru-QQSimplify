# Riru - QQ Simplify

[Riru](https://github.com/RikkaApps/Riru) 模块. 移除 QQ 的一些 UI 组件.

### 最低要求

[Riru](https://github.com/RikkaApps/Riru) 版本 > v21.2 .

### 功能

建议配合 "简洁模式" 使用

移除下列组件
 - `动态`
 - `看点`
 - `拍摄`
 - `一起玩`
 - `小程序(仅隐藏)`

### 构建

1. 安装 JDK, Android SDK, Android NDK

2. 创建文件 `local.properties` 在项目根目录
   ```properties
   sdk.dir=/path/to/android-sdk
   ndk.dir=/path/to/android-ndk
   cmake.dir=/path/to/android-cmake/*version*
   ```
   
3. 运行命令
   ```bash
   ./gradlew module:assembleRelease
   ```

4. 在 `module/build/outputs` 获取 `riru-qq-simplify-release.zip` 

