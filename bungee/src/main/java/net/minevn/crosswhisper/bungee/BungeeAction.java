package net.minevn.crosswhisper.bungee;

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
            if (udata.getLastMessage() == null) {
                sender.sendMessage(main.getConfig().getNoRecentMessage());
                return;
            }
            ProxiedPlayer receiver = main.getProxy().getPlayer(udata.getLastMessage());
            main.sendMessage(sender, receiver, message);
        }
    },
    SPY {
        @Override
        public void doAction(CWBungee main, DataInputStream data) throws IOException {
            ProxiedPlayer spy = main.getProxy().getPlayer(data.readUTF());
            String target = data.readUTF();
            main.getSpyMap().put(spy, target);
            spy.sendMessage(main.getConfig().getTargetSelectedMessage().replace("%target%", target));
        }
    };

    public abstract void doAction(CWBungee main, DataInputStream data) throws IOException;
}
