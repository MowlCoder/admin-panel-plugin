package me.mowlcoder.adminpanelplugin.events;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.gui.*;
import me.mowlcoder.adminpanelplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayersGUIEvents implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        if (event.getCurrentItem() == null) { return; }

        Player player = (Player) event.getWhoClicked();
        CustomGUI openedGui = GUIManager.getInstance().getOpenedGUI(player.getUniqueId());

        if (openedGui == null) {
            return;
        }

        if (!(openedGui instanceof PlayersGUI playersGUI)) {
            return;
        }

        event.setCancelled(true);

        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        String playersGuiActionTypeName = itemMeta.getPersistentDataContainer().get(
                NamespacedKey.fromString(openedGui.getStorageActionKey()),
                PersistentDataType.STRING
        );

        if (playersGuiActionTypeName == null) {
            return;
        }

        PlayersGUIAction action = PlayersGUIAction.valueOf(playersGuiActionTypeName);

        switch (action) {
            case OPEN_PLAYER_CONTROL_MENU -> {
                player.closeInventory();

                Player target = Bukkit.getPlayer(
                        itemMeta.getPersistentDataContainer().get(
                                NamespacedKey.fromString("player_name"),
                                PersistentDataType.STRING
                        )
                );

                if (target == null) {
                    return;
                }

                PlayerControlGUI gui = new PlayerControlGUI(target);
                player.openInventory(gui.getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), gui);
            }
            case NEXT_PAGE -> {
                playersGUI.nextPage();
                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig().getString("messages.success_change_page")
                );
            }
            case PREV_PAGE -> {
                playersGUI.prevPage();
                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig().getString("messages.success_change_page")
                );
            }
            case RELOAD -> {
                openedGui.loadGUIData();
                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig().getString("messages.success_reload")
                );
            }
            case GO_BACK -> {
                player.closeInventory();
                player.openInventory(AdminGUI.getInstance().getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), AdminGUI.getInstance());
            }
        }
    }
}
