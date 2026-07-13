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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * NeoBukkit 统一 API 接口 - 不依赖任何服务端特有类
 */
public interface NeoBukkitAPI {

    // ==================== 服务器信息 ====================
    String getServerVersion();
    String getServerName();
    String getMotd();
    int getMaxPlayers();
    List<? extends Player> getOnlinePlayers();
    double[] getTPS();
    long getCurrentTick();
    boolean isLagging();

    // ==================== 调度器（基础） ====================
    void runAsync(Runnable task);
    BukkitTask runSync(Runnable task);
    BukkitTask runSyncLater(Runnable task, long delayTicks);
    BukkitTask runSyncTimer(Runnable task, long delayTicks, long periodTicks);

    // ==================== 玩家基础 ====================
    Player getPlayer(String name);
    Player getPlayer(UUID uuid);
    void broadcastMessage(String message);
    void sendActionBar(Player player, String message);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    boolean isPrimaryThread();

    // ==================== 世界基础 ====================
    World getWorld(String name);
    World getWorld(UUID uuid);
    List<? extends World> getWorlds();
    float getLocalDifficultyAt(Location location);

    // ==================== 插件工具 ====================
    Plugin getPlugin();
    void setPlugin(Plugin plugin);

    // ==================== Purpur Server API ====================
    void addFuel(Material material, int burnTime);
    void removeFuel(Material material);

    // ==================== Purpur Player API ====================
    boolean isAfk(Player player);
    void setAfk(Player player, boolean afk);
    void resetIdleTimer(Player player);
    void resetIdleDuration(Player player);
    void sendBlockHighlight(Player player, Location location, int duration);
    void sendBlockHighlight(Player player, Location location, int duration, int argb);
    void sendBlockHighlight(Player player, Location location, int duration, String text);
    void sendBlockHighlight(Player player, Location location, int duration, String text, int argb);
    void sendBlockHighlight(Player player, Location location, int duration, Color color, int transparency);
    void sendBlockHighlight(Player player, Location location, int duration, String text, Color color, int transparency);
    void clearBlockHighlights(Player player);
    boolean usesPurpurClient(Player player);
    boolean teleportOffline(Player player, Location location);

    // ==================== Purpur Entity API ====================
    boolean isFireImmune(Entity entity);
    void setFireImmune(Entity entity, boolean fireImmune);
    boolean shouldBurnInDay(LivingEntity entity);
    void setShouldBurnInDay(LivingEntity entity, boolean shouldBurnInDay);
    boolean isInDaylight(LivingEntity entity);
    Entity getRider(Entity entity);
    boolean hasRider(Entity entity);
    boolean isRidable(Entity entity);
    boolean isRidableInWater(Entity entity);
    double getRidableMaxY(Entity entity);
    void setRidableMaxY(Entity entity, double maxY);

    // ==================== Purpur Boss API ====================
    UUID getWitherSummoner(Entity entity);
    void setWitherSummoner(Entity entity, UUID summoner);
    boolean isRavagerGriefable(Entity entity);
    void setRavagerGriefable(Entity entity, boolean griefable);

    // ==================== Purpur Attributes API ====================
    double getEntityAttribute(Entity entity, Attribute attribute);
    void setEntityAttribute(Entity entity, Attribute attribute, double value);

    // ==================== Purpur ItemStack API ====================
    String getDisplayName(ItemStack item);
    void setDisplayName(ItemStack item, String name);
    boolean hasDisplayName(ItemStack item);
    String getLocalizedName(ItemStack item);
    void setLocalizedName(ItemStack item, String name);
    boolean hasLocalizedName(ItemStack item);
    boolean hasLore(ItemStack item);
    int getCustomModelData(ItemStack item);
    void setCustomModelData(ItemStack item, Integer data);
    boolean hasCustomModelData(ItemStack item);
    boolean hasEnchant(ItemStack item, Enchantment enchantment);
    int getEnchantLevel(ItemStack item, Enchantment enchantment);
    Map<Enchantment, Integer> getEnchants(ItemStack item);
    boolean addEnchant(ItemStack item, Enchantment enchantment, int level, boolean ignoreLevelRestriction);
    boolean removeEnchant(ItemStack item, Enchantment enchantment);
    boolean hasEnchants(ItemStack item);
    boolean hasConflictingEnchant(ItemStack item, Enchantment enchantment);
    boolean hasAttributeModifiers(ItemStack item);
    Collection<AttributeModifier> getAttributeModifiers(ItemStack item, Attribute attribute);
    com.google.common.collect.Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item);
    com.google.common.collect.Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item, EquipmentSlot slot);
    boolean addAttributeModifier(ItemStack item, Attribute attribute, AttributeModifier modifier);

    // ==================== Purpur StoredEntity API（全部用 Object） ====================
    Object getStoredEntity(Block block, Class<? extends Entity> entityClass);
    Object getFirstStoredEntity(Block block);
    List<Object> getAllStoredEntities(Block block);
    Entity releaseStoredEntity(Object storedEntity);
    EntityType getStoredEntityType(Object storedEntity);
    String getStoredEntityCustomName(Object storedEntity);
    void setStoredEntityCustomName(Object storedEntity, String name);
    Component getStoredEntityCustomNameComponent(Object storedEntity);
    void setStoredEntityCustomNameComponent(Object storedEntity, Component name);
    PersistentDataContainer getStoredEntityData(Object storedEntity);
    void updateStoredEntity(Object storedEntity);

    // ==================== Purpur Tooltip API ====================
    Object createTooltipContext(boolean advanced, boolean creative);
    List<Component> computeTooltip(ItemStack item, Object tooltipContext, Player player);

    // ==================== Purpur UI Bars ====================
    void showTpsBar(Player player);
    void showRamBar(Player player);
    void hideTpsBar(Player player);
    void hideRamBar(Player player);

    // ==================== Folia 区域调度器 API ====================
    Object getRegionScheduler();
    void runAtRegion(Location location, Runnable task);
    void runAtRegionLater(Location location, Runnable task, long delayTicks);
    void runAtRegionTimer(Location location, Runnable task, long initialDelayTicks, long periodTicks);
    Object getEntityScheduler(Entity entity);
    void runAtEntity(Entity entity, Runnable task);
    void runAtEntityLater(Entity entity, Runnable task, long delayTicks);
    Object getGlobalRegionScheduler();
    void runGlobal(Runnable task);
    Object getAsyncScheduler();
    void runAsyncNow(Runnable task);

    // ==================== Folia 区域 TPS 监控 ====================
    double[] getRegionTPS(Location location);
    double[] getRegionTPS(Chunk chunk);
    double[] getRegionTPS(World world, int chunkX, int chunkZ);

    // ==================== 环境检测 ====================
    boolean isFolia();
    boolean isPaper();
    boolean isPurpur();
    boolean isNeoForge();
}