package pl.krukcode.getLogin.EN.cmds.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class ChangePassEN implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public ChangePassEN(Main m) {
        plugin = m;
        m.getCommand("changepass").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoinEN.loggedIn.get(uuid).equals(true)) {
            if (args.length == 3) {
                String old = args[0];
                String new1 = args[1];
                String new2 = args[2];
                String actualPass = pd.getData().getString(uuid + ".pass");

                if(old.equals(actualPass)) {
                    if (new1.equals(new2)) {
                        pd.getData().set(uuid + ".pass", new2);
                        pd.saveData();

                        p.sendMessage("§7Your password has been changed!");
                    } else {
                        p.sendMessage("§7Your new passwords aren't same!");
                    }
                } else {
                    p.sendMessage("§7Your old password is invalid!");
                }
            } else {
                p.sendMessage("§7Usage: §e/changepass [old pass] [new pass] [retype new pass]");
            }
        } else {
            p.sendMessage("§7You must login before changing password!");
        }
        return false;
    }
}