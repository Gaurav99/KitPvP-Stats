package me.jaaster.plugin.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Plado on 2/12/2017.
 */

public class PlayerDataManager {

    @Getter
    private static HashMap<UUID, PlayerData> playerDataList = new HashMap<>();


    public static void addPlayerData(UUID uuid){
        int kills = 0;
        int deaths = 0;
        double damage = 0;
        Player p = Bukkit.getPlayer(uuid);
        if (DataManager.hasPlayerData(p)) {
            kills = DataManager.getKills(p);
            deaths = DataManager.getDeaths(p);
            damage = DataManager.getDamage(p);
        }
        PlayerDataManager.addPlayerData(p.getUniqueId(), new PlayerData(kills, deaths, damage));
    }


    public static void addPlayerData(UUID uuid, PlayerData pd){
        playerDataList.put(uuid, pd);

    }
    public static void removePlayerData(UUID uuid){
        playerDataList.remove(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid){
        return playerDataList.get(uuid);
    }

    public static boolean hasPlayerData(UUID uuid){
        return getPlayerData(uuid) != null;
    }


}
