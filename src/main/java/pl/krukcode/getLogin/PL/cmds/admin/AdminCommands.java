package pl.krukcode.getLogin.PL.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.Util.Util;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.Objects;
import java.util.UUID;

public class AdminCommands implements CommandExecutor {

    public AdminCommands(Main m) {
        Objects.requireNonNull(m.getCommand("getLogin")).setExecutor(this);
    }

    PlayerData pd;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            if(strings.length == 0 || !strings[0].equals("login") && !strings[0].equals("register") && !strings[0].equals("unregister") && !strings[0].equals("changepass") && !strings[0].equals("reload")) {
                commandSender.sendMessage(Util.getHelpMessage());
                return false;
            }
        }
        Player plr = null;
        if (commandSender instanceof Player) { plr = (Player) commandSender; }
        if(!commandSender.hasPermission("getLogin.admin")) { Util.sendMessage(commandSender, Util.getStringFromConfig("no_permission_message")); return false; }
        if(strings.length == 0 || !strings[0].equals("login") && !strings[0].equals("register") && !strings[0].equals("unregister") && !strings[0].equals("changepass") && !strings[0].equals("reload")) {
            commandSender.sendMessage(Util.getHelpMessage());
            if (commandSender instanceof Player) { plr.playSound(plr.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1); }
            return false;
        }

        if (strings[0].equals("reload")) {
            Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
            Main.getMain().getPluginLoader().enablePlugin(Main.getMain());
            PlayerData.getInstance().reloadData();
            commandSender.sendMessage(Util.fix("&aPomyślnie przeładowano plugin!"));
            return false;
        }

        String toLoginNick = strings[1];
        Player toLoginPlr = Bukkit.getPlayer(toLoginNick);
        assert toLoginPlr != null;
        if(!toLoginPlr.isOnline() && strings[0].equals("login")) { commandSender.sendMessage(Util.fix("&cTen gracz jest offline!")); return false; }
        UUID uuid = toLoginPlr.getUniqueId();
        pd = PlayerData.getInstance();
        switch (strings[0].toLowerCase()) {
            case "login":
                if(onJoin.loggedIn.get(uuid)) { commandSender.sendMessage(Util.fix("&cTen gracz jest już zalogowany!")); return false; }

                onJoin.loggedIn.put(uuid, true);
                onJoin.stop.put(uuid, true);
                commandSender.sendMessage(Util.fix("&aPomyślnie zalogowano gracza &e" + toLoginNick));
                toLoginPlr.sendMessage(Util.fix("&aZostałeś zalogowany przez admina: &e" + commandSender.getName()));
                break;
            case "register":
                if(!toLoginPlr.isOnline()) { commandSender.sendMessage(Util.fix("&cTen gracz jest offline!")); return false; }
                if(!(pd.getData().getString(uuid + ".pass") == null)) { commandSender.sendMessage(Util.fix("&cTen gracz jest już zarejestrowany!")); return false; }

                int rand = (int)(Math.random() + 50 + Math.random() + Math.random() + 105 + 300 * Math.random());
                pd.getData().set(uuid + ".pass", rand);
                pd.getData().set(uuid + ".autologin", false);
                commandSender.sendMessage(Util.fix("&aPomyślnie zarejestrowano gracza &e" + toLoginNick + "\n\n&cJego hasło to: &e" + rand));
                toLoginPlr.kickPlayer(Util.fix("&aZostałeś zarejestrowany przez admina: &e" + commandSender.getName() + "\n\n&cTwoje hasło: &e" + rand + "\n\n&aWejdź ponownie na serwer i zaloguj się!"));
                break;
            case "unregister":
                if(pd.getData().getString(uuid + ".pass") == null) { commandSender.sendMessage(Util.fix("&cTen gracz nie jest zarejestrowany!")); return false; }

                pd.getData().set(uuid + ".pass", null);
                commandSender.sendMessage(Util.fix("&aPomyślnie odrejestrowano gracza &e" + toLoginNick));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aZostałeś odrejestrowany przez: &e" + commandSender.getName() + "\n\n&aDołącz ponownie na serwer i zarejestruj się!")); }
                break;
            case "changepass":
                if (!(strings.length == 3)) { commandSender.sendMessage(Util.fix("&7Użycie: &e/getLogin changepass [gracz] [hasło]&7.")); return false; }
                if(pd.getData().getString(uuid + ".pass") == null) { commandSender.sendMessage(Util.fix("&cTen gracz nie jest zarejestrowany!")); return false; }

                pd.getData().set(uuid + ".pass", strings[2]);
                commandSender.sendMessage(Util.fix("&aPomyślnie zmieniono hasło gracza &e" + toLoginNick + "\n\n&cJego hasło to: &e" + strings[2]));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aTwoje hasło zostało zmienione przez: &e" + commandSender.getName() + "\n\n&cTwoje hasło to: &e" + strings[2] + "\n\n&aDołącz ponownie na serwer i zaloguj się tym hasłem!")); }
                break;
        }
        return false;
    }
}
