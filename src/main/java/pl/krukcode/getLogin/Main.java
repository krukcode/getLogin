package pl.krukcode.getLogin;

import org.bukkit.plugin.java.JavaPlugin;
// | EN |
import pl.krukcode.getLogin.EN.cmds.admin.AdminCommandsEN;
import pl.krukcode.getLogin.EN.cmds.user.ChangePassEN;
import pl.krukcode.getLogin.EN.cmds.user.LoginEN;
import pl.krukcode.getLogin.EN.cmds.user.RegisterEN;
import pl.krukcode.getLogin.EN.events.onChatEN;
import pl.krukcode.getLogin.EN.events.onCommandEN;
import pl.krukcode.getLogin.EN.events.onJoinEN;
import pl.krukcode.getLogin.EN.events.onMoveEN;
// | PL |
import pl.krukcode.getLogin.PL.cmds.admin.AdminCommands;
import pl.krukcode.getLogin.PL.cmds.user.ChangePass;
import pl.krukcode.getLogin.PL.cmds.user.Login;
import pl.krukcode.getLogin.PL.cmds.user.Register;
import pl.krukcode.getLogin.PL.events.onChat;
import pl.krukcode.getLogin.PL.events.onCommand;
import pl.krukcode.getLogin.PL.events.onJoin;
import pl.krukcode.getLogin.PL.events.onMove;
// | PL |
import pl.krukcode.getLogin.Data.PlayerData;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Main main;
    PlayerData pd;

    public void onEnable() {
        main = this;
        reloadConfig();
        Util.setupConfig(main);
        Util.Setup(main, "a", Util.getStringFromConfig("language"));
        pd.setup(this);
        if (Util.getStringFromConfig("language").equals("PL")) {
            new ChangePass(this);
            new Login(this);
            new Register(this);
            new AdminCommands(this);

            getServer().getPluginManager().registerEvents(new onJoin(), this);
            getServer().getPluginManager().registerEvents(new onMove(), this);
            getServer().getPluginManager().registerEvents(new onChat(), this);
            getServer().getPluginManager().registerEvents(new onCommand(), this);
        } else {
            new ChangePassEN(this);
            new LoginEN(this);
            new RegisterEN(this);
            new AdminCommandsEN(this);

            getServer().getPluginManager().registerEvents(new onJoinEN(), this);
            getServer().getPluginManager().registerEvents(new onMoveEN(), this);
            getServer().getPluginManager().registerEvents(new onChatEN(), this);
            getServer().getPluginManager().registerEvents(new onCommandEN(), this);
        }
        Util.setStatus("LOADED", Objects.requireNonNull(getConfig().getString("language")));
    }

    public void onDisable() {
        reloadConfig();
        Util.setStatus("DISABLED", Objects.requireNonNull(getConfig().getString("language")));
    }

    public static Main getMain() { return main; }
    public static Main getInstance() { return main; }
    public Main() { pd = PlayerData.getInstance(); }
}
