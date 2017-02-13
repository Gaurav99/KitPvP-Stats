package me.jaaster.plugin.events;

import me.jaaster.plugin.data.DataManager;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Plado on 2/12/2017.
 */
public class Trigger implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        int kills = 0;
        int deaths = 0;
        double damage = 0;
        Player p = e.getPlayer();
        if (DataManager.hasPlayerData(p)) {
            kills = DataManager.getKills(p);
            deaths = DataManager.getDeaths(p);
            damage = DataManager.getDamage(p);
        }
        PlayerDataManager.addPlayerData(p.getUniqueId(), new PlayerData(kills, deaths, damage));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData pd = PlayerDataManager.getPlayerData(p.getUniqueId());
        //Save to database
        if(!DataManager.hasPlayerData(e.getPlayer())){
            DataManager.createPlayer(p, pd.getKills(), pd.getDeaths(), pd.getDamage());
        }

        DataManager.setPlayerKills(p, pd.getKills());
        DataManager.setDeaths(p, pd.getDeaths());
        DataManager.setDamage(p, pd.getDamage());

        PlayerDataManager.removePlayerData(e.getPlayer().getUniqueId());
    }
}
