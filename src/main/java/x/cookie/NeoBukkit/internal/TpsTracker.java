package x.cookie.NeoBukkit.internal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;

public class TpsTracker {

    private final LinkedList<Double> history = new LinkedList<>();
    private long lastTime = System.currentTimeMillis();
    private int tickCount = 0;
    private double currentTps = 20.0;
    private long currentTick = 0;

    public void start(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(plugin, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateTps();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void tick() {
        tickCount++;
        currentTick++;
    }

    private void updateTps() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastTime;
        if (elapsed > 0) {
            double tps = (double) tickCount * 1000 / elapsed;
            if (tps > 20.0) tps = 20.0;
            history.add(tps);
            if (history.size() > 10) history.removeFirst();
            double sum = 0;
            for (double v : history) sum += v;
            currentTps = sum / history.size();
        }
        tickCount = 0;
        lastTime = now;
    }

    public double[] getTPS() {
        return new double[]{
            currentTps,
            history.size() >= 5 ? history.get(history.size() - 5) : currentTps,
            history.size() >= 10 ? history.get(history.size() - 10) : currentTps
        };
    }

    public long getCurrentTick() {
        return currentTick;
    }
}