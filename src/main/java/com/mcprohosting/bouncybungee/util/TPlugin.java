package com.mcprohosting.bouncybungee.util;

import com.mcprohosting.bouncybungee.util.command.TCommandDispatch;
import com.mcprohosting.bouncybungee.util.command.TCommandHandler;
import com.mcprohosting.bouncybungee.util.cooldowns.TCooldownManager;
import com.mongodb.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 9/26/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TPlugin extends Plugin {
    private TCommandDispatch commandDispatch;
    private static DB mongoDB = null;
    @Override
    public void onEnable() {
        if (this instanceof TDatabaseManager) {
            ((TDatabaseManager)this).preLoad();
            getMongoDB();
        }
        this.commandDispatch = new TCommandDispatch(this);
        this.start();
    }
    @Override
    public void onDisable() {
        this.stop();
    }
    public DB getMongoDB() {
        if (TPlugin.mongoDB == null) {
            if (!(this instanceof TDatabaseManager)) return null;
            try {
                TPlugin.mongoDB = (new MongoClient(((TDatabaseManager)this).host(), ((TDatabaseManager)this).port())).getDB(((TDatabaseManager)this).database());
            } catch (UnknownHostException e) {
                ErrorHandler.reportError(e);
                return null;
            }
            TCooldownManager.database = TPlugin.mongoDB;
        }
        return TPlugin.mongoDB;
    }
    protected void configSet(String key, Object value) {
        DBObject config = this.getConfig();
        config.put(key, value);
        this.getCollection().save(config);
    }
    protected Object configGet(String key) {
        DBObject config = this.getConfig();
        if (!config.containsField(key)) return null;
        return config.get(key);
    }
    private DBCollection getCollection() {
        return this.getMongoDB().getCollection("bungee_config");
    }
    public DBObject getConfig() {
        BasicDBObject object = new BasicDBObject("pl_name", this.getDescription().getName());
        DBCursor cursor = this.getCollection().find();
        DBObject obj = null;
        if (cursor.count() == 0) {
            obj = object;
        }
        return (obj == null) ? cursor.next() : obj;
    }
    protected void registerCommandHandler(TCommandHandler handler) {
        this.commandDispatch.registerHandler(handler);
    }
    protected void registerEvents(Listener listener) {
        getProxy().getPluginManager().registerListener(this, listener);
    }
    protected abstract void start();
    protected abstract void stop();
}
