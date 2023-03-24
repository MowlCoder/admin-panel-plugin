package me.mowlcoder.adminpanelplugin.events;

import me.mowlcoder.adminpanelplugin.gui.AdminGUI;
import me.mowlcoder.adminpanelplugin.gui.CustomGUI;
import me.mowlcoder.adminpanelplugin.gui.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class GUIEvents implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }

        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        CustomGUI openedGui = GUIManager.getInstance().getOpenedGUI(player.getUniqueId());

        if (openedGui == null) {
            return;
        }

        if (inventory != openedGui.getInventory()) {
            if (event.getWhoClicked().getOpenInventory().getTopInventory() == openedGui.getInventory() && event.isShiftClick()) {
                event.setCancelled(true);
            }

            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        CustomGUI openedGui = GUIManager.getInstance().getOpenedGUI(player.getUniqueId());

        if (openedGui == null) { return; }
        if (openedGui.getInventory() != event.getInventory()) { return; }

        event.setCancelled(true);
    }
}
