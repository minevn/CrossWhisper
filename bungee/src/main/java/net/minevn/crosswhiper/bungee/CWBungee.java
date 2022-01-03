package net.minevn.crosswhiper.bungee;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.minevn.crosswhiper.bungee.models.Constants;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CWBungee extends Plugin implements Listener {
    private Configs config;

    @Override
    public void onEnable() {
        _instance = this;
        getProxy().registerChannel(Constants.CHANNEL_NAME);
        getProxy().getPluginManager().registerListener(this, this);
        generateConfig();
        config = new Configs(this);
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
                getLogger().log(Level.SEVERE, "Can save config", e);
            }
        }
    }

    public Configs getConfig() {
        return config;
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

    //region singleton
    public static final String CONFIG = "config.yml";
    private static CWBungee _instance;

    public static CWBungee getInstance() {
        return _instance;
    }
    //endregion
}
