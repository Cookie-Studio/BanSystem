package cn.cookiestudio.bansystem.command;

import cn.cookiestudio.bansystem.BanSystem;
import cn.cookiestudio.easy4form.window.BFormWindowSimple;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponseSimple;

import java.util.stream.Collectors;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        super("unbansystem", "unban system");
        this.setAliases(new String[]{"unbans"});
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            commandSender.sendMessage("This command cannot use in console");
            return true;
        }
        BFormWindowSimple.getBuilder()
                .setButtons(BanSystem.getInstance().getBanPlayerPool().getStorage().getKeys().stream().map(str -> new ElementButton(str,new ElementButtonImageData("path","textures/ui/icon_steve"))).collect(Collectors.toList()))
                .setTitle("Unban Player")
                .setResponseAction(e -> {
                    if (e.getResponse() == null)
                        return;
                    String player = ((FormResponseSimple)e.getResponse()).getClickedButton().getText();
                    BanSystem.getInstance().getBanPlayerPool().unban(player);
                    commandSender.sendMessage("successfully unban player" + player);
                })
        .build()
        .sendToPlayer((Player) commandSender);
        return true;
    }
}
