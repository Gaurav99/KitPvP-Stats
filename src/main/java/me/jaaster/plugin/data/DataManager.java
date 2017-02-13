package me.jaaster.plugin.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

/**
 * Created by Plado on 2/12/2017.
 */
public class DataManager {

    private static DataManager instance;

    //UUID, Name, Time
    private static Connection connection;
    private String host, database, username, password;
    private int port;
    private static String prefix;
    private static String table;

    public DataManager(String host, Integer port, String database, String username, String password, String pluginname) {
        table = "Stats";
        instance = this;

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.prefix = pluginname;
        createConnection();
        create("CREATE TABLE IF NOT EXISTS " + table + "(uuid VARCHAR(60), name VARCHAR(16), kills INT, deaths INT, damage DOUBLE)");

    }



    public synchronized void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + this.username + "&password=" + this.password + "&autoReconnect=true");
            System.out.println(this.prefix + " Connected.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(this.prefix + e.getMessage());
        }

    }

    public Connection getConnection() {
        return this.connection;
    }

    //Getting data from Database
    private static ResultSet get(String string) {
        ResultSet resultset = null;
        try {
            resultset = connection.createStatement().executeQuery(string);
        } catch (SQLException e) {
            System.out.println(prefix + e.getMessage());
        }
        return resultset;
    }


    public static void create(String string) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(string);
            statement.close();
        } catch (SQLException e) {
            System.out.println(prefix + e.getMessage());
        }
    }


    //Database has data relating to the player
    public static synchronized boolean hasPlayerData(Player p) {
        try {
            ResultSet resultSet = get("SELECT uuid FROM " + table + " WHERE uuid  = " + "'" + p.getUniqueId() + "';");
            while (resultSet.next()) {
                if (resultSet.getString("uuid").equals(p.getUniqueId().toString()))
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static synchronized boolean hasPlayerData(String name) {
        try {
            ResultSet resultSet = get("SELECT name FROM " + table + " WHERE name  = " + "'" + name + "';");
            while (resultSet.next()) {
                if (resultSet.getString("name").equals(name))
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Adds playerdata database
    public static synchronized void createPlayer(Player p, int kills, int deaths, double damage){
        create("INSERT INTO " + "sys." + table + " VALUES(" + "'" + p.getUniqueId() + "'" + "," + "'" + p.getName() + "'" + "," + kills + "," + deaths + ","  + damage + ")");

    }

    //get uuid from string in the database
    public static synchronized UUID getUUID(String name){
        ResultSet resultSet = get("SELECT uuid FROM " + table + " WHERE name = " + "'" + name+ "';");
        try {
            UUID uuid = null;
            while (resultSet.next()) {
                uuid = UUID.fromString(resultSet.getString("uuid"));
            }
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Setters
    public static void setPlayerKills(Player p , double kills){
        create("UPDATE " + table + " SET kills = " + kills + " WHERE uuid = " + "'" + p.getUniqueId() + "'");
    }
    public static void setDeaths(Player p, int deaths){
        create("UPDATE " + table + " SET deaths = " + deaths + " WHERE uuid = " + "'" + p.getUniqueId() + "'");

    }
    public static void setDamage(Player p, double damage){
        create("UPDATE " + table + " SET damage = " + damage + " WHERE uuid = " + "'" + p.getUniqueId() + "'");

    }
    //Getters
    public static synchronized int getKills(OfflinePlayer p){
        ResultSet resultSet = get("SELECT kills FROM " + table + " WHERE uuid = " + "'" + p.getUniqueId() + "';");
        try {
            int kills = 0;
            while (resultSet.next()) {
                kills = resultSet.getInt("kills");
            }
            return kills;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized int getDeaths(OfflinePlayer p){
        ResultSet resultSet = get("SELECT deaths FROM " + table + " WHERE uuid = " + "'" + p.getUniqueId() + "';");
        try {
            int deaths = 0;
            while (resultSet.next()) {
                deaths = resultSet.getInt("deaths");
            }
            return deaths;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static synchronized double getDamage(OfflinePlayer p){
        ResultSet resultSet = get("SELECT damage FROM " + table + " WHERE uuid = " + "'" + p.getUniqueId() + "';");
        try {
            double damage = 0;
            while (resultSet.next()) {
                damage = resultSet.getInt("damage");
            }
            return damage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }










}
