package net.minevn.crosswhisper.bungee;

import net.md_5.bungee.api.ChatColor;
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
    private String m_self;
    private String m_norecent;
    private String m_targetselected;
    private String m_targetsent;
    private String m_targetreceived;

    @SuppressWarnings("ConstantConditions")
    public Configs(CWBungee main) {
        try {
            Configuration config = loadFromFile(new File(main.getDataFolder(), CWBungee.CONFIG));
            Configuration message = config.getSection("message");
            m_sent = ChatColor.translateAlternateColorCodes('&', message.getString("sent"));
            m_received = ChatColor.translateAlternateColorCodes('&', message.getString("received"));
            m_blocked = ChatColor.translateAlternateColorCodes('&', message.getString("blocked"));
            m_notonline = ChatColor.translateAlternateColorCodes('&', message.getString("not-online"));
            m_self = ChatColor.translateAlternateColorCodes('&', message.getString("self"));
            m_norecent = ChatColor.translateAlternateColorCodes('&', message.getString("no-recent"));
            m_targetsent = ChatColor.translateAlternateColorCodes('&', message.getString("target-sent"));
            m_targetreceived = ChatColor.translateAlternateColorCodes('&', message.getString("target-received"));
            m_targetselected = ChatColor.translateAlternateColorCodes('&', message.getString("target-selected"));
        } catch (Exception ex) {
            main.getLogger().log(Level.SEVERE, "Error loading config", ex);
        }
    }

    public String getTargetSelectedMessage() {
        return m_targetselected;
    }

    public String getTargetSentMessage() {
        return m_targetsent;
    }

    public String getTargetReceivedMessage() {
        return m_targetreceived;
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

    public String getSelfMessage() {
        return m_self;
    }

    public String getNoRecentMessage() {
        return m_norecent;
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

    public static void saveConfig(Configuration config, File file) {
        try {
            new Configuration();
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException ex) {
            CWBungee.getInstance().getLogger().log(Level.WARNING, "error saving file " + file.getName(), ex);
        }
    }
    //endregion
}
