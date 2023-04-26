package net.minevn.crosswhisper.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.minevn.crosswhisper.models.Constants;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CWBungee extends Plugin implements Listener {
    private Configs config;
    private ConcurrentHashMap<ProxiedPlayer, String> spyMap;

    @Override
    public void onEnable() {
        _instance = this;
        getProxy().registerChannel(Constants.CHANNEL_NAME);
        getProxy().getPluginManager().registerListener(this, this);
        generateConfig();
        config = new Configs(this);
        spyMap = new ConcurrentHashMap<>();
    }

    private void generateConfig() {
        File root = getDataFolder();
        if (!root.exists()) root.mkdir();
        File data = new File(root, "userdata");
        if (!data.exists()) data.mkdir();
        File config = new File(root, CONFIG);
        if (!config.exists()) {
            try (InputStream in = getResourceAsStream("bungeeconfig.yml")) {
                Files.copy(in, config.toPath());
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Can't save config", e);
            }
        }
    }

    public Configs getConfig() {
        return config;
    }

    public void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
        if (sender == null || !sender.isConnected()) return;
        if (sender == receiver) {
            sender.sendMessage(getConfig().getSelfMessage());
            return;
        }
        UserData sdata = UserData.getData(sender);
        UserData rdata;
        if (receiver == null || !receiver.isConnected() || (rdata = UserData.getData(receiver)) == null) {
            sender.sendMessage(getConfig().getNotOnlineMessage());
            return;
        }
        if (rdata.isBlocked() || rdata.isBlackList(sender)) {
            sender.sendMessage(getConfig().getBlockedMessage());
            return;
        }
        rdata.setLastMessage(sender.getName());
        sdata.setLastMessage(receiver.getName());
        receiver.sendMessage(getConfig().getReceivedMessage()
                .replace("%sender%", sender.getName())
                .replace("%message%", message)
        );
        sender.sendMessage(getConfig().getSentMessage()
                .replace("%receiver%", receiver.getName())
                .replace("%message%", message)
        );

        for (Map.Entry<ProxiedPlayer, String> entry : spyMap.entrySet()) {
            ProxiedPlayer spy = entry.getKey();
            String target = entry.getValue();

            if (target.equals(sender.getName()) || target.equals("all")) {
                spy.sendMessage(
                        config.getTargetSentMessage()
                                .replace("%target%", sender.getName())
                                .replace("%receiver%", receiver.getName())
                                .replace("%message%", message)
                );
            } else if (target.equals(receiver.getName())) {
                spy.sendMessage(
                        config.getTargetReceivedMessage()
                                .replace("%sender%", sender.getName())
                                .replace("%target%", receiver.getName())
                                .replace("%message%", message)
                );
            }
        }
    }

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        if (!e.getTag().equals(Constants.CHANNEL_NAME)) return;
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(e.getData()));
        try {
            BungeeAction.valueOf(data.readUTF()).doAction(this, data);
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Error processing packet", ex);
        }
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        new UserData(e.getConnection().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        UserData data = UserData.getData(e.getPlayer());
        if (data != null) data.destroy();

        spyMap.remove(e.getPlayer());
    }

    //region singleton
    public static final String CONFIG = "config.yml";
    private static CWBungee _instance;

    public static CWBungee getInstance() {
        return _instance;
    }
    //endregion


    public ConcurrentHashMap<ProxiedPlayer, String> getSpyMap() {
        return spyMap;
    }
}
