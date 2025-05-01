package pl.krukcode.getLogin.EN.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.Util.Util;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class AdminCommandsEN implements CommandExecutor {

    public AdminCommandsEN(Main m) {
        Objects.requireNonNull(m.getCommand("getLogin")).setExecutor(this);
    }

    PlayerData pd;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            if(strings.length == 0 || !strings[0].equals("login") && !strings[0].equals("register") && !strings[0].equals("unregister") && !strings[0].equals("changepass") && !strings[0].equals("reload")) {
                commandSender.sendMessage(Util.getHelpMessage());
            }
            return false;
        }
        Player plr = (Player) commandSender;
        if(!commandSender.hasPermission("getLogin.admin")) { Util.sendMessage(commandSender, Util.getStringFromConfig("no_permission_message")); return false; }
        if(strings.length == 0 || !strings[0].equals("login") && !strings[0].equals("register") && !strings[0].equals("unregister") && !strings[0].equals("changepass") && !strings[0].equals("reload")) {
            commandSender.sendMessage(Util.getHelpMessage());
            plr.playSound(plr.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
            return false;
        }

        if (strings[0].equals("reload")) {
            try {
                Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
                Main.getMain().getPluginLoader().enablePlugin(Main.getMain());
                PlayerData.getInstance().reloadData();
            } catch (Exception exception) {
                Main.getMain().getLogger().log(Level.WARNING, String.valueOf(exception));
            }
            commandSender.sendMessage(Util.fix("&aSuccessfully reloaded plugin!"));
            return false;
        }

        String toLoginNick = strings[1];
        Player toLoginPlr = Bukkit.getPlayer(toLoginNick);
        assert toLoginPlr != null;
        if(!toLoginPlr.isOnline() && strings[0].equals("login")) { commandSender.sendMessage(Util.fix("&cThis player is offline!")); return false; }
        UUID uuid = toLoginPlr.getUniqueId();
        pd = PlayerData.getInstance();
        switch (strings[0].toLowerCase()) {
            case "login":
                if(onJoinEN.loggedIn.get(uuid)) { commandSender.sendMessage(Util.fix("&cThis player is already logged in!")); return false; }

                onJoinEN.loggedIn.put(uuid, true);
                commandSender.sendMessage(Util.fix("&aSuccessfully logged in player &e" + toLoginNick));
                toLoginPlr.sendMessage(Util.fix("&aYou have been logged in by: &e" + commandSender.getName()));
                break;
            case "register":
                if(pd.getData().getString(uuid + ".pass") != null) { commandSender.sendMessage(Util.fix("&cThis player is already registered!")); return false; }

                int rand = (int)(Math.random() + 50 + Math.random() + Math.random() + 105 + 300 * Math.random() + Math.random() * Math.random() * 300);
                pd.getData().set(uuid + ".pass", rand);
                commandSender.sendMessage(Util.fix("&aSuccessfully registered &e" + toLoginNick + "\n\n&cHis pass is: &e" + rand));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYou are registered by admin: &e" + commandSender.getName() + "\n\n&cYour pass is: &e" + rand + "\n\n&aRejoin the server and login with this password!")); }
                break;
            case "unregister":
                if(pd.getData().getString(uuid + ".pass") == null) { commandSender.sendMessage(Util.fix("&cThis player is already unregistered!")); return false; }

                pd.getData().set(uuid + ".pass", null);
                commandSender.sendMessage(Util.fix("&aSuccessfully unregistered &e" + toLoginNick));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYou are unregistered by admin: &e" + commandSender.getName() + "\n\n&aRejoin the server and register!")); }
                break;
            case "changepass":
                if (!(strings.length == 2)) { commandSender.sendMessage(Util.fix("&7Usage: &e/getLogin changepass [player] [password]&7.")); return false; }
                if(pd.getData().getString(uuid + ".pass") == null) { commandSender.sendMessage(Util.fix("&cThis player is not registered!")); return false; }

                pd.getData().set(uuid + ".pass", strings[2]);
                commandSender.sendMessage(Util.fix("&aSuccessfully changed pass for &e" + toLoginNick + "\n\n&cHis pass is: &e" + strings[2]));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aYour password has been changed by: &e" + commandSender.getName() + "\n\n&cYour pass is: &e" + strings[2] + "\n\n&aRejoin the server and login with this password!")); }
                break;
            default:
                throw new IllegalStateException("Unknown argument!");
        }
        return false;
    }
}
