package cn.cookiestudio.bansystem.command;

import cn.cookiestudio.bansystem.BanSystem;
import cn.cookiestudio.easy4form.window.BFormWindowCustom;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import java.util.stream.Collectors;

public class BanCommand extends Command {
    public BanCommand() {
        super("bansystem","ban system");
        this.setAliases(new String[]{"bans"});
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            commandSender.sendMessage("This command cannot use in console");
            return true;
        }
        if (!commandSender.isOp()){
            commandSender.sendMessage("I'm sorry,but you haven't op to run this command");
            return true;
        }
        Player sender = (Player)commandSender;
        BFormWindowCustom.getBuilder()
                .addElements(new ElementDropdown("选择玩家：", Server.getInstance().getOnlinePlayers().values().stream().map(p -> p.getName()).collect(Collectors.toList())))
                .addElements(new ElementInput("如果玩家不在线，在这里写名字:"))
                .addElements(new ElementInput("原因:"))
                .addElements(new ElementInput("封禁时间（单位：分钟）："))
                .setTitle("Ban Player")
                .setResponseAction(e -> {
                    if (e.getResponse() == null)
                        return;
                    FormResponseCustom response = (FormResponseCustom)e.getResponse();
                    String reason = response.getInputResponse(2);
                    int time = Integer.parseInt(response.getInputResponse(3));
                    String name = String.valueOf(response.getDropdownResponse(0).getElementContent());
                    if (Server.getInstance().getOnlinePlayers().values().stream().map(p -> p.getName()).collect(Collectors.toList()).contains(name) && response.getInputResponse(1).isEmpty()){
                        BanSystem.getInstance().getBanPlayerPool().ban(name,time,reason);
                        Server.getInstance().broadcastMessage(BanSystem.getInstance().getSetting().getString("banPlayerMessage").replaceAll("\\{player\\}",name).replaceAll("\\{reason\\}",reason).replaceAll("\\{time\\}", String.valueOf(time)).replaceAll("\\{op\\}",sender.getName()));
                        return;
                    }
                    name = response.getInputResponse(1);
                    Server.getInstance().broadcastMessage(BanSystem.getInstance().getSetting().getString("banPlayerMessage").replaceAll("\\{player\\}",name).replaceAll("\\{reason\\}",reason).replaceAll("\\{time\\}", String.valueOf(time)).replaceAll("\\{op\\}",sender.getName()));
                    BanSystem.getInstance().getBanPlayerPool().ban(name,time,reason);
                    return;
                }).build().sendToPlayer(sender);
        return true;
    }
}
