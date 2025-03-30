package pl.krukcode.getLogin.Data;

public class PlayerAPI {

    private static PlayerData pd;

    static {
        pd = PlayerData.getInstance();
    }
}
