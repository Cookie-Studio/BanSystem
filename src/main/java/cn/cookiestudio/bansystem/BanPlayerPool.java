package cn.cookiestudio.bansystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class BanPlayerPool {

    private Config storage = new Config(BanSystem.getInstance().getDataFolder() + "/ban.yml");

    {
        Server.getInstance().getScheduler().scheduleRepeatingTask(BanSystem.getInstance(),() -> {
            refresh();
        },20 * 60);
    }

    public void ban(Player player,int minute , String reason){
        ban(player.getName(),minute,reason);
    }

    public void ban(String player,int minute , String reason){
        if (Server.getInstance().getOnlinePlayers().values().stream().map(p -> p.getName()).collect(Collectors.toList()).contains(player)){
            BanSystem.kick(Server.getInstance().getPlayer(player),reason);
        }
        storage.set(player,minute);
        storage.save();
    }

    public void unban(String player){
        this.storage.remove(player);
        this.storage.save();
    }

    public boolean isBanned(Player player){
        return isBanned(player.getName());
    }

    public boolean isBanned(String player){
        return storage.exists(player);
    }

    public int getBanTime(Player player){
        return getBanTime(player.getName());
    }

    public int getBanTime(String player){
        return storage.exists(player) ? storage.getInt(player) : 0;
    }

    public void refresh(){
        storage.getAll().entrySet().forEach(entry -> {
            if ((int)storage.get(entry.getKey()) - 1 == 0){
                storage.remove(entry.getKey());
                storage.save();
                return;
            }
            storage.set(entry.getKey(),(int)entry.getValue() - 1);
            storage.save();
        });
    }
}
