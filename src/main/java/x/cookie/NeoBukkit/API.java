package x.cookie.NeoBukkit;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import x.cookie.NeoBukkit.impl.FoliaUnifiedImpl;
import x.cookie.NeoBukkit.impl.PurpurUnifiedImpl;
import x.cookie.NeoBukkit.internal.ServiceDetector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * NeoBukkit 统一 API 入口
 * 按平台分组：Core、Purpur、Folia
 */
public final class API {

    private static final NeoBukkitAPI IMPL;

    static {
        if (ServiceDetector.isFolia()) {
            IMPL = new FoliaUnifiedImpl();
        } else {
            IMPL = new PurpurUnifiedImpl();
        }
    }

    // ==================== Core API（所有服务端通用） ====================
    public static final class Core {

        // ---- 服务器信息 ----
        public static String getServerVersion() { return IMPL.getServerVersion(); }
        public static String getServerName() { return IMPL.getServerName(); }
        public static String getMotd() { return IMPL.getMotd(); }
        public static int getMaxPlayers() { return IMPL.getMaxPlayers(); }
        public static List<? extends Player> getOnlinePlayers() { return IMPL.getOnlinePlayers(); }
        public static long getCurrentTick() { return IMPL.getCurrentTick(); }
        public static boolean isPrimaryThread() { return IMPL.isPrimaryThread(); }

        // ---- 调度器 ----
        public static void runAsync(Runnable task) { IMPL.runAsync(task); }
        public static BukkitTask runSync(Runnable task) { return IMPL.runSync(task); }
        public static BukkitTask runSyncLater(Runnable task, long delayTicks) { return IMPL.runSyncLater(task, delayTicks); }
        public static BukkitTask runSyncTimer(Runnable task, long delayTicks, long periodTicks) { return IMPL.runSyncTimer(task, delayTicks, periodTicks); }

        // ---- 玩家基础 ----
        public static Player getPlayer(String name) { return IMPL.getPlayer(name); }
        public static Player getPlayer(UUID uuid) { return IMPL.getPlayer(uuid); }
        public static void broadcastMessage(String message) { IMPL.broadcastMessage(message); }
        public static void sendActionBar(Player player, String message) { IMPL.sendActionBar(player, message); }
        public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
            IMPL.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }

        // ---- 世界基础 ----
        public static World getWorld(String name) { return IMPL.getWorld(name); }
        public static World getWorld(UUID uuid) { return IMPL.getWorld(uuid); }
        public static List<? extends World> getWorlds() { return IMPL.getWorlds(); }

        // ---- 插件工具 ----
        public static Plugin getPlugin() { return IMPL.getPlugin(); }
        public static void setPlugin(Plugin plugin) { IMPL.setPlugin(plugin); }

        // ---- 环境检测 ----
        public static boolean isFolia() { return IMPL.isFolia(); }
        public static boolean isPaper() { return IMPL.isPaper(); }
        public static boolean isPurpur() { return IMPL.isPurpur(); }
        public static boolean isNeoForge() { return IMPL.isNeoForge(); }

        private Core() {}
    }

    // ==================== Purpur 特有 API ====================
    public static final class Purpur {

        // ---- Server ----
        public static boolean isLagging() { return IMPL.isLagging(); }
        public static void addFuel(Material material, int burnTime) { IMPL.addFuel(material, burnTime); }
        public static void removeFuel(Material material) { IMPL.removeFuel(material); }

        // ---- Player ----
        public static boolean isAfk(Player player) { return IMPL.isAfk(player); }
        public static void setAfk(Player player, boolean afk) { IMPL.setAfk(player, afk); }
        public static void resetIdleTimer(Player player) { IMPL.resetIdleTimer(player); }
        public static void resetIdleDuration(Player player) { IMPL.resetIdleDuration(player); }
        public static void sendBlockHighlight(Player player, Location location, int duration) { IMPL.sendBlockHighlight(player, location, duration); }
        public static void sendBlockHighlight(Player player, Location location, int duration, int argb) { IMPL.sendBlockHighlight(player, location, duration, argb); }
        public static void sendBlockHighlight(Player player, Location location, int duration, String text) { IMPL.sendBlockHighlight(player, location, duration, text); }
        public static void sendBlockHighlight(Player player, Location location, int duration, String text, int argb) { IMPL.sendBlockHighlight(player, location, duration, text, argb); }
        public static void sendBlockHighlight(Player player, Location location, int duration, Color color, int transparency) { IMPL.sendBlockHighlight(player, location, duration, color, transparency); }
        public static void sendBlockHighlight(Player player, Location location, int duration, String text, Color color, int transparency) { IMPL.sendBlockHighlight(player, location, duration, text, color, transparency); }
        public static void clearBlockHighlights(Player player) { IMPL.clearBlockHighlights(player); }
        public static boolean usesPurpurClient(Player player) { return IMPL.usesPurpurClient(player); }
        public static boolean teleportOffline(Player player, Location location) { return IMPL.teleportOffline(player, location); }

        // ---- Entity ----
        public static boolean isFireImmune(Entity entity) { return IMPL.isFireImmune(entity); }
        public static void setFireImmune(Entity entity, boolean fireImmune) { IMPL.setFireImmune(entity, fireImmune); }
        public static boolean shouldBurnInDay(LivingEntity entity) { return IMPL.shouldBurnInDay(entity); }
        public static void setShouldBurnInDay(LivingEntity entity, boolean shouldBurnInDay) { IMPL.setShouldBurnInDay(entity, shouldBurnInDay); }
        public static boolean isInDaylight(LivingEntity entity) { return IMPL.isInDaylight(entity); }
        public static Entity getRider(Entity entity) { return IMPL.getRider(entity); }
        public static boolean hasRider(Entity entity) { return IMPL.hasRider(entity); }
        public static boolean isRidable(Entity entity) { return IMPL.isRidable(entity); }
        public static boolean isRidableInWater(Entity entity) { return IMPL.isRidableInWater(entity); }
        public static double getRidableMaxY(Entity entity) { return IMPL.getRidableMaxY(entity); }
        public static void setRidableMaxY(Entity entity, double maxY) { IMPL.setRidableMaxY(entity, maxY); }

        // ---- Boss ----
        public static UUID getWitherSummoner(Entity entity) { return IMPL.getWitherSummoner(entity); }
        public static void setWitherSummoner(Entity entity, UUID summoner) { IMPL.setWitherSummoner(entity, summoner); }
        public static boolean isRavagerGriefable(Entity entity) { return IMPL.isRavagerGriefable(entity); }
        public static void setRavagerGriefable(Entity entity, boolean griefable) { IMPL.setRavagerGriefable(entity, griefable); }

        // ---- Attributes ----
        public static double getEntityAttribute(Entity entity, Attribute attribute) { return IMPL.getEntityAttribute(entity, attribute); }
        public static void setEntityAttribute(Entity entity, Attribute attribute, double value) { IMPL.setEntityAttribute(entity, attribute, value); }

        // ---- ItemStack ----
        public static String getDisplayName(ItemStack item) { return IMPL.getDisplayName(item); }
        public static void setDisplayName(ItemStack item, String name) { IMPL.setDisplayName(item, name); }
        public static boolean hasDisplayName(ItemStack item) { return IMPL.hasDisplayName(item); }
        public static String getLocalizedName(ItemStack item) { return IMPL.getLocalizedName(item); }
        public static void setLocalizedName(ItemStack item, String name) { IMPL.setLocalizedName(item, name); }
        public static boolean hasLocalizedName(ItemStack item) { return IMPL.hasLocalizedName(item); }
        public static boolean hasLore(ItemStack item) { return IMPL.hasLore(item); }
        public static int getCustomModelData(ItemStack item) { return IMPL.getCustomModelData(item); }
        public static void setCustomModelData(ItemStack item, Integer data) { IMPL.setCustomModelData(item, data); }
        public static boolean hasCustomModelData(ItemStack item) { return IMPL.hasCustomModelData(item); }
        public static boolean hasEnchant(ItemStack item, Enchantment enchantment) { return IMPL.hasEnchant(item, enchantment); }
        public static int getEnchantLevel(ItemStack item, Enchantment enchantment) { return IMPL.getEnchantLevel(item, enchantment); }
        public static Map<Enchantment, Integer> getEnchants(ItemStack item) { return IMPL.getEnchants(item); }
        public static boolean addEnchant(ItemStack item, Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
            return IMPL.addEnchant(item, enchantment, level, ignoreLevelRestriction);
        }
        public static boolean removeEnchant(ItemStack item, Enchantment enchantment) { return IMPL.removeEnchant(item, enchantment); }
        public static boolean hasEnchants(ItemStack item) { return IMPL.hasEnchants(item); }
        public static boolean hasConflictingEnchant(ItemStack item, Enchantment enchantment) { return IMPL.hasConflictingEnchant(item, enchantment); }
        public static boolean hasAttributeModifiers(ItemStack item) { return IMPL.hasAttributeModifiers(item); }
        public static Collection<AttributeModifier> getAttributeModifiers(ItemStack item, Attribute attribute) {
            return IMPL.getAttributeModifiers(item, attribute);
        }
        public static com.google.common.collect.Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item) {
            return IMPL.getAttributeModifiers(item);
        }
        public static com.google.common.collect.Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item, EquipmentSlot slot) {
            return IMPL.getAttributeModifiers(item, slot);
        }
        public static boolean addAttributeModifier(ItemStack item, Attribute attribute, AttributeModifier modifier) {
            return IMPL.addAttributeModifier(item, attribute, modifier);
        }

        // ---- StoredEntity ----
        public static Object getStoredEntity(Block block, Class<? extends Entity> entityClass) {
            return IMPL.getStoredEntity(block, entityClass);
        }
        public static Object getFirstStoredEntity(Block block) { return IMPL.getFirstStoredEntity(block); }
        public static List<Object> getAllStoredEntities(Block block) { return IMPL.getAllStoredEntities(block); }
        public static Entity releaseStoredEntity(Object storedEntity) { return IMPL.releaseStoredEntity(storedEntity); }
        public static EntityType getStoredEntityType(Object storedEntity) { return IMPL.getStoredEntityType(storedEntity); }
        public static String getStoredEntityCustomName(Object storedEntity) { return IMPL.getStoredEntityCustomName(storedEntity); }
        public static void setStoredEntityCustomName(Object storedEntity, String name) { IMPL.setStoredEntityCustomName(storedEntity, name); }
        public static Component getStoredEntityCustomNameComponent(Object storedEntity) { return IMPL.getStoredEntityCustomNameComponent(storedEntity); }
        public static void setStoredEntityCustomNameComponent(Object storedEntity, Component name) { IMPL.setStoredEntityCustomNameComponent(storedEntity, name); }
        public static PersistentDataContainer getStoredEntityData(Object storedEntity) { return IMPL.getStoredEntityData(storedEntity); }
        public static void updateStoredEntity(Object storedEntity) { IMPL.updateStoredEntity(storedEntity); }

        // ---- Tooltip ----
        public static Object createTooltipContext(boolean advanced, boolean creative) { return IMPL.createTooltipContext(advanced, creative); }
        public static List<Component> computeTooltip(ItemStack item, Object tooltipContext, Player player) {
            return IMPL.computeTooltip(item, tooltipContext, player);
        }

        // ---- UI Bars ----
        public static void showTpsBar(Player player) { IMPL.showTpsBar(player); }
        public static void showRamBar(Player player) { IMPL.showRamBar(player); }
        public static void hideTpsBar(Player player) { IMPL.hideTpsBar(player); }
        public static void hideRamBar(Player player) { IMPL.hideRamBar(player); }

        // ---- TPS ----
        public static double[] getTPS() { return IMPL.getTPS(); }

        // ---- World ----
        public static float getLocalDifficultyAt(Location location) { return IMPL.getLocalDifficultyAt(location); }

        private Purpur() {}
    }

    // ==================== Folia 特有 API ====================
    public static final class Folia {

        // ---- 区域调度器 ----
        public static Object getRegionScheduler() { return IMPL.getRegionScheduler(); }
        public static void runAtRegion(Location location, Runnable task) { IMPL.runAtRegion(location, task); }
        public static void runAtRegionLater(Location location, Runnable task, long delayTicks) { IMPL.runAtRegionLater(location, task, delayTicks); }
        public static void runAtRegionTimer(Location location, Runnable task, long initialDelayTicks, long periodTicks) {
            IMPL.runAtRegionTimer(location, task, initialDelayTicks, periodTicks);
        }

        // ---- 实体调度器 ----
        public static Object getEntityScheduler(Entity entity) { return IMPL.getEntityScheduler(entity); }
        public static void runAtEntity(Entity entity, Runnable task) { IMPL.runAtEntity(entity, task); }
        public static void runAtEntityLater(Entity entity, Runnable task, long delayTicks) { IMPL.runAtEntityLater(entity, task, delayTicks); }

        // ---- 全局调度器 ----
        public static Object getGlobalRegionScheduler() { return IMPL.getGlobalRegionScheduler(); }
        public static void runGlobal(Runnable task) { IMPL.runGlobal(task); }

        // ---- 异步调度器 ----
        public static Object getAsyncScheduler() { return IMPL.getAsyncScheduler(); }
        public static void runAsyncNow(Runnable task) { IMPL.runAsyncNow(task); }

        // ---- 区域 TPS ----
        public static double[] getRegionTPS(Location location) { return IMPL.getRegionTPS(location); }
        public static double[] getRegionTPS(Chunk chunk) { return IMPL.getRegionTPS(chunk); }
        public static double[] getRegionTPS(World world, int chunkX, int chunkZ) { return IMPL.getRegionTPS(world, chunkX, chunkZ); }

        private Folia() {}
    }

    private API() {}
}