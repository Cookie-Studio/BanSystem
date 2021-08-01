package cn.cookiestudio.bansystem;

import cn.cookiestudio.bansystem.command.BanCommand;
import cn.cookiestudio.bansystem.command.UnbanCommand;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.DisconnectPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;

@Getter
public class BanSystem extends PluginBase {

    @Getter
    private static BanSystem instance = null;
    private BanPlayerPool banPlayerPool;
    private Config setting;

    @Override
    public void onEnable() {
        instance = this;
        this.saveResource("setting.yml");
        setting = new Config(this.getDataFolder() + "/setting.yml");
        banPlayerPool = new BanPlayerPool();
        Server.getInstance().getPluginManager().registerEvents(new Listener(),this);
        Server.getInstance().getCommandMap().register("",new BanCommand());
        Server.getInstance().getCommandMap().register("",new UnbanCommand());
    }

    @Override
    public void onDisable() {
        banPlayerPool.refresh();
    }

    public static void kick(Player player,String reason){
        DisconnectPacket packet = new DisconnectPacket();
        packet.message = reason;
        player.dataPacket(packet);
    }
}
