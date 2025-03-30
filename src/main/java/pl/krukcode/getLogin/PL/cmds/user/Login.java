package pl.krukcode.getLogin.PL.cmds.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.UUID;

public class Login implements CommandExecutor {

    Main plugin;
    PlayerData pd;

    public Login(Main m) {
        plugin = m;
        m.getCommand("login").setExecutor(this);

        pd = PlayerData.getInstance();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String lab, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if(onJoin.loggedIn.get(uuid) == false) {
            if (args.length == 1) {
                if (pd.getData().getString(uuid + ".pass") != null) {
                    String pass = args[0];
                    String actualPass = pd.getData().getString(uuid + ".pass");

                    if(pass.equals(actualPass)) {
                        onJoin.loggedIn.put(uuid, true);

                        p.sendMessage("§aPomyślnie zalogowano!");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
                        onJoin.stop.put(uuid, true);
                    } else {
                        p.kickPlayer("§cWpisałeś złe hasło!\n§7Spróbuj ponownie później!");
                    }
                } else {
                    p.sendMessage("§7Zanim się zalogujesz §emusisz się zarejestrować!");
                    p.sendMessage("§7Użyj komendy §e/register [hasło] [powtórz hasło]");
                }
            } else {
                p.sendMessage("§7Użycie: §e/login [hasło]");
            }
        } else {
            p.sendMessage("§cJesteś już zalogowany!");
        }
        return false;
    }
}
