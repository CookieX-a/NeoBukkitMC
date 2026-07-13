package x.cookie.NeoBukkit.impl;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class FoliaUnifiedImpl extends PurpurUnifiedImpl {

    @Override
    public Object getRegionScheduler() {
        try {
            Method m = Bukkit.class.getMethod("getRegionScheduler");
            return m.invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void runAtRegion(Location location, Runnable task) {
        try {
            Object scheduler = Bukkit.class.getMethod("getRegionScheduler").invoke(null);
            Method run = scheduler.getClass().getMethod("run", Plugin.class, Location.class, Consumer.class);
            run.invoke(scheduler, getPlugin(), location, (Consumer<Object>) t -> task.run());
        } catch (Exception e) {
            super.runAtRegion(location, task);
        }
    }

    @Override
    public void runAtRegionLater(Location location, Runnable task, long delayTicks) {
        try {
            Object scheduler = Bukkit.class.getMethod("getRegionScheduler").invoke(null);
            Method run = scheduler.getClass().getMethod("runDelayed", Plugin.class, Location.class, Consumer.class, long.class);
            run.invoke(scheduler, getPlugin(), location, (Consumer<Object>) t -> task.run(), delayTicks);
        } catch (Exception e) {
            super.runAtRegionLater(location, task, delayTicks);
        }
    }

    @Override
    public void runAtRegionTimer(Location location, Runnable task, long initialDelayTicks, long periodTicks) {
        try {
            Object scheduler = Bukkit.class.getMethod("getRegionScheduler").invoke(null);
            Method run = scheduler.getClass().getMethod("runAtFixedRate", Plugin.class, Location.class, Consumer.class, long.class, long.class);
            run.invoke(scheduler, getPlugin(), location, (Consumer<Object>) t -> task.run(), initialDelayTicks, periodTicks);
        } catch (Exception e) {
            super.runAtRegionTimer(location, task, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public Object getEntityScheduler(Entity entity) {
        try {
            Method m = entity.getClass().getMethod("getScheduler");
            return m.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void runAtEntity(Entity entity, Runnable task) {
        try {
            Object scheduler = entity.getClass().getMethod("getScheduler").invoke(entity);
            Method run = scheduler.getClass().getMethod("run", Plugin.class, Consumer.class, Runnable.class);
            run.invoke(scheduler, getPlugin(), (Consumer<Object>) t -> task.run(), (Runnable) null);
        } catch (Exception e) {
            super.runAtEntity(entity, task);
        }
    }

    @Override
    public void runAtEntityLater(Entity entity, Runnable task, long delayTicks) {
        try {
            Object scheduler = entity.getClass().getMethod("getScheduler").invoke(entity);
            Method run = scheduler.getClass().getMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class);
            run.invoke(scheduler, getPlugin(), (Consumer<Object>) t -> task.run(), (Runnable) null, delayTicks);
        } catch (Exception e) {
            super.runAtEntityLater(entity, task, delayTicks);
        }
    }

    @Override
    public Object getGlobalRegionScheduler() {
        try {
            Method m = Bukkit.class.getMethod("getGlobalRegionScheduler");
            return m.invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void runGlobal(Runnable task) {
        try {
            Object scheduler = Bukkit.class.getMethod("getGlobalRegionScheduler").invoke(null);
            Method run = scheduler.getClass().getMethod("run", Plugin.class, Consumer.class);
            run.invoke(scheduler, getPlugin(), (Consumer<Object>) t -> task.run());
        } catch (Exception e) {
            super.runGlobal(task);
        }
    }

    @Override
    public Object getAsyncScheduler() {
        try {
            Method m = Bukkit.class.getMethod("getAsyncScheduler");
            return m.invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void runAsyncNow(Runnable task) {
        try {
            Object scheduler = Bukkit.class.getMethod("getAsyncScheduler").invoke(null);
            Method run = scheduler.getClass().getMethod("runNow", Plugin.class, Consumer.class);
            run.invoke(scheduler, getPlugin(), (Consumer<Object>) t -> task.run());
        } catch (Exception e) {
            super.runAsyncNow(task);
        }
    }

    @Override
    public double[] getRegionTPS(Location location) {
        try {
            Method m = Bukkit.class.getMethod("getRegionTPS", Location.class);
            return (double[]) m.invoke(null, location);
        } catch (Exception e) {
            return super.getRegionTPS(location);
        }
    }

    @Override
    public double[] getRegionTPS(Chunk chunk) {
        try {
            Method m = Bukkit.class.getMethod("getRegionTPS", Chunk.class);
            return (double[]) m.invoke(null, chunk);
        } catch (Exception e) {
            return super.getRegionTPS(chunk);
        }
    }

    @Override
    public double[] getRegionTPS(World world, int chunkX, int chunkZ) {
        try {
            Method m = Bukkit.class.getMethod("getRegionTPS", World.class, int.class, int.class);
            return (double[]) m.invoke(null, world, chunkX, chunkZ);
        } catch (Exception e) {
            return super.getRegionTPS(world, chunkX, chunkZ);
        }
    }

    @Override
    public boolean isFolia() {
        return true;
    }
}