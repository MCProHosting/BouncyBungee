package com.mcprohosting.bouncybungee;

import com.mcprohosting.bouncybungee.command.BaseReceiver;
import com.mcprohosting.bouncybungee.command.BaseSender;
import com.mcprohosting.bouncybungee.command.NetCommandDispatch;
import com.mcprohosting.bouncybungee.servers.BouncyServerBeatHandler;
import com.mcprohosting.bouncybungee.util.TPlugin;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
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
        Thread thread = new Thread(this.beatHandler);
        thread.start();
        registerEvents(new MOTDFeature());
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
        this.settings.load(getResourceAsStream("settings.properties"));
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
}
