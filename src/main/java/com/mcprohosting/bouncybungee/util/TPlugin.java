package com.mcprohosting.bouncybungee.util;

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
    @Override
    public void onEnable() {
        this.start();
    }
    @Override
    public void onDisable() {
        this.stop();
    }
    protected void registerEvents(Listener listener) {
        getProxy().getPluginManager().registerListener(this, listener);
    }
    protected abstract void start();
    protected abstract void stop();
}
