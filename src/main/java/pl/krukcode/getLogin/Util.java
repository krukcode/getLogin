package pl.krukcode.getLogin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    static Main main;

    public static void Setup(Main m, String color, String language) {
        main = m;
        Util.language = language;
        setStatus("LOADING", language);
        setPrefix("&" + color + m.getDescription().getName());
        setVersion(m.getDescription().getVersion());
    }

    public static String getStringFromConfig(String optionInConfig) { return Main.getMain().getConfig().getString(optionInConfig); }

    public static void setStatus(String Status, String language) {
        if (language.equals("PL")) {
            switch (Status) {
                case "LOADING":
                    status = "ŁADOWANIE";
                    Bukkit.getConsoleSender().sendMessage("Ładowanie systemu...");
                    break;
                case "LOADED":
                    status = "ZAŁADOWANO";
                    sendToConsole(fix("&aZaładowano plugin! &7| &eWersja pluginu: &a" + getVersion() + " &7| &bObecny status: &c" + smallCaps(getStatus())));
                    sendToConsole(fix("&eDeveloper pluginu: &aKRUKCODE"));
                    break;
                case "DISABLED":
                    status = "WYŁĄCZONY";
                    Bukkit.getConsoleSender().sendMessage(fix("[" +  Main.getMain().getDescription().getName() + " Wyłączanie pluginu..."));
                    pluginPrefix = "null";
                    setVersion("V1.0");
                    Bukkit.getConsoleSender().sendMessage(fix(pluginPrefix + " Plugin wyłączony!"));
                    Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
                    break;
            }
        } else {
            switch (Status) {
                case "LOADING":
                    status = "LOADING";
                    Bukkit.getConsoleSender().sendMessage(fix(pluginPrefix + " Loading plugin..."));
                    break;
                case "LOADED":
                    status = "LOADED";
                    sendToConsole(fix("&aPlugin loaded! &7| &ePlugin version: &a" + getVersion() + " &7| &bCurrent status: &c" + smallCaps(getStatus())));
                    sendToConsole(fix("&ePlugin creator: &aKRUKCODE"));
                    break;
                case "DISABLED":
                    status = "DISABLED";
                    Bukkit.getConsoleSender().sendMessage(fix(pluginPrefix + " Disabling plugin..."));
                    pluginPrefix = "null";
                    setVersion("V1.0");
                    Bukkit.getConsoleSender().sendMessage(fix( "[" +  Main.getMain().getDescription().getName() + "] Plugin disabled!"));
                    Main.getMain().getPluginLoader().disablePlugin(Main.getMain());
                    break;
            }
        }
    }

    public static void setupConfig(Main m) {
        m.getConfig().options().copyDefaults(true);
        m.saveDefaultConfig();
    }

    private static String pluginPrefix = "null";
    private static String language = "null";
    private static String status = "";
    private static String version = "V1.0";
    public static void sendToConsole (String text) { if(!(pluginPrefix.equals("null"))) { Bukkit.getConsoleSender().sendMessage(fix(pluginPrefix + " " + text)); } else { Main.getMain().getLogger().log(Level.WARNING, "Setup Util using Util.Setup(main, 'minecraft color code', 'language [PL * EN]')"); } }
    public static void sendMessage (CommandSender sender, String text) { sender.sendMessage(Util.fix(text)); }
    public static String getVersion() { return version; }
    public static String getStatus() { return status; }

    private static void setPrefix(String prefix) { pluginPrefix = "&7[" + prefix + "&7]&r"; }
    private static void setVersion(String Version) { version = Version; }



    public static String fix(String text) {
        return fixGradient(ChatColor.translateAlternateColorCodes('&', text).replaceAll(">>", "»").replaceAll("<<", "«"));
    }

    public static String smallCaps(String text) {
        return text
                .replaceAll("a", "ᴀ")
                .replaceAll("b", "ʙ")
                .replaceAll("c", "ᴄ")
                .replaceAll("d", "ᴅ")
                .replaceAll("e", "ᴇ")
                .replaceAll("f", "ꜰ")
                .replaceAll("g", "ɢ")
                .replaceAll("h", "ʜ")
                .replaceAll("i", "ɪ")
                .replaceAll("j", "ᴊ")
                .replaceAll("k", "ᴋ")
                .replaceAll("l", "ʟ")
                .replaceAll("m", "ᴍ")
                .replaceAll("n", "ɴ")
                .replaceAll("o", "ᴏ")
                .replaceAll("p", "ᴘ")
                .replaceAll("r", "ʀ")
                .replaceAll("s", "ѕ")
                .replaceAll("t", "ᴛ")
                .replaceAll("u", "ᴜ")
                .replaceAll("w", "ᴡ")
                .replaceAll("y", "ʏ")
                .replaceAll("z", "ᴢ")
                .replaceAll("A", "ᴀ")
                .replaceAll("B", "ʙ")
                .replaceAll("C", "ᴄ")
                .replaceAll("D", "ᴅ")
                .replaceAll("E", "ᴇ")
                .replaceAll("F", "ꜰ")
                .replaceAll("G", "ɢ")
                .replaceAll("H", "ʜ")
                .replaceAll("I", "ɪ")
                .replaceAll("J", "ᴊ")
                .replaceAll("K", "ᴋ")
                .replaceAll("L", "ʟ")
                .replaceAll("M", "ᴍ")
                .replaceAll("N", "ɴ")
                .replaceAll("O", "ᴏ")
                .replaceAll("P", "ᴘ")
                .replaceAll("Q", "ǫ")
                .replaceAll("R", "ʀ")
                .replaceAll("S", "ѕ")
                .replaceAll("T", "ᴛ")
                .replaceAll("U", "ᴜ")
                .replaceAll("V", "ᴠ")
                .replaceAll("W", "ᴡ")
                .replaceAll("X", "х")
                .replaceAll("Y", "ʏ")
                .replaceAll("Z", "ᴢ");

    }

    // --Commented out by Inspection START (16.03.2025 10:00):
//    public static List<String> fix(List<String> strings) {
//        strings.replaceAll(Util::fix);
//
//        return strings;
//    }
// --Commented out by Inspection STOP (16.03.2025 10:00)
    public static String fixGradient(String string) {
        Pattern pattern = Pattern.compile("&(#[a-fA-F0-9]{6})");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start() + 1, matcher.end());
            string = string.replace("&" + color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }
}
