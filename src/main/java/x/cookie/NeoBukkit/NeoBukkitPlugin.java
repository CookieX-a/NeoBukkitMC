package x.cookie.NeoBukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class NeoBukkitPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        API.Core.setPlugin(this);

        getLogger().info("NeoBukkit v0.1.0-beta loaded!");
        getLogger().info("Server: " + API.Core.getServerName());
        getLogger().info("TPS: " + API.Purpur.getTPS()[0]);
    }

    @Override
    public void onDisable() {
        getLogger().info("NeoBukkit unloaded");
    }
}