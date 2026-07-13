package x.cookie.NeoBukkit.impl;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import x.cookie.NeoBukkit.NeoBukkitAPI;
import x.cookie.NeoBukkit.internal.ServiceDetector;
import x.cookie.NeoBukkit.internal.TpsTracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"deprecation", "unchecked"})
public class PurpurUnifiedImpl implements NeoBukkitAPI {

    private static Plugin plugin;
    private final TpsTracker tpsTracker = new TpsTracker();
    private final ExecutorService asyncExecutor = Executors.newCachedThreadPool();

    private String cachedServerName = null;
    private String cachedMotd = null;

    private final Map<UUID, Boolean> afkStatus = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> fireImmuneCache = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> burnInDayCache = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> riderCache = new ConcurrentHashMap<>();
    private final Map<UUID, Double> ridableMaxYCache = new ConcurrentHashMap<>();
    private final Map<String, Double> entityAttributeCache = new ConcurrentHashMap<>();
    private final Map<Material, Integer> fuelRegistry = new ConcurrentHashMap<>();
    private final Map<UUID, BossBar> tpsBars = new ConcurrentHashMap<>();
    private final Map<UUID, BossBar> ramBars = new ConcurrentHashMap<>();

    public PurpurUnifiedImpl() {
        fuelRegistry.put(Material.COAL, 1600);
        fuelRegistry.put(Material.CHARCOAL, 1600);
        fuelRegistry.put(Material.OAK_LOG, 300);
        fuelRegistry.put(Material.BIRCH_LOG, 300);
        fuelRegistry.put(Material.SPRUCE_LOG, 300);
        fuelRegistry.put(Material.JUNGLE_LOG, 300);
        fuelRegistry.put(Material.ACACIA_LOG, 300);
        fuelRegistry.put(Material.DARK_OAK_LOG, 300);
        fuelRegistry.put(Material.MANGROVE_LOG, 300);
        fuelRegistry.put(Material.CHERRY_LOG, 300);
        fuelRegistry.put(Material.OAK_PLANKS, 150);
        fuelRegistry.put(Material.BIRCH_PLANKS, 150);
        fuelRegistry.put(Material.SPRUCE_PLANKS, 150);
        fuelRegistry.put(Material.JUNGLE_PLANKS, 150);
        fuelRegistry.put(Material.ACACIA_PLANKS, 150);
        fuelRegistry.put(Material.DARK_OAK_PLANKS, 150);
        fuelRegistry.put(Material.MANGROVE_PLANKS, 150);
        fuelRegistry.put(Material.CHERRY_PLANKS, 150);
        fuelRegistry.put(Material.LAVA_BUCKET, 20000);
    }

    // ==================== 服务器信息 ====================
    @Override
    public String getServerVersion() {
        return Bukkit.getServer().getVersion();
    }

    @Override
    public String getServerName() {
        if (cachedServerName != null) return cachedServerName;
        try {
            File file = new File("server.properties");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("server-name=")) {
                            cachedServerName = line.substring(12).trim();
                            return cachedServerName;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        cachedServerName = "Unknown Server";
        return cachedServerName;
    }

    @Override
    public String getMotd() {
        if (cachedMotd != null) return cachedMotd;
        try {
            File file = new File("server.properties");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("motd=")) {
                            cachedMotd = line.substring(5).trim();
                            return cachedMotd;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        cachedMotd = "A Minecraft Server";
        return cachedMotd;
    }

    @Override
    public int getMaxPlayers() {
        try {
            File file = new File("server.properties");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("max-players=")) {
                            return Integer.parseInt(line.substring(12).trim());
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return 20;
    }

    @Override
    public List<? extends Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public double[] getTPS() {
        if (ServiceDetector.isPurpur()) {
            try {
                Class<?> cls = Class.forName("org.purpurmc.purpur.PurpurConfig");
                Method m = cls.getMethod("getTPS");
                return (double[]) m.invoke(null);
            } catch (Exception ignored) {}
        }
        return tpsTracker.getTPS();
    }

    @Override
    public long getCurrentTick() {
        try {
            Method m = Bukkit.class.getMethod("getCurrentTick");
            return (long) m.invoke(null);
        } catch (Exception e) {
            return tpsTracker.getCurrentTick();
        }
    }

    @Override
    public boolean isLagging() {
        double[] tps = getTPS();
        return tps[0] < 18.0;
    }

    // ==================== 调度器 ====================
    @Override
    public void runAsync(Runnable task) {
        asyncExecutor.submit(task);
    }

    @Override
    public BukkitTask runSync(Runnable task) {
        return Bukkit.getScheduler().runTask(getPlugin(), task);
    }

    @Override
    public BukkitTask runSyncLater(Runnable task, long delayTicks) {
        return Bukkit.getScheduler().runTaskLater(getPlugin(), task, delayTicks);
    }

    @Override
    public BukkitTask runSyncTimer(Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimer(getPlugin(), task, delayTicks, periodTicks);
    }

    // ==================== 玩家基础 ====================
    @Override
    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public void sendActionBar(Player player, String message) {
        // 完全通过反射调用，避免编译时依赖 Paper API
        try {
            Method m = player.getClass().getMethod("sendActionBar", Component.class);
            m.invoke(player, Component.text(message));
            return;
        } catch (NoSuchMethodException e1) {
            // 忽略，继续尝试下一个
        } catch (Exception ignored) {}

        try {
            Method m = player.getClass().getMethod("sendActionBar", String.class);
            m.invoke(player, message);
            return;
        } catch (NoSuchMethodException e2) {
            // 忽略
        } catch (Exception ignored) {}

        try {
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer");
            if (craftPlayer.isInstance(player)) {
                Method m = craftPlayer.getMethod("sendActionBar", String.class);
                m.invoke(player, message);
                return;
            }
        } catch (Exception ignored) {}

        // 最终降级：发送到聊天栏
        player.sendMessage("§7[ActionBar] " + message);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        } catch (NoSuchMethodError e) {
            if (title != null && !title.isEmpty()) player.sendMessage("§l" + title);
            if (subtitle != null && !subtitle.isEmpty()) player.sendMessage("§7" + subtitle);
        }
    }

    // ==================== 世界基础 ====================
    @Override
    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    @Override
    public World getWorld(UUID uuid) {
        return Bukkit.getWorld(uuid);
    }

    @Override
    public List<? extends World> getWorlds() {
        return Bukkit.getWorlds();
    }

    @Override
    public float getLocalDifficultyAt(Location location) {
        World world = location.getWorld();
        if (world == null) return 0.0f;
        double y = location.getY();
        long time = world.getTime();
        float diff = 0.0f;
        if (y < 0) diff += 0.3f;
        if (y < -20) diff += 0.3f;
        if (time > 13000 && time < 23000) diff += 0.4f;
        return Math.min(diff, 1.0f);
    }

    // ==================== 插件工具 ====================
    @Override
    public Plugin getPlugin() {
        if (plugin == null) throw new IllegalStateException("API.setPlugin() not called");
        return plugin;
    }

    @Override
    public void setPlugin(Plugin p) {
        plugin = p;
        tpsTracker.start(p);
    }

    // ==================== Purpur Server API ====================
    @Override
    public void addFuel(Material material, int burnTime) {
        fuelRegistry.put(material, burnTime);
    }

    @Override
    public void removeFuel(Material material) {
        fuelRegistry.remove(material);
    }

    // ==================== Purpur Player API ====================
    @Override
    public boolean isAfk(Player player) {
        return afkStatus.getOrDefault(player.getUniqueId(), false);
    }

    @Override
    public void setAfk(Player player, boolean afk) {
        afkStatus.put(player.getUniqueId(), afk);
    }

    @Override
    public void resetIdleTimer(Player player) {
        resetIdleDuration(player);
    }

    @Override
    public void resetIdleDuration(Player player) {
        afkStatus.put(player.getUniqueId(), false);
        try {
            Method m = player.getClass().getMethod("resetIdleDuration");
            m.invoke(player);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class);
            m.invoke(player, location, duration);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration, int argb) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class, int.class);
            m.invoke(player, location, duration, argb);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration, String text) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class, String.class);
            m.invoke(player, location, duration, text);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration, String text, int argb) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class, String.class, int.class);
            m.invoke(player, location, duration, text, argb);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration, Color color, int transparency) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class, Color.class, int.class);
            m.invoke(player, location, duration, color, transparency);
        } catch (Exception ignored) {}
    }

    @Override
    public void sendBlockHighlight(Player player, Location location, int duration, String text, Color color, int transparency) {
        try {
            Method m = player.getClass().getMethod("sendBlockHighlight", Location.class, int.class, String.class, Color.class, int.class);
            m.invoke(player, location, duration, text, color, transparency);
        } catch (Exception ignored) {}
    }

    @Override
    public void clearBlockHighlights(Player player) {
        try {
            Method m = player.getClass().getMethod("clearBlockHighlights");
            m.invoke(player);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean usesPurpurClient(Player player) {
        try {
            Method m = player.getClass().getMethod("usesPurpurClient");
            return (boolean) m.invoke(player);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean teleportOffline(Player player, Location location) {
        if (player.isOnline()) {
            return player.teleport(location);
        }
        return false;
    }

    // ==================== Purpur Entity API ====================
    @Override
    public boolean isFireImmune(Entity entity) {
        return fireImmuneCache.getOrDefault(entity.getUniqueId(), false);
    }

    @Override
    public void setFireImmune(Entity entity, boolean fireImmune) {
        fireImmuneCache.put(entity.getUniqueId(), fireImmune);
        try {
            Method m = entity.getClass().getMethod("setFireImmune", Boolean.class);
            m.invoke(entity, fireImmune);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean shouldBurnInDay(LivingEntity entity) {
        return burnInDayCache.getOrDefault(entity.getUniqueId(), true);
    }

    @Override
    public void setShouldBurnInDay(LivingEntity entity, boolean shouldBurnInDay) {
        burnInDayCache.put(entity.getUniqueId(), shouldBurnInDay);
        try {
            Method m = entity.getClass().getMethod("setShouldBurnInDay", boolean.class);
            m.invoke(entity, shouldBurnInDay);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isInDaylight(LivingEntity entity) {
        Location loc = entity.getLocation();
        World w = loc.getWorld();
        if (w == null) return false;
        return w.getTime() > 0 && w.getTime() < 13000 && loc.getBlock().getLightFromSky() > 10;
    }

    @Override
    public Entity getRider(Entity entity) {
        UUID id = riderCache.get(entity.getUniqueId());
        if (id != null) return Bukkit.getPlayer(id);
        try {
            Method m = entity.getClass().getMethod("getRider");
            return (Entity) m.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean hasRider(Entity entity) {
        if (riderCache.containsKey(entity.getUniqueId())) return true;
        try {
            Method m = entity.getClass().getMethod("hasRider");
            return (boolean) m.invoke(entity);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRidable(Entity entity) {
        return riderCache.containsKey(entity.getUniqueId());
    }

    @Override
    public boolean isRidableInWater(Entity entity) {
        return false;
    }

    @Override
    public double getRidableMaxY(Entity entity) {
        return ridableMaxYCache.getOrDefault(entity.getUniqueId(), Double.MAX_VALUE);
    }

    @Override
    public void setRidableMaxY(Entity entity, double maxY) {
        ridableMaxYCache.put(entity.getUniqueId(), maxY);
        try {
            Method m = entity.getClass().getMethod("setRidableMaxY", double.class);
            m.invoke(entity, maxY);
        } catch (Exception ignored) {}
    }

    // ==================== Purpur Boss API ====================
    @Override
    public UUID getWitherSummoner(Entity entity) {
        try {
            Method m = entity.getClass().getMethod("getSummoner");
            return (UUID) m.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setWitherSummoner(Entity entity, UUID summoner) {
        try {
            Method m = entity.getClass().getMethod("setSummoner", UUID.class);
            m.invoke(entity, summoner);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isRavagerGriefable(Entity entity) {
        try {
            Method m = entity.getClass().getMethod("isGriefable");
            return (boolean) m.invoke(entity);
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void setRavagerGriefable(Entity entity, boolean griefable) {
        try {
            Method m = entity.getClass().getMethod("setGriefable", boolean.class);
            m.invoke(entity, griefable);
        } catch (Exception ignored) {}
    }

    // ==================== Purpur Attributes API ====================
    @Override
    public double getEntityAttribute(Entity entity, Attribute attribute) {
        if (entity instanceof LivingEntity) {
            try {
                return ((LivingEntity) entity).getAttribute(attribute).getValue();
            } catch (Exception ignored) {}
        }
        String key = entity.getType().name() + "." + attribute.name();
        return entityAttributeCache.getOrDefault(key, 0.0);
    }

    @Override
    public void setEntityAttribute(Entity entity, Attribute attribute, double value) {
        if (entity instanceof LivingEntity) {
            try {
                ((LivingEntity) entity).getAttribute(attribute).setBaseValue(value);
                return;
            } catch (Exception ignored) {}
        }
        String key = entity.getType().name() + "." + attribute.name();
        entityAttributeCache.put(key, value);
    }

    // ==================== Purpur ItemStack API ====================
    @Override
    public String getDisplayName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getDisplayName();
    }

    @Override
    public void setDisplayName(ItemStack item, String name) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    @Override
    public boolean hasDisplayName(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName();
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        // 使用新方法 getItemName() 替代已废弃的 getLocalizedName()
        return item.getItemMeta().getItemName();
    }

    @Override
    public void setLocalizedName(ItemStack item, String name) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        // 使用新方法 setItemName() 替代已废弃的 setLocalizedName()
        meta.setItemName(name);
        item.setItemMeta(meta);
    }

    @Override
    public boolean hasLocalizedName(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasItemName();
    }

    @Override
    public boolean hasLore(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasLore();
    }

    @Override
    public int getCustomModelData(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getCustomModelData();
    }

    @Override
    public void setCustomModelData(ItemStack item, Integer data) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setCustomModelData(data);
        item.setItemMeta(meta);
    }

    @Override
    public boolean hasCustomModelData(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData();
    }

    @Override
    public boolean hasEnchant(ItemStack item, Enchantment enchantment) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(enchantment);
    }

    @Override
    public int getEnchantLevel(ItemStack item, Enchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getEnchantLevel(enchantment);
    }

    @Override
    public Map<Enchantment, Integer> getEnchants(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return new HashMap<>();
        return item.getItemMeta().getEnchants();
    }

    @Override
    public boolean addEnchant(ItemStack item, Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        item.setItemMeta(meta);
        return true;
    }

    @Override
    public boolean removeEnchant(ItemStack item, Enchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        meta.removeEnchant(enchantment);
        item.setItemMeta(meta);
        return true;
    }

    @Override
    public boolean hasEnchants(ItemStack item) {
        return item != null && item.hasItemMeta() && !item.getItemMeta().getEnchants().isEmpty();
    }

    @Override
    public boolean hasConflictingEnchant(ItemStack item, Enchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return false;
        for (Enchantment e : item.getItemMeta().getEnchants().keySet()) {
            if (e.conflictsWith(enchantment)) return true;
        }
        return false;
    }

    @Override
    public boolean hasAttributeModifiers(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasAttributeModifiers();
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(ItemStack item, Attribute attribute) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getAttributeModifiers(attribute);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getAttributeModifiers();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack item, EquipmentSlot slot) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getAttributeModifiers(slot);
    }

    @Override
    public boolean addAttributeModifier(ItemStack item, Attribute attribute, AttributeModifier modifier) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
        return true;
    }

    // ==================== Purpur StoredEntity API ====================
    @Override
    public Object getStoredEntity(Block block, Class<? extends Entity> entityClass) {
        try {
            Method m = block.getClass().getMethod("getStoredEntity", Class.class);
            return m.invoke(block, entityClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getFirstStoredEntity(Block block) {
        try {
            Method m = block.getClass().getMethod("getFirstStoredEntity");
            return m.invoke(block);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Object> getAllStoredEntities(Block block) {
        try {
            Method m = block.getClass().getMethod("getAllStoredEntities");
            return (List<Object>) m.invoke(block);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Entity releaseStoredEntity(Object storedEntity) {
        if (storedEntity == null) return null;
        try {
            Method m = storedEntity.getClass().getMethod("release");
            return (Entity) m.invoke(storedEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EntityType getStoredEntityType(Object storedEntity) {
        if (storedEntity == null) return null;
        try {
            Method m = storedEntity.getClass().getMethod("getType");
            return (EntityType) m.invoke(storedEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getStoredEntityCustomName(Object storedEntity) {
        if (storedEntity == null) return null;
        try {
            Method m = storedEntity.getClass().getMethod("getCustomName");
            return (String) m.invoke(storedEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setStoredEntityCustomName(Object storedEntity, String name) {
        if (storedEntity == null) return;
        try {
            Method m = storedEntity.getClass().getMethod("setCustomName", String.class);
            m.invoke(storedEntity, name);
            updateStoredEntity(storedEntity);
        } catch (Exception ignored) {}
    }

    @Override
    public Component getStoredEntityCustomNameComponent(Object storedEntity) {
        if (storedEntity == null) return null;
        try {
            Method m = storedEntity.getClass().getMethod("customName");
            return (Component) m.invoke(storedEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setStoredEntityCustomNameComponent(Object storedEntity, Component name) {
        if (storedEntity == null) return;
        try {
            Method m = storedEntity.getClass().getMethod("customName", Component.class);
            m.invoke(storedEntity, name);
            updateStoredEntity(storedEntity);
        } catch (Exception ignored) {}
    }

    @Override
    public PersistentDataContainer getStoredEntityData(Object storedEntity) {
        if (storedEntity == null) return null;
        try {
            Method m = storedEntity.getClass().getMethod("getPersistentDataContainer");
            return (PersistentDataContainer) m.invoke(storedEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateStoredEntity(Object storedEntity) {
        if (storedEntity == null) return;
        try {
            Method m = storedEntity.getClass().getMethod("update");
            m.invoke(storedEntity);
        } catch (Exception ignored) {}
    }

    // ==================== Purpur Tooltip API ====================
    @Override
    public Object createTooltipContext(boolean advanced, boolean creative) {
        try {
            Class<?> cls = Class.forName("io.papermc.paper.inventory.tooltip.TooltipContext");
            Method m = cls.getMethod("create", boolean.class, boolean.class);
            return m.invoke(null, advanced, creative);
        } catch (Exception e) {
            Map<String, Boolean> map = new HashMap<>();
            map.put("advanced", advanced);
            map.put("creative", creative);
            return map;
        }
    }

    @Override
    public List<Component> computeTooltip(ItemStack item, Object tooltipContext, Player player) {
        if (item == null) return new ArrayList<>();
        try {
            Method m = item.getClass().getMethod("computeTooltip",
                    Class.forName("io.papermc.paper.inventory.tooltip.TooltipContext"),
                    Player.class);
            return (List<Component>) m.invoke(item, tooltipContext, player);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ==================== Purpur UI Bars ====================
    @Override
    public void showTpsBar(Player player) {
        if (player == null || !player.isOnline()) return;
        try {
            Bukkit.dispatchCommand(player, "tpsbar");
            return;
        } catch (Exception ignored) {}

        double[] tps = getTPS();
        double avg = tps[0];
        BossBar bar = Bukkit.createBossBar(
                "§6TPS: §a" + String.format("%.2f", avg) +
                        " §7| §e5m: §a" + String.format("%.2f", tps[1]) +
                        " §7| §e15m: §a" + String.format("%.2f", tps[2]),
                avg < 10 ? BarColor.RED : (avg < 15 ? BarColor.YELLOW : BarColor.GREEN),
                BarStyle.SEGMENTED_10
        );
        bar.setProgress(Math.min(avg / 20.0, 1.0));
        bar.addPlayer(player);
        tpsBars.put(player.getUniqueId(), bar);
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            bar.removePlayer(player);
            tpsBars.remove(player.getUniqueId());
        }, 200L);
    }

    @Override
    public void showRamBar(Player player) {
        if (player == null || !player.isOnline()) return;
        try {
            Bukkit.dispatchCommand(player, "rambar");
            return;
        } catch (Exception ignored) {}

        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory() / 1024 / 1024;
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        double perc = (double) used / max;
        BossBar bar = Bukkit.createBossBar(
                "§6RAM: §e" + used + "§7/§e" + max + " MB §7(" + String.format("%.1f", perc * 100) + "%)",
                perc < 0.7 ? BarColor.GREEN : (perc < 0.85 ? BarColor.YELLOW : BarColor.RED),
                BarStyle.SEGMENTED_10
        );
        bar.setProgress(Math.min(perc, 1.0));
        bar.addPlayer(player);
        ramBars.put(player.getUniqueId(), bar);
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            bar.removePlayer(player);
            ramBars.remove(player.getUniqueId());
        }, 200L);
    }

    @Override
    public void hideTpsBar(Player player) {
        BossBar bar = tpsBars.remove(player.getUniqueId());
        if (bar != null) bar.removePlayer(player);
        try {
            Bukkit.dispatchCommand(player, "tpsbar");
        } catch (Exception ignored) {}
    }

    @Override
    public void hideRamBar(Player player) {
        BossBar bar = ramBars.remove(player.getUniqueId());
        if (bar != null) bar.removePlayer(player);
        try {
            Bukkit.dispatchCommand(player, "rambar");
        } catch (Exception ignored) {}
    }

    // ==================== Folia 方法（在 Purpur 中降级） ====================
    @Override
    public Object getRegionScheduler() {
        return null;
    }

    @Override
    public void runAtRegion(Location location, Runnable task) {
        runSync(task);
    }

    @Override
    public void runAtRegionLater(Location location, Runnable task, long delayTicks) {
        runSyncLater(task, delayTicks);
    }

    @Override
    public void runAtRegionTimer(Location location, Runnable task, long initialDelayTicks, long periodTicks) {
        runSyncTimer(task, initialDelayTicks, periodTicks);
    }

    @Override
    public Object getEntityScheduler(Entity entity) {
        return null;
    }

    @Override
    public void runAtEntity(Entity entity, Runnable task) {
        runSync(task);
    }

    @Override
    public void runAtEntityLater(Entity entity, Runnable task, long delayTicks) {
        runSyncLater(task, delayTicks);
    }

    @Override
    public Object getGlobalRegionScheduler() {
        return null;
    }

    @Override
    public void runGlobal(Runnable task) {
        runSync(task);
    }

    @Override
    public Object getAsyncScheduler() {
        return null;
    }

    @Override
    public void runAsyncNow(Runnable task) {
        runAsync(task);
    }

    @Override
    public double[] getRegionTPS(Location location) {
        return getTPS();
    }

    @Override
    public double[] getRegionTPS(Chunk chunk) {
        return getTPS();
    }

    @Override
    public double[] getRegionTPS(World world, int chunkX, int chunkZ) {
        return getTPS();
    }

    // ==================== 环境检测 ====================
    @Override
    public boolean isFolia() {
        return ServiceDetector.isFolia();
    }

    @Override
    public boolean isPaper() {
        return ServiceDetector.isPaper();
    }

    @Override
    public boolean isPurpur() {
        return ServiceDetector.isPurpur();
    }

    @Override
    public boolean isNeoForge() {
        return ServiceDetector.isNeoForge();
    }
}