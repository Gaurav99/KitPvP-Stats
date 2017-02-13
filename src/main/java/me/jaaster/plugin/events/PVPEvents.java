package me.jaaster.plugin.events;

import me.jaaster.plugin.DamageAPI;
import me.jaaster.plugin.data.PlayerData;
import me.jaaster.plugin.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by Plado on 2/12/2017.
 */
public class PVPEvents implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        PlayerData pd = PlayerDataManager.getPlayerData(e.getEntity().getUniqueId());
        pd.setDeaths(pd.getDeaths() + 1);
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player))
            return;

        Player v = (Player) e.getEntity();

        if(e.getDamager() instanceof Projectile){
            Projectile proj = (Projectile) e.getDamager();
            if(!(proj.getShooter() instanceof  Player))
                return;

            double damage = DamageAPI.calculateDamageAddArmor(v.getInventory().getArmorContents(), e.getCause(), e.getDamage());
            Player d = (Player) proj.getShooter();
            addDamage(d, damage);
            if(v.getHealth() - damage <=0)
                addKill(d);

            return;

        }

        if(!(e.getDamager() instanceof Player))
            return;
        Player d = (Player) e.getDamager();
        //Calculated the armor and the damage
        double damage = DamageAPI.calculateDamageAddArmor(v.getInventory().getArmorContents(), e.getCause(), e.getDamage());
        addDamage(d, damage);


        if(v.getHealth() - damage <=0)
            addKill(d);


    }


    private void addKill(Player d){
        PlayerData pd = PlayerDataManager.getPlayerData(d.getUniqueId());
        pd.setKills(pd.getKills() + 1);
    }

    private void addDamage(Player d, double damage){

        PlayerData pd = PlayerDataManager.getPlayerData(d.getUniqueId());
        pd.setDamage(pd.getDamage() + damage);
    }


}
