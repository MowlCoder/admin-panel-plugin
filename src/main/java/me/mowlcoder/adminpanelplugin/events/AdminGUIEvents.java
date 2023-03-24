package me.mowlcoder.adminpanelplugin.events;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.gui.*;
import me.mowlcoder.adminpanelplugin.utils.ChatUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AdminGUIEvents implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        if (event.getCurrentItem() == null) { return; }

        Player player = (Player) event.getWhoClicked();
        CustomGUI openedGui = GUIManager.getInstance().getOpenedGUI(player.getUniqueId());

        if (openedGui == null) {
            return;
        }

        if (!(openedGui instanceof AdminGUI)) {
            return;
        }

        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        String adminGuiActionTypeName = itemMeta.getPersistentDataContainer().get(
                NamespacedKey.fromString(AdminGUI.getInstance().getStorageActionKey()),
                PersistentDataType.STRING
        );

        if (adminGuiActionTypeName == null) {
            return;
        }

        AdminGUIAction adminGUIAction = AdminGUIAction.valueOf(adminGuiActionTypeName);

        switch (adminGUIAction) {
            case RELOAD -> {
                AdminGUI.getInstance().loadGUIData();
                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig().getString("messages.success_reload")
                );
            }
            case CLOSE -> player.closeInventory();
            case OPEN_PLAYERS_MENU -> {
                player.closeInventory();
                PlayersGUI playersGUI = new PlayersGUI();
                player.openInventory(playersGUI.getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), playersGUI);
            }
        }
    }
}
