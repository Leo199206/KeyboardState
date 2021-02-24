# KeyboardState
键盘状态监听工具类

#### 效果预览
<img src="https://raw.githubusercontent.com/Leo199206/KeyboardState/main/device-2021-02-03-164444.gif" width="300" heght="500" align=center />


#### 依赖
+ 添加maven仓库配置到项目根目录gradle文件下（注意不是具体模块下）

```
allprojects {
    repositories {
        maven { url "https://jcenter.bintray.com" }
    }
}
```

+ 添加以下maven依赖配置到app模块，gradle文件下

```
implementation  'com.jlertele.keyboard:KeyboardState:1.0.0'
```
