package net.minevn.crosswhiper.bungee;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configs {
    private String m_sent;
    private String m_received;
    private String m_blocked;
    private String m_notonline;

    @SuppressWarnings("ConstantConditions")
    public Configs(CWBungee main) {
        try {
            Configuration config = loadFromFile(new File(main.getDataFolder(), CWBungee.CONFIG));
            Configuration message = config.getSection("message");
            m_sent = message.getString("sent");
            m_received = message.getString("received");
            m_blocked = message.getString("blocked");
            m_notonline = message.getString("not-online");
        } catch (Exception ex) {
            main.getLogger().log(Level.SEVERE, "Error loading config", ex);
        }
    }

    public String getSentMessage() {
        return m_sent;
    }

    public String getReceivedMessage() {
        return m_received;
    }

    public String getBlockedMessage() {
        return m_blocked;
    }

    public String getNotOnlineMessage() {
        return m_notonline;
    }

    //region static
    public static Configuration loadFromFile(File file) {
        try {
            if (!file.exists()) return null;
            return ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(file);
        } catch (IOException ignored) {
            return null;
        }
    }
    //endregion
}
