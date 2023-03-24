package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import org.bukkit.ChatColor;

public class GUIConfig {

    private final String title;
    private final int size;
    private final boolean isAutoUpdating;
    private final int autoUpdatePeriod;
    private final String storageActionKey;

    public GUIConfig(String key) {
        title = ChatColor.translateAlternateColorCodes('&', AdminPanelPlugin.getInstance().getConfig().getString(key + ".title"));
        size = AdminPanelPlugin.getInstance().getConfig().getInt(key + ".size", 45);
        isAutoUpdating = AdminPanelPlugin.getInstance().getConfig().getBoolean(key + ".auto_update");
        autoUpdatePeriod = AdminPanelPlugin.getInstance().getConfig().getInt("admin_panel.auto_update_delay", 60);
        storageActionKey = AdminPanelPlugin.getInstance().getConfig().getString(key + ".storage_action_key");
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public boolean isAutoUpdating() {
        return isAutoUpdating;
    }

    public int getAutoUpdatePeriod() {
        return autoUpdatePeriod;
    }

    public String getStorageActionKey() {
        return storageActionKey;
    }
}
