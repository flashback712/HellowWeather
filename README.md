## HellowWeather

### 演示图

<p align="center"><img src="https://github.com/flashback712/HellowWeather/blob/main/demo.gif" width="280" height="550"/></p>

### 项目亮点
- HellowWeather本身是一款简洁易用的Android app，支持搜索地点记忆以及定位自动获取天气，尽管功能简单，但麻雀虽小，五脏俱全，非常适合入门实战

    - 架构清晰：严格遵循MVVM架构+Repository架构来进行编码
    - 模块划分：尽管项目本身不大，但仍将其划分为了两个module，一个为app主module，该module主要来存放应用的核心代码，如Activity，ViewModel，另一个是app_data子module，如其名所示，该module主要存放model类以及获取网络数据的相关类
    - 在企业开发中，往往都是多module进行开发的，而不是将全部代码都放至一个app主module里，这么做的好处是     module可以在不同应用中共享，但这同时也会带来一定的不便，比如说子类module无法获取到主module的类，再比如说如何统一管理多个module的第三方库依赖，HellowWeather作为一个小应用，用来提前熟悉一下企业的开发模式是再恰当不过
    - 主流第三方库的使用
        - Retrofit：Retrofit作为目前安卓主流的网络获取库，其重要性不言而喻，HellowWeather可以很好演示如何使用Retrofit从后台获取数据
        - Dagger：Dagger是目前主流的依赖注入库，但Dagger本身就有匕首的意思，匕首可御敌，使用不当也会伤到自己，Dagger依赖注入库也是这个理，使用恰当可以减少很多初始化的代码，但其使用难度不小，此外使用不当也会使编码得更加复杂，HellowWeather就很好演示了如何使用Dagger进行依赖注入，比如说如何向Application，Activity这种Dagger无法直接初始化的类进行依赖注入。如何搭配Retrofit网络库来获取网络数据更加优雅～
    - 选用sdk31，对Android新特性例如livedata监听、kotlin携程进行实战演示，让你的知识快人一步
    - 良好的编码风格：在如今的开发过程中，并不是说人和项目只要有一个能跑就行，而是需要项目具有一定的可维护性，而良好的代码风格正是可维护性基本要求之一，下面代码截取至MainActivity文件。

``` kotlin
    private fun init() {
        initLocationFunctions()
        if (permissionForLocationIsGranted()) {
            startLocateUser()
        } else {
            val requestCallback = RequestCallback { permissionForLocationIsGranted, _, _ ->
                if (permissionForLocationIsGranted) {
                    startLocateUser()
                } else {
                    doNothing()
                }
            }
            requestLocationPermission(requestCallback)
        }
        getCurrentLocationCombineWeatherInfo()
    }
```

    init()方法负责执行初始化工作，即使你不知道这个应用的基本信息，从代码里大致也能推断出下列程序逻辑。

    - 先初始化定位功能。

    - 判断是否有定位权限。

    - 有。直接定位用户。

    - 无。去请求定位权限，当用户同意时再进行定位。
    
    - 根据定位获取天气功能。

从这上面三点就可以进一步推出这个应用至少有获取用户所在城市的天气信息的功能，这样的代码在项目还有很多处，这里不一一列举了。

### 其它信息

#### 数据来源

- 天气数据来源于<a href = "https://caiyunapp.com/api/weather">彩云天气</a>，由于采用的是该网站的免费API，有如下特点：
    - 支持共100000次调用。
    - 每20秒刷新一次
    - 只能获取未来15天的天气预报(包括今天)
    - 支持QS8
- 地图定位
    - 植入了百度地图SDK来实现定位功能
- 第三方开源库
    - 除了比较常见开源库Retrofit，Dagger外，列举一些不小众强大的第三方库
    - 获取定位权限，PermissionX
    - 使用地址选择器库AndroidPicker
    - 外观ui设计，Material Design

#### 参考

<a href = "https://blog.csdn.net/guolin_blog/article/details/105233078">《第一行代码》</a>