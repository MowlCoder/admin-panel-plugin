package me.mowlcoder.adminpanelplugin;

import me.mowlcoder.adminpanelplugin.commands.AdminPanelCMD;
import me.mowlcoder.adminpanelplugin.events.AdminGUIEvents;
import me.mowlcoder.adminpanelplugin.events.GUIEvents;
import me.mowlcoder.adminpanelplugin.events.PlayerControlGUIEvents;
import me.mowlcoder.adminpanelplugin.events.PlayersGUIEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminPanelPlugin extends JavaPlugin {

    private static AdminPanelPlugin instance;

    public static AdminPanelPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getCommand("admin-panel").setExecutor(new AdminPanelCMD());
        getServer().getPluginManager().registerEvents(new GUIEvents(), this);
        getServer().getPluginManager().registerEvents(new AdminGUIEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayersGUIEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerControlGUIEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
