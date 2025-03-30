package pl.krukcode.getLogin.EN.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Util;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.Objects;
import java.util.UUID;

public class AdminCommandsEN implements CommandExecutor {

    public AdminCommandsEN(Main m) {
        Objects.requireNonNull(m.getCommand("getLogin")).setExecutor(this);
    }

    PlayerData pd;

    @SuppressWarnings("static-access")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CommandSender p = commandSender;
        Player plr = (Player) commandSender;
        if(!p.hasPermission("getLogin.admin")) { Util.sendMessage(p, Util.getStringFromConfig("no_permission_message")); return false; }
        if(strings[0].isEmpty() || !strings[0].equals("login") && !strings[0].equals("register") && !strings[0].equals("unregister") && !strings[0].equals("changepass") && !strings[0].equals("reload")) {
            p.sendMessage(Util.fix(
                    "                     &agetLogin " + Util.getVersion() +
                        "\n\n&eCommands:\n" +
                        " &bPlayers:\n" +
                        "  &7- &d/login [password]\n" +
                        "  &7- &d/register [password] [password]\n" +
                        "  &7- &d/changepass [old pass] [new pass] [retype new pass]\n" +
                        " &bAdmins:\n" +
                        "  &7- &d/getlogin login [player]\n" +
                        "  &7- &d/getlogin register [player]\n" +
                        "  &7- &d/getlogin unregister [player]\n" +
                        "  &7- &d/getlogin changepass [player] [password]\n" +
                        "  &7- &d/getlogin reload\n" +
                        "  &7- &d/getlogin help\n"
            ));
            plr.playSound(plr.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
            return false;
        }

        if (strings[0].equals("reload")) {
            Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
            Main.getMain().getPluginLoader().enablePlugin(Main.getMain());
            p.sendMessage(Util.fix("&aSuccessfully reloaded plugin!"));
            return false;
        }

        String toLoginNick = strings[1];
        Player toLoginPlr = Bukkit.getPlayer(toLoginNick);
        if(!toLoginPlr.isOnline() && strings[0].equals("login")) { p.sendMessage(Util.fix("&cThis player is offline!")); return false; }
        UUID uuid = toLoginPlr.getUniqueId();
        pd = PlayerData.getInstance();
        switch (strings[0].toLowerCase()) {
            case "login":
                if(onJoinEN.loggedIn.get(uuid)) { p.sendMessage(Util.fix("&cThis player is already logged in!")); return false; }

                onJoinEN.loggedIn.put(uuid, true);
                p.sendMessage(Util.fix("&aSuccessfully logged in player &e" + toLoginNick));
                toLoginPlr.sendMessage(Util.fix("&aYou have been logged in by: &e" + p.getName()));
                break;
            case "register":
                if(pd.getData().getString(uuid + ".pass") != null) { p.sendMessage(Util.fix("&cThis player is already registered!")); return false; }

                int rand = (int)(Math.random() + 50 + Math.random() + Math.random() + 105 + 300 * Math.random());
                pd.getData().set(uuid + ".pass", rand);
                p.sendMessage(Util.fix("&aSuccessfully registered &e" + toLoginNick + "\n\n&cHis pass is: &e" + rand));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYou are registered by admin: &e" + p.getName() + "\n\n&cYour pass is: &e" + rand + "\n\n&aRejoin the server and login with this password!")); }
                break;
            case "unregister":
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&cThis player is already unregistered!")); return false; }

                pd.getData().set(uuid + ".pass", null);
                p.sendMessage(Util.fix("&aSuccessfully unregistered &e" + toLoginNick));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYou are unregistered by admin: &e" + p.getName() + "\n\n&aRejoin the server and register!")); }
                break;
            case "changepass":
                if (!(strings.length == 2)) { p.sendMessage(Util.fix("&7Usage: &e/getLogin changepass [player] [password]&7.")); return false; }
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&cThis player is not registered!")); return false; }

                pd.getData().set(uuid + ".pass", strings[2]);
                p.sendMessage(Util.fix("&aSuccessfully changed pass for &e" + toLoginNick + "\n\n&cHis pass is: &e" + strings[2]));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYour password has been changed by: &e" + p.getName() + "\n\n&cYour pass is: &e" + strings[2] + "\n\n&aRejoin the server and login with this password!")); }
                break;
        }
        return false;
    }
}
