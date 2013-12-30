package com.mcprohosting.bouncybungee;

import com.mcprohosting.bouncybungee.command.BaseReceiver;
import com.mcprohosting.bouncybungee.command.BaseSender;
import com.mcprohosting.bouncybungee.command.NetCommandDispatch;
import com.mcprohosting.bouncybungee.igcommand.*;
import com.mcprohosting.bouncybungee.listeners.MOTDFeature;
import com.mcprohosting.bouncybungee.listeners.PluginMessageListener;
import com.mcprohosting.bouncybungee.servers.BouncyServerBeatHandler;
import com.mcprohosting.bouncybungee.util.TPlugin;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

/**
 * Main Plugin class!
 */
public class BouncyBungee extends TPlugin {
    /**
     * The instance of the plugin, stored here for ease!
     */
    @Getter private static BouncyBungee instance;
    /**
     * Stores our settings file (loaded)
     */
    private Properties settings;
    /**
     * Stores our strings file (loaded)
     */
    private Properties strings;
    /**
     * Stores our player bans
     */
    private ArrayList<String> bans;
    /**
     * Has our NetCommandDispatch for registration.
     */
    @Getter private NetCommandDispatch dispatch;
    /**
     * Has our BouncyServerBeatHandler for handling heartbeats from servers.
     */
    @Getter private BouncyServerBeatHandler beatHandler;
    /**
     * Stores a Jedis pool.
     */
    private JedisPool jedisPool;

    /**
     * Start method.
     */
    @Override
    protected void start() {
        BouncyBungee.instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        try {
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, this.settings.getProperty("host"));
        this.dispatch = new NetCommandDispatch();
        this.getDispatch().registerNetCommands(new BaseReceiver());
        registerEvents(new BaseSender());
        this.beatHandler = new BouncyServerBeatHandler();
        this.beatHandler.schedule();
        registerEvents(new MOTDFeature());
        registerEvents(new PluginMessageListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GAlertCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GSetMaxPlayers());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GBan());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GTransfer());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GSend());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GSetMOTD());
        this.getDispatch().registerNetCommands(new AlertNet());
        this.getDispatch().registerNetCommands(new ModifyNetMaxPlayers());
        this.getDispatch().registerNetCommands(new NetBan());
        this.getDispatch().registerNetCommands(new TransferNet());
        this.getDispatch().registerNetCommands(new NetSend());
        this.getDispatch().registerNetCommands(new ModifyNetMaxPlayers());
    }

    /**
     * Get a Jedis object here.
     * @return A newly grabbed Jedis link. Make sure you return it when you're done!
     */
    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    /**
     * Returns a Jedis resource to the pool.
     * @param jedis The Jedis resource you wish to return.
     */
    public void returnJedis(Jedis jedis) {
        this.jedisPool.returnResource(jedis);
    }

    /**
     * Nothing here yet!
     */
    @Override
    protected void stop() {
        saveBans();
    }

    /**
     * Reloads the two properties files.
     * @throws IOException When there was an error reading the files
     */
    public void reload() throws IOException {
        loadConfig();
    }

    public void loadConfig() throws IOException {
        this.settings = new Properties();
        this.strings = new Properties();
        this.settings.load(getFileAsStream("settings.properties"));
        this.strings.load(getResourceAsStream("strings.properties"));

        if (!this.settings.containsKey("host")) {
            this.settings.setProperty("host", "127.0.0.1");
            saveConfig();
        }
        if (!this.settings.containsKey("maxplayers")) {
            this.settings.setProperty("maxplayers", "100");
            saveConfig();
        }
        if (!this.settings.containsKey("motd")) {
            this.settings.setProperty("motd", "Welcome to TitanMC");
            saveConfig();
        }

        loadBans();
    }

    public boolean editConfigProperty(String key, String value) throws IOException {
        if (this.settings.containsKey(key)) {
            this.settings.setProperty(key, value);
            saveConfig();
            return true;
        }

        return false;
    }

    public Properties getSettings() {
        return settings;
    }

    public void saveConfig() throws IOException {
        this.settings.store(new FileOutputStream(getDataFolder().getPath() + "/settings.properties"), null);
    }

    public String getFormat(String key, boolean prefix, boolean color, String[]... datas) {
        String property = this.strings.getProperty(key);
        if (prefix) property = ChatColor.
                translateAlternateColorCodes('&', this.strings.getProperty("prefix")) + property;
        property = ChatColor.translateAlternateColorCodes('&', property);
        if (datas == null) return property;
        for (String[] data : datas) {
            if (data.length != 2) continue;
            property = property.replaceAll(data[0], data[1]);
        }
        if (color) property = ChatColor.translateAlternateColorCodes('&', property);
        return property;
    }
    public String getFormat(String key, boolean prefix, boolean color) {
        return getFormat(key, prefix, color, null);
    }
    public String getFormat(String key, boolean prefix) {
        return getFormat(key, prefix, true);
    }
    public String getFormat(String key) {
        return getFormat(key, true);
    }
    public InputStreamReader getFileAsStream(String name) {
        File f = new File(getDataFolder(), name);
        if (!(f.canRead() && f.exists())) try {
            boolean newFile = f.createNewFile();
            if (!newFile) return null;
            return getFileAsStream(name);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        try {
            return new InputStreamReader(new BufferedInputStream(new FileInputStream(f)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<ServerInfo> getAllServerInfo() {
        Collection<ServerInfo> copy = new HashSet<ServerInfo>();
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            copy.add(info);
        }
        return copy;
    }

    private void loadBans() {
        this.bans = new ArrayList<String>();

        File playerBans = new File(getDataFolder(), "bans.txt");
        try {
            if (playerBans.exists() == false) {
                playerBans.createNewFile();
            }

            BufferedReader in = new BufferedReader(new FileReader(playerBans));
            String line = null;

            while ((line = in.readLine()) != null) {
                this.bans.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBans() {
        File playerBans = new File(getDataFolder(), "bans.txt");
        try {
            playerBans.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(playerBans));

            for (String player : bans) {
                out.write(player);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerToBans(String player) {
        bans.add(player);
    }

    public boolean containsPlayer(String player) {
        return bans.contains(player);
    }
}
