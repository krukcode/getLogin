package pl.krukcode.getLogin.PL.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.krukcode.getLogin.Main;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.Util;
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.Objects;
import java.util.UUID;

public class AdminCommands implements CommandExecutor {

    public AdminCommands(Main m) {
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
                            "\n\n&eKomendy:\n" +
                            " &bGracze:\n" +
                            "  &7- &d/login [hasło]\n" +
                            "  &7- &d/register [hasło] [hasło]\n" +
                            "  &7- &d/changepass [stare hasło] [nowe hasło] [powtórz nowe hasło]\n" +
                            " &bAdmini:\n" +
                            "  &7- &d/getlogin login [gracz]\n" +
                            "  &7- &d/getlogin register [gracz]\n" +
                            "  &7- &d/getlogin unregister [gracz]\n" +
                            "  &7- &d/getlogin changepass [gracz] [hasło]\n" +
                            "  &7- &d/getlogin reload\n" +
                            "  &7- &d/getlogin help\n"
            ));
            plr.playSound(plr.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 1);
            return false;
        }

        if (strings[0].equals("reload")) {
            Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
            Main.getMain().getPluginLoader().enablePlugin(Main.getMain());
            p.sendMessage(Util.fix("&aPomyślnie przeładowano plugin!"));
            return false;
        }

        String toLoginNick = strings[1];
        Player toLoginPlr = Bukkit.getPlayer(toLoginNick);
        if(!toLoginPlr.isOnline() && strings[0].equals("login")) { p.sendMessage(Util.fix("&cTen gracz jest offline!")); return false; }
        UUID uuid = toLoginPlr.getUniqueId();
        pd = PlayerData.getInstance();
        switch (strings[0].toLowerCase()) {
            case "login":
                if(onJoin.loggedIn.get(uuid)) { p.sendMessage(Util.fix("&cTen gracz jest już zalogowany!")); return false; }

                onJoin.loggedIn.put(uuid, true);
                p.sendMessage(Util.fix("&aPomyślnie zalogowano gracza &e" + toLoginNick));
                toLoginPlr.sendMessage(Util.fix("&aZostałeś zalogowany przez admina: &e" + p.getName()));
                break;
            case "register":
                if(!toLoginPlr.isOnline()) { p.sendMessage(Util.fix("&cTen gracz jest offline!")); return false; }
                if(!(pd.getData().getString(uuid + ".pass") == null)) { p.sendMessage(Util.fix("&cTen gracz jest już zarejestrowany!")); return false; }

                int rand = (int)(Math.random() + 50 + Math.random() + Math.random() + 105 + 300 * Math.random());
                pd.getData().set(uuid + ".pass", rand);
                p.sendMessage(Util.fix("&aPomyślnie zarejestrowano gracza &e" + toLoginNick + "\n\n&cJego hasło to: &e" + rand));
                toLoginPlr.kickPlayer(Util.fix("&aZostałeś zarejestrowany przez admina: &e" + p.getName() + "\n\n&cTwoje hasło: &e" + rand + "\n\n&aWejdź ponownie na serwer i zaloguj się!"));
                break;
            case "unregister":
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&cTen gracz nie jest zarejestrowany!")); return false; }

                pd.getData().set(uuid + ".pass", null);
                p.sendMessage(Util.fix("&aPomyślnie odrejestrowano gracza &e" + toLoginNick));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aZostałeś odrejestrowany przez: &e" + p.getName() + "\n\n&aDołącz ponownie na serwer i zarejestruj się!")); }
                break;
            case "changepass":
                if (!(strings.length == 2)) { p.sendMessage(Util.fix("&7Użycie: &e/getLogin changepass [gracz] [hasło]&7.")); return false; }
                if(pd.getData().getString(uuid + ".pass") == null) { p.sendMessage(Util.fix("&cTen gracz nie jest zarejestrowany!")); return false; }

                pd.getData().set(uuid + ".pass", strings[2]);
                p.sendMessage(Util.fix("&aPomyślnie zmieniono hasło gracza &e" + toLoginNick + "\n\n&cJego hasło to: &e" + strings[2]));
                if (toLoginPlr.isOnline()) { toLoginPlr.kickPlayer(Util.fix("&aTwoje hasło zostało zmienione przez: &e" + p.getName() + "\n\n&cTwoje hasło to: &e" + strings[2] + "\n\n&aDołącz ponownie na serwer i zaloguj się tym hasłem!")); }
                break;
        }
        return false;
    }
}
