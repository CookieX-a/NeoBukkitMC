# NeoBukkit

[![版本](https://img.shields.io/badge/版本-0.1.0--beta-blue)](https://github.com/CookieX-a/NeoBukkitMC)
[![构建](https://img.shields.io/badge/构建-通过-brightgreen)]
[![许可证](https://img.shields.io/badge/许可证-MIT-green)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange]

> 一个统一、自实现的 Spigot 服务端 API 库

---

## 📖 项目简介

**NeoBukkit** 是一个运行于 Spigot / Paper / Purpur / Folia 服务端的 **统一 API 库**。

它为插件开发者提供了一套 **完整、自实现、不依赖服务端特有类** 的统一 API。所有功能都由 NeoBukkit 自己实现，而不是依赖服务端提供——真正做到 **一次编译，处处运行**。

无论你的插件部署在 Spigot、Paper、Purpur 还是 Folia 上，NeoBukkit 都能确保所有 API 调用正常工作。

---

## 🎯 核心目标

- ✅ **一次编译，处处运行** — 一个 JAR 包支持 Spigot / Paper / Purpur / Folia
- ✅ **自实现，不依赖服务端特有类** — 不使用反射调用 Paper/Purpur 特有 API，全部自己实现
- ✅ **统一 API，按平台分组** — `API.Core` / `API.Purpur` / `API.Folia`，清晰明了
- ✅ **兼容未来版本** — 不依赖任何可能被移除的 API

---

## ✨ 功能特性

### 🔹 Core API（所有服务端通用）

| 功能 | 说明 |
|------|------|
| 服务器信息 | 名称、版本、MOTD、最大玩家数 |
| 玩家操作 | 获取玩家、广播消息、ActionBar、Title |
| 世界操作 | 获取世界、世界列表 |
| 调度器 | 异步、同步、延迟、定时任务 |
| 环境检测 | 判断当前运行环境（Folia / Paper / Purpur） |

### 🔸 Purpur API（增强功能，完全自实现）

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

### 🔹 Folia API（区域化调度，完全自实现）

| 功能 | 说明 |
|------|------|
| 区域调度 | `runAtRegion` / `runAtRegionLater` / `runAtRegionTimer` |
| 实体调度 | `runAtEntity` / `runAtEntityLater` |
| 全局调度 | `runGlobal` |
| 异步调度 | `runAsyncNow` |
| 区域 TPS | 获取指定位置/区块的区域 TPS |

---

## 📁 项目结构

```
x.cookie.NeoBukkit/
├── API.java                    # 统一入口（按平台分组）
│   ├── API.Core                # 所有服务端通用
│   ├── API.Purpur              # Purpur 增强功能
│   └── API.Folia               # Folia 区域调度
├── NeoBukkitAPI.java           # 统一接口
├── NeoBukkitPlugin.java        # 插件主类
├── impl/
│   ├── PurpurUnifiedImpl.java  # Purpur 全部 API 实现（完全自实现）
│   └── FoliaUnifiedImpl.java   # Folia 特有扩展（区域调度 + 区域 TPS）
└── internal/
    ├── ServiceDetector.java    # 环境检测
    └── TpsTracker.java         # TPS 计算器
```

---

## 🚀 使用示例

### 基础用法

```java
import x.cookie.NeoBukkit.API;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // 初始化 NeoBukkit
        API.Core.setPlugin(this);

        // 获取服务器信息
        String serverName = API.Core.getServerName();
        int playerCount = API.Core.getOnlinePlayers().size();

        // 获取 TPS
        double[] tps = API.Purpur.getTPS();
        getLogger().info("TPS: " + tps[0]);

        // 异步任务（自动适配 Folia/Paper/Bukkit）
        API.Core.runAsync(() -> {
            // 耗时操作
        });

        // Folia 区域调度（非 Folia 环境自动降级）
        API.Folia.runAtRegion(player.getLocation(), () -> {
            // 在区域线程执行
        });
    }
}
```

### 按平台分类调用

```java
// Core API（所有服务端通用）
API.Core.getServerName();
API.Core.runAsync(() -> {});
API.Core.broadcastMessage("Hello!");

// Purpur API（增强功能，完全自实现）
API.Purpur.getTPS();
API.Purpur.isAfk(player);
API.Purpur.addFuel(Material.COAL, 3000);

// Folia API（区域调度，非 Folia 环境自动降级）
API.Folia.runAtRegion(location, () -> {});
API.Folia.getRegionTPS(location);
```

---

## 📦 依赖配置

### Gradle

```gradle
repositories {
    maven {
        url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'
    }
}

dependencies {
    compileOnly 'x.cookie:NeoBukkit:0.1.0-beta'
}
```

### Maven

```xml
<dependency>
    <groupId>x.cookie</groupId>
    <artifactId>NeoBukkit</artifactId>
    <version>0.1.0-beta</version>
    <scope>provided</scope>
</dependency>
```

---

## 🛠️ 编译与构建

```bash
# 克隆项目
git clone https://github.com/CookieX-a/NeoBukkitMC.git

# 进入目录
cd NeoBukkit

# 编译打包
./gradlew clean build

# 输出位置
build/libs/NeoBukkit-0.1.0-beta.jar
```

---

## 📄 插件依赖

在你的 `plugin.yml` 中声明依赖：

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.MyPlugin
api-version: "1.21"
depend: [NeoBukkit]
```

---

## 🔧 系统要求

| 项目 | 要求 |
|------|------|
| Java | 21 或更高版本 |
| 服务端 | Spigot / Paper / Purpur / Folia |
| Minecraft | 26.1.2 或更高版本 |

---

## 📚 文档与支持

- 📖 **API 文档**：Javadoc 已包含在发布的 JAR 中
- 📦 **源码**：`NeoBukkit-0.1.0-beta-sources.jar`
- 🐛 **问题反馈**：[GitHub Issues](https://github.com/CookieX-a/NeoBukkitMC/issues)

---

## 📝 许可证

MIT License

---

**NeoBukkit** — 让插件开发不再受服务端限制。一次编写，处处运行。 🚀