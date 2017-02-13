package me.jaaster.plugin.commands;

import me.jaaster.plugin.data.DataManager;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Plado on 2/12/2017.
 */
public class StatsCommand implements CommandExecutor{


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!cmd.getLabel().equalsIgnoreCase("stats"))
         return false;

        if(!(sender instanceof Player)) {
            //Send players stats to sender
            return false;
        }
        Player p = (Player) sender;



        if(args.length > 0){
         //Target players stats
            if(isPlayer(args[0])) {
                //That player is not online
                showStats(p, Bukkit.getPlayer(args[0]));
                return true;
            }

            if(!DataManager.hasPlayerData(args[0])) {
                //That player does not online or in database
                return false;
            }
            //Targets stats
            showStats(p, Bukkit.getOfflinePlayer(DataManager.getUUID(args[0])));

            return true;
        }
            //Player sender stats
            showStats(p, null);

        return true;
    }

    //Player is online
    public boolean isPlayer(String s){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().equals(s))
                return true;
        }
        return false;
    }


    private void showStats(Player p, OfflinePlayer t){
       if(t == null)
           t = p;

        //Target does not have a wrapper class
        if(!PlayerDataManager.hasPlayerData(t.getUniqueId())){
            PlayerDataManager.addPlayerData(t.getUniqueId(), new PlayerData(DataManager.getKills(t),DataManager.getDeaths(t), DataManager.getDamage(t)));
        }

        PlayerData pd = PlayerDataManager.getPlayerData(t.getUniqueId());

        p.sendMessage("Name: " + t.getName());
        p.sendMessage("Kills: " + pd.getKills());
        p.sendMessage("Deaths: " + pd.getDeaths());
        p.sendMessage("Damage: " + pd.getDamage());
}


}
