package me.mowlcoder.adminpanelplugin.commands;

import me.mowlcoder.adminpanelplugin.AdminPanelPlugin;
import me.mowlcoder.adminpanelplugin.gui.AdminGUI;
import me.mowlcoder.adminpanelplugin.gui.GUIManager;
import me.mowlcoder.adminpanelplugin.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class AdminPanelCMD implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, AdminPanelPlugin.getInstance().getConfig().getString("messages.able_use_only_players"));
            return true;
        }

        player.openInventory(AdminGUI.getInstance().getInventory());
        GUIManager.getInstance().addOpenedGUI(player.getUniqueId(), AdminGUI.getInstance());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
