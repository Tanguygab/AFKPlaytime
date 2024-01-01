package io.github.tanguygab.afkplaytime;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class AFKPlaytimeExpansion extends PlaceholderExpansion {

    private final AFKPlaytime plugin;

    public AFKPlaytimeExpansion(AFKPlaytime plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "afkplaytime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Tanguygab";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        int ticks = plugin.getPlayTime(player);
        if (params.equals("time_played")) return getFormattedTime(ticks);
        return Long.toString(getTime(ticks, params));
    }

    private long getTime(Integer ticks, String params) {
        switch (params) {
            case "time_played:seconds": return ticks/20 % 60;
            case "time_played:minutes": return TimeUnit.SECONDS.toMinutes(ticks/20) % 60;
            case "time_played:hours": return TimeUnit.SECONDS.toHours(ticks/20) % 24;

            case "seconds_played": return ticks/20;
            case "minutes_played": return TimeUnit.SECONDS.toMinutes(ticks/20);
            case "hours_played": return TimeUnit.SECONDS.toHours(ticks/20);

            case "time_played:days":
            case "days_played": return TimeUnit.SECONDS.toDays(ticks/20);

            default: return 0;
        }
    }

    /**
     * @author Sxtanna
     * From https://github.com/PlaceholderAPI/Statistics-Expansion/blob/master/src/main/java/com/extendedclip/papi/expansion/mcstatistics/StatisticsUtils.java#L135
     */
    private String getFormattedTime(int seconds) {
        if (seconds < 1) return "";
        if (seconds < 60) return seconds + "s";

        int minutes = seconds / 60,
                hours = minutes / 60,
                days = hours / 24,
                weeks = days / 7;

        final StringJoiner joiner = new StringJoiner(" ");
        appendTime(joiner, weeks, "w");
        appendTime(joiner, days%7, "d");
        appendTime(joiner, hours%24, "h");
        appendTime(joiner, minutes%60, "m");
        appendTime(joiner, seconds %60, "s");
        return joiner.toString();
    }

    private void appendTime(final StringJoiner joiner, final long value, final String unit) {
        if (value > 0) joiner.add(value + unit);
    }

}

