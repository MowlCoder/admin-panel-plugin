package me.mowlcoder.adminpanelplugin.gui;

import org.bukkit.inventory.Inventory;

public interface CustomGUI {
    String getStorageActionKey();
    Inventory getInventory();
    void loadGUIData();
}
