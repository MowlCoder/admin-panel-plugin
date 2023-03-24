package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerControlGUI implements CustomGUI {

    private final String configKey = "player_control_panel";
    private final GUIConfig guiConfig;
    private final String storageActionKey;
    private Inventory inventory;
    private final Player target;

    public PlayerControlGUI(Player player) {
        guiConfig = new GUIConfig(configKey);
        storageActionKey = guiConfig.getStorageActionKey();
        this.target = player;
        initInventory();
    }

    @Override
    public String getStorageActionKey() {
        return storageActionKey;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void loadGUIData() {
        PlayerGUIInfo playerGUIInfo = new PlayerGUIInfo(target);
        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".player_head_position"),
                playerGUIInfo.generatePlayerGUISlot()
        );

        setActionSlot("tp_to_player", 0, PlayerControlGUIAction.TP_TO_PLAYER);
        setActionSlot("tp_player_to_admin", 1, PlayerControlGUIAction.TP_PLAYER_TO_ADMIN);
        setActionSlot("toggle_op", 2, PlayerControlGUIAction.TOGGLE_OP);
        setActionSlot("full_health", 3, PlayerControlGUIAction.SET_FULL_HEALTH);
        setActionSlot("full_food", 4, PlayerControlGUIAction.SET_FULL_FOOD);
        setActionSlot("kick", 5, PlayerControlGUIAction.KICK_PLAYER);
        setActionSlot("ban", 6, PlayerControlGUIAction.BAN_PLAYER);

        NavigationSlotInfo reloadSlotInfo = new NavigationSlotInfo("reload");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".reload_position"),
                new ItemBuilder(reloadSlotInfo.getMaterial())
                        .setDisplayName(reloadSlotInfo.getDisplayName())
                        .setLore(reloadSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                PlayerControlGUIAction.RELOAD.name()
                        )
                        .build()
        );

        NavigationSlotInfo backSlotInfo = new NavigationSlotInfo("go_back");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".go_back_position"),
                new ItemBuilder(backSlotInfo.getMaterial())
                        .setDisplayName(backSlotInfo.getDisplayName())
                        .setLore(backSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                PlayerControlGUIAction.GO_BACK.name()
                        )
                        .build()
        );
    }

    public Player getTarget() {
        return target;
    }

    private void initInventory() {
        inventory = Bukkit.createInventory(null, guiConfig.getSize(), guiConfig.getTitle());

        if (guiConfig.isAutoUpdating()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadGUIData();
                }
            }.runTaskTimer(AdminPanelPlugin.getInstance(), 0L, guiConfig.getAutoUpdatePeriod());
        } else {
            loadGUIData();
        }
    }

    private void setActionSlot(String slotKey, int defaultPosition, PlayerControlGUIAction action) {
        ConfigurationSection tpToPlayerSection = AdminPanelPlugin.getInstance().getConfig().getConfigurationSection(configKey + ".slots." + slotKey);
        inventory.setItem(
                tpToPlayerSection.getInt("position", defaultPosition),
                generateActionSlotFromConfing(tpToPlayerSection, action)
        );
    }

    private ItemStack generateActionSlotFromConfing(ConfigurationSection section, PlayerControlGUIAction action) {
        return new ItemBuilder(Material.valueOf(section.getString("material")))
                .setDisplayName(section.getString("name"))
                .setValueInPersistentDataContainer(
                        storageActionKey,
                        PersistentDataType.STRING,
                        action.name()
                )
                .build();
    }
}
