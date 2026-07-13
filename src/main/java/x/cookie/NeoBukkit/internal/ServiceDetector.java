package x.cookie.NeoBukkit.internal;

public final class ServiceDetector {

    public static boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isFolia() {
        return hasClass("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
    }

    public static boolean isPurpur() {
        return hasClass("org.purpurmc.purpur.PurpurConfig");
    }

    public static boolean isPaper() {
        return hasClass("com.destroystokyo.paper.PaperConfig");
    }

    public static boolean isSpigot() {
        return hasClass("org.spigotmc.SpigotConfig");
    }

    public static boolean isNeoForge() {
        return hasClass("net.neoforged.fml.loading.FMLEnvironment");
    }

    private ServiceDetector() {}
}