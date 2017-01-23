package com.github.games647.minefana;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.influxdb.dto.Point;

public class PlayerListener implements Listener {

    private final MinefanaBukkit plugin;

    public PlayerListener(MinefanaBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        //send delayed update to have the new player count
        Bukkit.getScheduler().runTask(plugin, this::sendPlayerUpdate);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        sendPlayerUpdate();
    }

    private void sendPlayerUpdate() {
        Point playerPoint = Point.measurement("players")
                .addField("online", Bukkit.getOnlinePlayers().size())
                .addField("max", Bukkit.getMaxPlayers())
                .build();

        plugin.getInfluxConnector().send(playerPoint);
    }
}
