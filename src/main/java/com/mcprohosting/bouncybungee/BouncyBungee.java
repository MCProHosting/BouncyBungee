package com.mcprohosting.bouncybungee;

import com.mcprohosting.bouncybungee.command.BaseReceiver;
import com.mcprohosting.bouncybungee.command.BaseSender;
import com.mcprohosting.bouncybungee.command.NetCommandDispatch;
import com.mcprohosting.bouncybungee.igcommand.AlertNet;
import com.mcprohosting.bouncybungee.igcommand.GAlertCommand;
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
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GAlertCommand());
        this.getDispatch().registerNetCommands(new AlertNet());
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
    }

    /**
     * Reloads the two properties files.
     * @throws IOException When there was an error reading the files
     */
    public void reload() throws IOException {
        this.settings = new Properties();
        this.strings = new Properties();
        this.settings.load(getFileAsStream("settings.properties"));
        if (!this.settings.containsKey("host")) {
            BouncyBungee.getInstance().getLogger().info("Adding property");
            this.settings.setProperty("host", "127.0.0.1");
            this.settings.store(new FileOutputStream(getDataFolder().getPath() + "/settings.properties"), null);
        }
        this.strings.load(getResourceAsStream("strings.properties"));
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
}
