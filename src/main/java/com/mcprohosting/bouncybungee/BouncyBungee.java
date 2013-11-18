package com.mcprohosting.bouncybungee;

import com.mcprohosting.bouncybungee.command.BaseReceiver;
import com.mcprohosting.bouncybungee.command.BaseSender;
import com.mcprohosting.bouncybungee.command.NetCommandDispatch;
import com.mcprohosting.bouncybungee.command.NetDelegate;
import com.mcprohosting.bouncybungee.util.ErrorHandler;
import com.mcprohosting.bouncybungee.util.TDatabaseManager;
import com.mcprohosting.bouncybungee.util.TPlugin;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 11/18/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
//TODO clankymicheael
public class BouncyBungee extends TPlugin implements TDatabaseManager {
    private static BouncyBungee instance;
    private Properties settings;
    private Properties strings;
    private NetCommandDispatch dispatch;
    private JedisPool jedisPool;

    public static BouncyBungee getInstance() {
        return BouncyBungee.instance;
    }

    @Override
    protected void start() {
        BouncyBungee.instance = this;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, this.host());
        this.dispatch = new NetCommandDispatch();
        this.getNetDispatch().registerNetCommands(new BaseReceiver());
        registerEvents(new BaseSender());
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public void returnJedis(Jedis jedis) {
        this.jedisPool.returnResource(jedis);
    }

    @Override
    protected void stop() {
    }

    public void reload() throws IOException {
        this.settings = new Properties();
        this.strings = new Properties();
        this.settings.load(getResourceAsStream("settings.properties"));
        this.strings.load(getResourceAsStream("strings.properties"));
    }
    public String getFormat(String key, boolean prefix, boolean color, String[]... datas) {
        String property = this.strings.getProperty(key);
        if (prefix) property = ChatColor.translateAlternateColorCodes('&', this.strings.getProperty("prefix")) + property;
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

    @Override
    public String database() {
        return this.settings.getProperty("database");  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String host() {
        return this.settings.getProperty("host");  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int port() {
        return Integer.parseInt(this.settings.getProperty("port"));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void preLoad() {
        try {
            reload();
        } catch (IOException e) {
            ErrorHandler.reportError(e);
        }
    }
    public NetCommandDispatch getNetDispatch() {
        return this.dispatch;
    }
}
