package net.minevn.crosswhisper.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {
    private UUID uuid;
    private boolean blocked;
    private List<String> blackList;
    private String lastMessage = null;
    private File configFile;
    private Configuration config;

    public UserData(UUID uuid) {
        this.uuid = uuid;
        configFile = new File(root, uuid.toString() + ".yml");
        config = Configs.loadFromFile(configFile);
        if (config == null) config = new Configuration();
        blocked = config.get("blocked", false);
        blackList = config.getStringList("black-list");
        getDataMap().put(uuid, this);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isBlackList(ProxiedPlayer other) {
        return blackList != null && blackList.contains(other.getName());
    }

    public void addBlackList(String name) {
        blackList.add(name);
        saveConfig();
    }

    public void removeBlackList(String name) {
        blackList.remove(name);
        saveConfig();
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String sender) {
        this.lastMessage = sender;
    }

    public void saveConfig() {
        config.set("blocked", blocked);
        config.set("black-list", blackList);
        Configs.saveConfig(config, configFile);
    }

    public void destroy() {
        getDataMap().remove(uuid);
    }

    //region static
    private static final File root = new File(CWBungee.getInstance().getDataFolder(), "userdata");
    private static Map<UUID, UserData> data;

    private static Map<UUID, UserData> getDataMap() {
        if (data == null) data = new ConcurrentHashMap<>();
        return data;
    }

    public static UserData getData(ProxiedPlayer player) {
        return  getDataMap().get(player.getUniqueId());
    }
    //endregion
}
