package io.github.tanguygab.afkplaytime;

import net.ess3.api.IUser;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import net.ess3.api.events.AfkStatusChangeEvent;

import java.util.HashMap;
import java.util.Map;

public final class AFKPlaytime extends JavaPlugin implements Listener {

    protected final Map<IUser, Integer> users = new HashMap<>();
    private final Statistic stat = Statistic.PLAY_ONE_MINUTE;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        users.keySet().forEach(user->user.getBase().setStatistic(stat, users.get(user)));
    }

    @EventHandler
    public void onAfk(AfkStatusChangeEvent e) {
        IUser user = e.getAffected();
        Player player = user.getBase();
        if (user.isAfk()) {
            player.setStatistic(stat,users.get(user));
            users.remove(user);
        } else users.put(user,player.getStatistic(stat));
    }
}
