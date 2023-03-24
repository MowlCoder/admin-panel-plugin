package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.utils.ItemBuilder;
import me.mowlcoder.adminpanelplugin.utils.SystemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class AdminGUI implements CustomGUI {

    private final String configKey = "admin_panel";
    private Inventory inventory;
    private final String storageActionKey;
    private final static AdminGUI instance = new AdminGUI();
    private final GUIConfig guiConfig;

    private AdminGUI() {
        guiConfig = new GUIConfig(configKey);
        storageActionKey = guiConfig.getStorageActionKey();
        initInventory();
    }

    public static AdminGUI getInstance() {
        return instance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getStorageActionKey() {
        return storageActionKey;
    }

    private void initInventory() {
        inventory = Bukkit.createInventory(
                null,
                guiConfig.getSize(),
                guiConfig.getTitle()
        );

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

    public void loadGUIData() {
        double processLoad = SystemUtil.getProcessCpuLoad();
        long maxMemoryInMb = SystemUtil.getMaxMemoryInMb();
        long usedMemoryInMb = SystemUtil.getUsedMemoryInMb();

        StringBuilder sb = new StringBuilder();
        for (double tps : SystemUtil.getTps())
        {
            sb.append(String.format("%,.2f", tps));
            sb.append(", ");
        }

        ConfigurationSection tpsSection = AdminPanelPlugin.getInstance()
                .getConfig()
                .getConfigurationSection(configKey + ".slots.tps");

        inventory.setItem(
                tpsSection.getInt("position", 0),
                new ItemBuilder(Material.valueOf(tpsSection.getString("material")))
                        .setDisplayName(tpsSection.getString("name"))
                        .setLore(Arrays.asList("1m, 5m, 15m", sb.toString()))
                        .build()
        );

        ConfigurationSection memorySection = AdminPanelPlugin.getInstance()
                .getConfig()
                .getConfigurationSection(configKey + ".slots.memory_usage");

        String memoryUsage = "%used_memory%MB/%max_memory%MB".replace("%used_memory%", Long.toString(usedMemoryInMb)).replace("%max_memory%", Long.toString(maxMemoryInMb));
        inventory.setItem(
                memorySection.getInt("position", 1),
                new ItemBuilder(Material.valueOf(memorySection.getString("material")))
                        .setDisplayName(memorySection.getString("name"))
                        .setLore(Arrays.asList(memoryUsage))
                        .build()
        );

        ConfigurationSection cpuSection = AdminPanelPlugin.getInstance()
                .getConfig()
                .getConfigurationSection(configKey + ".slots.cpu_load");

        String cpuLoad = processLoad + "%";
        inventory.setItem(
                cpuSection.getInt("position", 2),
                new ItemBuilder(Material.valueOf(cpuSection.getString("material")))
                        .setDisplayName(cpuSection.getString("name"))
                        .setLore(Arrays.asList(cpuLoad))
                        .build()
        );

        ConfigurationSection playerOnlineSection = AdminPanelPlugin.getInstance()
                .getConfig()
                .getConfigurationSection(configKey + ".slots.player_online");

        String playerOnline = Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers();
        inventory.setItem(
                playerOnlineSection.getInt("position", 3),
                new ItemBuilder(Material.valueOf(playerOnlineSection.getString("material")))
                        .setDisplayName(playerOnlineSection.getString("name"))
                        .setLore(Arrays.asList(playerOnline, "Нажмите, чтобы открыть меню с игроками"))
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                AdminGUIAction.OPEN_PLAYERS_MENU.name()
                        )
                        .build()
        );

        int allPing = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            allPing += player.getPing();
        }

        String averagePing = allPing == 0 ? "0ms" : (allPing / Bukkit.getOnlinePlayers().size()) + "ms";

        ConfigurationSection pingSection = AdminPanelPlugin.getInstance()
                .getConfig()
                .getConfigurationSection(configKey + ".slots.average_ping");

        inventory.setItem(
                pingSection.getInt("position", 4),
                new ItemBuilder(Material.valueOf(pingSection.getString("material")))
                        .setDisplayName(pingSection.getString("name"))
                        .setLore(Arrays.asList(averagePing))
                        .build()
        );

        NavigationSlotInfo reloadSlotInfo = new NavigationSlotInfo("reload");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".reload_position"),
                new ItemBuilder(reloadSlotInfo.getMaterial())
                        .setDisplayName(reloadSlotInfo.getDisplayName())
                        .setLore(reloadSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                AdminGUIAction.RELOAD.name()
                        )
                        .build()
        );

        NavigationSlotInfo closeSlotInfo = new NavigationSlotInfo("close");

        inventory.setItem(
                AdminPanelPlugin.getInstance().getConfig().getInt(configKey + ".close_position"),
                new ItemBuilder(closeSlotInfo.getMaterial())
                        .setDisplayName(closeSlotInfo.getDisplayName())
                        .setLore(closeSlotInfo.getLore())
                        .setValueInPersistentDataContainer(
                                storageActionKey,
                                PersistentDataType.STRING,
                                AdminGUIAction.CLOSE.name()
                        )
                        .build()
        );
    }
}
