package pl.krukcode.getLogin.PL.cmds.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class ChangePass implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public ChangePass(Main m) {
        plugin = m;
        m.getCommand("changepass").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoin.loggedIn.get(uuid).equals(true)) {
            if (args.length == 3) {
                String old = args[0];
                String new1 = args[1];
                String new2 = args[2];
                String actualPass = pd.getData().getString(uuid + ".pass");

                if(old.equals(actualPass)) {
                    if (new1.equals(new2)) {
                        pd.getData().set(uuid + ".pass", new2);
                        pd.saveData();

                        p.sendMessage("§7Twoje hasło zostało zmienione!");
                    } else {
                        p.sendMessage("§7Twoje nowe hasła się nie zgadzają!");
                    }
                } else {
                    p.sendMessage("§7Twoje stare hasło jest nieprawidłowe!");
                }
            } else {
                p.sendMessage("§7Użycie: §e/changepass [stare hasło] [nowe hasło] [powtórz nowe hasło]");
            }
        } else {
            p.sendMessage("§7Żeby zmienić hasło musisz się zalogować!");
        }
        return false;
    }
}