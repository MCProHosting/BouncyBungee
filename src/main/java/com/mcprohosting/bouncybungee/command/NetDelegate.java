package com.mcprohosting.bouncybungee.command;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.util.ErrorHandler;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * The NetDelegate is responsible for subscribing to the Jedis channel and dispatching
 * appropriate commands to the instance of the dispatch. It runs in it's own thread
 */
public class NetDelegate extends JedisPubSub implements Runnable {
    /**
     * The name of the channel to subscribe to. Used to send and recieve data. Do not get these out of sync, or this application
     * will not work whatsoever.
     */
    public static final String CHAN = "BUNGEE_LINK";
    @Override
    public void onMessage(String chan, String data) {
        if (!chan.equals(NetDelegate.CHAN)) return;
        JSONObject obj;
        try {
            obj = new JSONObject(data);
        } catch (JSONException ex) {
            ErrorHandler.reportError(ex);
            return;
        }
        if (!BouncyBungee.getInstance().getNetDispatch().handleCommand(obj)) {
            BouncyBungee.getInstance().getLogger().info("Failed to execute command " + obj.toString());
        }
    }

    @Override
    public void onPMessage(String s, String s2, String s3) {
    }

    @Override
    public void onSubscribe(String s, int i) {
    }

    @Override
    public void onUnsubscribe(String s, int i) {
    }

    @Override
    public void onPUnsubscribe(String s, int i) {
    }

    @Override
    public void onPSubscribe(String s, int i) {
    }

    @Override
    public void run() {
        Jedis jedis = BouncyBungee.getInstance().getJedis();
        jedis.subscribe(this, NetDelegate.CHAN);
        //BouncyBungee.getInstance().returnJedis(jedis);
    }
}
