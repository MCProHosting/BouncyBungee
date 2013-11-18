package com.mcprohosting.bouncybungee.util;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 9/26/13
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TDatabaseManager {
    public String database();
    public String host();
    public int port();
    public void preLoad();
}
