package net.minevn.crosswhisper.bukkit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minevn.crosswhisper.models.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CWBukkit extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, Constants.CHANNEL_NAME);
//        this.getServer().getMessenger().registerIncomingPluginChannel(this, Constants.CHANNEL_NAME, this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    public void sendMessage(Player player, ByteArrayDataOutput data) {
        player.sendPluginMessage(this, Constants.CHANNEL_NAME, data.toByteArray());
    }

    public void sendWhisper(Player sender, String receiver, String message) {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("MESSAGE_SEND");
        data.writeUTF(sender.getName());
        data.writeUTF(receiver);
        data.writeUTF(message);
        sendMessage(sender, data);
    }

    public void sendReply(Player sender, String message) {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("REPLY");
        data.writeUTF(sender.getName());
        data.writeUTF(message);
        sendMessage(sender, data);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (command.getName().equals("w")) {
            if (args.length < 2) {
                player.sendMessage("Cách dùng: /msg <tên người nhận> <tin nhắn>");
                return true;
            }
            String receiver = args[0];
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            sendWhisper(player, receiver, message);
        }
        if (command.getName().equals("r")) {
            if (args.length < 1) {
                player.sendMessage("Cách dùng: /r <tin nhắn>");
                return true;
            }
            String message = String.join(" ", args);
            sendReply(player, message);
        }
        return true;
    }
}
