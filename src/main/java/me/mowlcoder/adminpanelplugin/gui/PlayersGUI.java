package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlayersGUI implements CustomGUI {

    private final String configKey = "players_online";
    private Inventory inventory;
    private final GUIConfig guiConfig;
    private final String storageActionKey;
    private int currentPage = 1;
    private int maxPage = 1;
    private final int playersOnPage = AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".players_on_page", 36);

    public PlayersGUI() {
        guiConfig = new GUIConfig(configKey);
        storageActionKey = guiConfig.getStorageActionKey();
        initInventory();
    }


    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public String getStorageActionKey() {
        return storageActionKey;
    }

    @Override
    public void loadGUIData() {
        int inventoryIndex = 0;
        int currentPlayerIndex = 0;
        maxPage = Math.ceilDiv(Bukkit.getOnlinePlayers().size(), playersOnPage);

        for (int i = 0; i < playersOnPage; i++) {
            inventory.setItem(i, null);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (currentPlayerIndex < ((currentPage - 1) * playersOnPage)) {
                currentPlayerIndex++;
                continue;
            }

            if (currentPlayerIndex >= currentPage * playersOnPage) {
                break;
            }

            PlayerGUIInfo playerGUIInfo = new PlayerGUIInfo(player);
            ItemStack slot = playerGUIInfo.generatePlayerGUISlot();
            ItemMeta slotMeta = slot.getItemMeta();

            slotMeta.getPersistentDataContainer().set(
                    NamespacedKey.fromString(storageActionKey),
                    PersistentDataType.STRING,
                    PlayersGUIAction.OPEN_PLAYER_CONTROL_MENU.name()
            );

            slot.setItemMeta(slotMeta);

            inventory.setItem(inventoryIndex, slot);
            inventoryIndex++;
            currentPlayerIndex++;
        }

        NavigationSlotInfo reloadSlotInfo = new NavigationSlotInfo("reload");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".reload_position"),
                new ItemBuilder(reloadSlotInfo.getMaterial())
                        .setDisplayName(reloadSlotInfo.getDisplayName())
                        .setLore(reloadSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                PlayersGUIAction.RELOAD.name()
                        )
                        .build()
        );

        if (hasPrevPage()) {
            NavigationSlotInfo prevPageSlotInfo = new NavigationSlotInfo("prev_page");

            inventory.setItem(
                    AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".prev_page_position"),
                    new ItemBuilder(prevPageSlotInfo.getMaterial())
                            .setDisplayName(prevPageSlotInfo.getDisplayName())
                            .setLore(prevPageSlotInfo.getLore())
                            .setValueInPersistentDataContainer(
                                    storageActionKey,
                                    PersistentDataType.STRING,
                                    PlayersGUIAction.PREV_PAGE.name()
                            )
                            .build()
            );
        } else {
            inventory.setItem(AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".prev_page_position"), null);
        }

        if (hasNextPage()) {
            NavigationSlotInfo nextPageSlotInfo = new NavigationSlotInfo("next_page");

            inventory.setItem(
                    AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".next_page_position"),
                    new ItemBuilder(nextPageSlotInfo.getMaterial())
                            .setDisplayName(nextPageSlotInfo.getDisplayName())
                            .setLore(nextPageSlotInfo.getLore())
                            .setValueInPersistentDataContainer(
                                    storageActionKey,
                                    PersistentDataType.STRING,
                                    PlayersGUIAction.NEXT_PAGE.name()
                            )
                            .build()
            );
        } else {
            inventory.setItem(AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".next_page_position"), null);
        }

        NavigationSlotInfo goBackSlotInfo = new NavigationSlotInfo("go_back");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".go_back_position"),
                new ItemBuilder(goBackSlotInfo.getMaterial())
                        .setDisplayName(goBackSlotInfo.getDisplayName())
                        .setLore(goBackSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                PlayersGUIAction.GO_BACK.name()
                        )
                        .build()
        );
    }

    public void nextPage() {
        if (currentPage + 1 > maxPage) {
            return;
        }

        currentPage += 1;
        loadGUIData();
    }

    public boolean hasNextPage() {
        return currentPage + 1 <= maxPage;
    }

    public void prevPage() {
        if (currentPage - 1 < 1) {
            return;
        }

        currentPage -= 1;
        loadGUIData();
    }

    public boolean hasPrevPage() {
        return currentPage - 1 >= 1;
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
}
