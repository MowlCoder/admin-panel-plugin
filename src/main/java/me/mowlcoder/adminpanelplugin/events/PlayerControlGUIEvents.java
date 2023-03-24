package me.mowlcoder.adminpanelplugin.events;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.gui.*;
import me.mowlcoder.adminpanelplugin.utils.ChatUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerControlGUIEvents  implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        if (event.getCurrentItem() == null) { return; }

        Player player = (Player) event.getWhoClicked();
        CustomGUI openedGui = GUIManager.getInstance().getOpenedGUI(player.getUniqueId());

        if (openedGui == null) {
            return;
        }

        if (!(openedGui instanceof PlayerControlGUI playerControlGUI)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        ItemMeta itemMeta = itemStack.getItemMeta();
        String guiActionTypeName = itemMeta.getPersistentDataContainer().get(
                NamespacedKey.fromString(openedGui.getStorageActionKey()),
                PersistentDataType.STRING
        );

        if (guiActionTypeName == null) {
            return;
        }

        PlayerControlGUIAction action = PlayerControlGUIAction.valueOf(guiActionTypeName);

        Player targetPlayer = playerControlGUI.getTarget();

        switch (action) {
            case TP_TO_PLAYER -> player.teleport(targetPlayer.getLocation());
            case TP_PLAYER_TO_ADMIN -> targetPlayer.teleport(player.getLocation());
            case SET_FULL_HEALTH -> {
                targetPlayer.setHealth(20);

                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages.success_set_health")
                                .replace("%player_name%", player.getName())
                );

                playerControlGUI.loadGUIData();
            }
            case SET_FULL_FOOD -> {
                targetPlayer.setFoodLevel(20);

                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages.success_set_food")
                                .replace("%player_name%", player.getName())
                );

                playerControlGUI.loadGUIData();
            }
            case KICK_PLAYER -> {
                targetPlayer.kick();

                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages.success_kick")
                                .replace("%player_name%", player.getName())
                );

                player.closeInventory();
                PlayersGUI playersGUI = new PlayersGUI();
                player.openInventory(playersGUI.getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), playersGUI);
            }
            case BAN_PLAYER -> {
                targetPlayer.banPlayer(
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages.default_ban_message")
                );

                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages.success_ban")
                                .replace("%player_name%", player.getName())
                );

                player.closeInventory();
                PlayersGUI playersGUI = new PlayersGUI();
                player.openInventory(playersGUI.getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), playersGUI);
            }
            case TOGGLE_OP -> {
                targetPlayer.setOp(!targetPlayer.isOp());

                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig()
                                .getString("messages." + (targetPlayer.isOp() ? "enable_op" : "disable_op"))
                                .replace("%player_name%", player.getName())
                );

                playerControlGUI.loadGUIData();
            }
            case RELOAD -> {
                playerControlGUI.loadGUIData();
                ChatUtil.sendMessage(
                        player,
                        AdminPanelPlugin.getInstance().getConfig().getString("messages.success_reload")
                );
            }
            case GO_BACK -> {
                player.closeInventory();
                PlayersGUI playersGUI = new PlayersGUI();
                player.openInventory(playersGUI.getInventory());
                GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), playersGUI);
            }
        }
    }
}
