package com.mcprohosting.bouncybungee.util;

import com.mcprohosting.bouncybungee.util.command.TCommandDispatch;
import com.mcprohosting.bouncybungee.util.command.TCommandHandler;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 9/26/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TPlugin extends Plugin {
    private TCommandDispatch commandDispatch;
    @Override
    public void onEnable() {
        if (this instanceof TDatabaseManager) {
            ((TDatabaseManager)this).preLoad();
        }
        this.commandDispatch = new TCommandDispatch(this);
        this.start();
    }
    @Override
    public void onDisable() {
        this.stop();
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
