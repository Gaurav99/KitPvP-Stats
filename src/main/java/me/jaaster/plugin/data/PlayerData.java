package me.jaaster.plugin.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Plado on 2/12/2017.
 */
@AllArgsConstructor
@Getter
@Setter
public class PlayerData {

    private int kills;
    private int deaths;
    private double damage;

    public void setDamage(double damage){
        this.damage = Math.round(damage);
    }

}
