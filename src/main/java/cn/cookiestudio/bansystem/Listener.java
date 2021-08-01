package cn.cookiestudio.bansystem;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerJoinEvent;

public class Listener implements cn.nukkit.event.Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (BanSystem.getInstance().getBanPlayerPool().isBanned(event.getPlayer())){
            BanSystem.kick(event.getPlayer(),BanSystem.getInstance().getSetting().getString("bannedPlayerJoinMessage").replaceAll("\\{time\\}", String.valueOf(BanSystem.getInstance().getBanPlayerPool().getBanTime(event.getPlayer()))));
        }
    }
}
