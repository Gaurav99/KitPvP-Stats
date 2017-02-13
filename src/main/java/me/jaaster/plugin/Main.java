package me.jaaster.plugin;

import me.jaaster.plugin.commands.StatsCommand;
import me.jaaster.plugin.data.DataManager;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import me.jaaster.plugin.events.PVPEvents;
import me.jaaster.plugin.events.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Plado on 2/12/2017.
 */
public class Main extends JavaPlugin {
    private DataManager dataManager;
    private PluginManager manager;
    private static Main main;

    public void onEnable() {
        main = this;
        manager = getServer().getPluginManager();

        dataManager = new DataManager("localhost", 3306, "sys", "Joriah Lasater", "root", "KitPvP-Stats");
        registerCommands();
        registerListeners();
        registerOnlinePlayers();
    }


    public void onDisable() {
        saveOnlinePlayerData();
        try {
            dataManager.getConnection().close();
        } catch (SQLException e) {
            getLogger().severe("Cannot Connect to database!");
            e.printStackTrace();
        }


    }

    private void registerOnlinePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerDataManager.addPlayerData(p.getUniqueId());
        }
    }

    private void registerCommands() {
        getCommand("stats").setExecutor(new StatsCommand());
    }

    private void registerListeners() {
        manager.registerEvents(new Trigger(), this);
        manager.registerEvents(new PVPEvents(), this);
    }

    private void saveOnlinePlayerData() {
        for (UUID uuid : PlayerDataManager.getPlayerDataList().keySet()) {
            //If Data is not associated with an online player
            if (Bukkit.getPlayer(uuid) == null) {
                continue;
            }

            Player p = Bukkit.getPlayer(uuid);

            PlayerData pd = PlayerDataManager.getPlayerData(uuid);

            if (!DataManager.hasPlayerData(p)) {
                DataManager.createPlayer(p, pd.getKills(), pd.getDeaths(), pd.getDamage());
            }

            DataManager.setPlayerKills(p, pd.getKills());
            DataManager.setDeaths(p, pd.getDeaths());
            DataManager.setDamage(p, pd.getDamage());
        }
    }


}
