package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class NavigationSlotInfo {

    private Material material;
    private String displayName;

    private List<String> lore;

    public NavigationSlotInfo(String key) {
        material = Material.valueOf(
                AdminPanelPlugin.getInstance().getConfig().getString("navigation." + key + ".material")
        );
        displayName = AdminPanelPlugin.getInstance().getConfig().getString("navigation." + key + ".name");
        lore = AdminPanelPlugin.getInstance().getConfig().getStringList("navigation." + key + ".lore");
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }
}
