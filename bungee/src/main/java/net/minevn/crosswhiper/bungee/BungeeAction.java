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
            main.sendMessage(sender, receiver, message);
        }
    },
    REPLY {
        @Override
        public void doAction(CWBungee main, DataInputStream data) throws IOException {
            ProxiedPlayer sender = main.getProxy().getPlayer(data.readUTF());
            String message = data.readUTF();
            UserData udata = UserData.getData(sender);
            ProxiedPlayer receiver = udata == null || udata.getLastMessage() == null
                    ? null
                    : main.getProxy().getPlayer(udata.getLastMessage());
            main.sendMessage(sender, receiver, message);
        }
    }
    ;

    public abstract void doAction(CWBungee main, DataInputStream data) throws IOException;
}
