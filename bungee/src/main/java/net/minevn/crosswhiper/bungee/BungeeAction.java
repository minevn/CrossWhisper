package net.minevn.crosswhiper.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;

public enum BungeeAction {
    MESSAGE_SEND {
        @Override
        public void doAction(CWBungee main, DataInputStream data) throws IOException {
            ProxiedPlayer sender = main.getProxy().getPlayer(data.readUTF());
            ProxiedPlayer receiver = main.getProxy().getPlayer(data.readUTF());
            String message = data.readUTF();
            if (sender == null || !sender.isConnected()) return;
            if (receiver == null || receiver.isConnected()) {
                sender.sendMessage(main.getConfig().getNotOnlineMessage());
            }
        }
    },
    ;

    public abstract void doAction(CWBungee main, DataInputStream data) throws IOException;
}
