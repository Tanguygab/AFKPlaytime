package io.github.tanguygab.afkplaytime;

import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import net.ess3.api.events.AfkStatusChangeEvent;

import java.util.HashMap;
import java.util.Map;

public final class AFKPlaytime extends JavaPlugin implements Listener {

    private final Map<IUser, Integer> users = new HashMap<>();
    private Statistic stat;
    private IEssentials essentials;
    private AFKPlaytimeExpansion expansion;

    @Override
    public void onEnable() {
        try {stat = Statistic.PLAY_ONE_MINUTE;}
        catch (Throwable e) {stat = Statistic.valueOf("PLAY_ONE_TICK");}

        essentials = (IEssentials) getServer().getPluginManager().getPlugin("Essentials");

        getServer().getPluginManager().registerEvents(this, this);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            expansion = new AFKPlaytimeExpansion(this);
            expansion.register();
        }
    }

    @Override
    public void onDisable() {
        users.keySet().forEach(user->user.getBase().setStatistic(stat, users.get(user)));
        if (expansion != null) expansion.unregister();
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

    public int getPlayTime(OfflinePlayer player) {
        User user = essentials.getUser(player.getUniqueId());
        if (users.containsKey(user)) return users.get(user);
        try {return player.getStatistic(stat);}
        catch (Throwable e) {return player.getPlayer() == null ? 0 : player.getPlayer().getStatistic(stat);}
    }
}
