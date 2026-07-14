# NeoBukkit



[![版本](https://img.shields.io/badge/版本-0.1.0--beta-blue)](https://github.com/CookieX-a/NeoBukkitMC)

[![构建](https://img.shields.io/badge/构建-通过-brightgreen)]()

[![许可证](https://img.shields.io/badge/许可证-MIT-green)](LICENSE)

[![Java](https://img.shields.io/badge/Java-21-orange)]()



一个统一、自实现的 Spigot 服务端 API 库。





## 项目简介



NeoBukkit 是一个运行于 Spigot / Paper / Purpur / Folia 服务端的统一 API 库。



它为插件开发者提供了一套完整、自实现、不依赖服务端特有类的统一 API。所有功能都由 NeoBukkit 自己实现，而不是依赖服务端提供。真正做到一次编译，处处运行。





## 核心目标



- 一次编译，处处运行 — 一个 JAR 包支持 Spigot / Paper / Purpur / Folia

- 自实现，不依赖服务端特有类 — 全部自己实现，不反射调用 Paper/Purpur 特有 API

- 统一 API，按平台分组 — API.Core / API.Purpur / API.Folia，清晰明了

- 兼容未来版本 — 不依赖任何可能被移除的 API





## 功能特性



### Core API（所有服务端通用）



| 功能 | 说明 |

|------|------|

| 服务器信息 | 名称、版本、MOTD、最大玩家数 |

| 玩家操作 | 获取玩家、广播消息、ActionBar、Title |

| 世界操作 | 获取世界、世界列表 |

| 调度器 | 异步、同步、延迟、定时任务 |

| 环境检测 | 判断当前运行环境（Folia / Paper / Purpur） |



### Purpur API



| 功能 | 说明 |

|------|------|

| TPS 监控 | 获取服务器 TPS（1m / 5m / 15m） |

| 卡顿检测 | 自动判断服务器是否卡顿 |

| AFK 管理 | 玩家 AFK 状态查询与设置 |

| 实体增强 | 火焰免疫、日光燃烧、骑乘系统 |

| Block Highlight | 方块高亮标记 |

| StoredEntity | 方块实体存储管理 |

| 物品增强 | 显示名、自定义模型数据、属性修饰符 |

| UI Bars | TPS / RAM 状态条 |

| 燃料系统 | 动态注册/移除熔炉燃料 |

| 区域难度 | 获取指定位置的区域难度 |

| Boss API | 凋灵召唤者、劫掠兽破坏控制 |

| Tooltip | 物品提示框上下文计算 |



### Folia API（区域化调度）



| 功能 | 说明 |

|------|------|

| 区域调度 | runAtRegion / runAtRegionLater / runAtRegionTimer |

| 实体调度 | runAtEntity / runAtEntityLater |

| 全局调度 | runGlobal |

| 异步调度 | runAsyncNow |

| 区域 TPS | 获取指定位置/区块的区域 TPS |





## 使用示例



```java

import x.cookie.NeoBukkit.API;



public class MyPlugin extends JavaPlugin {

&#x20;   @Override

&#x20;   public void onEnable() {

&#x20;       // 初始化 NeoBukkit

&#x20;       API.Core.setPlugin(this);



&#x20;       // 获取服务器信息

&#x20;       String serverName = API.Core.getServerName();

&#x20;       int playerCount = API.Core.getOnlinePlayers().size();



&#x20;       // 获取 TPS

&#x20;       double[] tps = API.Purpur.getTPS();

&#x20;       getLogger().info("TPS: " + tps[0]);



&#x20;       // 异步任务（自动适配 Folia/Paper/Bukkit）

&#x20;       API.Core.runAsync(() -> {

&#x20;           // 耗时操作

&#x20;       });



&#x20;       // Folia 区域调度（非 Folia 环境自动降级）

&#x20;       API.Folia.runAtRegion(player.getLocation(), () -> {

&#x20;           // 在区域线程执行

&#x20;       });

&#x20;   }

}

```





## 下载与使用



从 [Releases](https://github.com/CookieX-a/NeoBukkitMC/releases) 页面下载 `NeoBukkit-0.1.0-beta.jar`，放入服务端 `plugins/` 目录即可。



### 通过 JitPack 引用（可选）



**Gradle**:

```gradle

repositories {

&#x20;   maven { url 'https://jitpack.io' }

}

dependencies {

&#x20;   compileOnly 'com.github.CookieX-a:NeoBukkitMC:0.1.0-beta'

}

```



**Maven**:

```xml

<repository>

&#x20;   <id>jitpack</id>

&#x20;   <url>https://jitpack.io</url>

</repository>

<dependency>

&#x20;   <groupId>com.github.CookieX-a</groupId>

&#x20;   <artifactId>NeoBukkitMC</artifactId>

&#x20;   <version>0.1.0-beta</version>

&#x20;   <scope>provided</scope>

</dependency>

```





## 编译



```bash

git clone https://github.com/CookieX-a/NeoBukkitMC.git

cd NeoBukkit

./gradlew clean build

# 输出: build/libs/NeoBukkit-0.1.0-beta.jar

```





## 插件依赖



在你的 `plugin.yml` 中声明：



```yaml

depend: [NeoBukkit]

```





## 系统要求



- Java 21 或更高版本

- 服务端: Spigot / Paper / Purpur / Folia

- Minecraft 26.1.2 或更高版本





## 许可证



MIT License