# KeyboardState
键盘状态监听工具类

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
