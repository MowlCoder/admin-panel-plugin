package me.mowlcoder.adminpanelplugin.gui;

import me.mowlcoder.adminpanelplugin.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class PlayerGUIInfo {

    private final Player player;

    public PlayerGUIInfo(Player player) {
        this.player = player;
    }

    public ItemStack generatePlayerGUISlot() {
        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD).setDisplayName("&b" + player.getName()).build();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(player.getName());
        skullMeta.setLore(getInfoForLore());
        skullMeta.getPersistentDataContainer().set(
                NamespacedKey.fromString("player_name"),
                PersistentDataType.STRING,
                player.getName()
        );
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

    public List<String> getInfoForLore() {
        String ping = "&ePing:&a " + player.getPing() + "ms";
        String isOp = "&eIs op: " + (player.isOp() ? "&aYes" : "&cNo");
        String coordinates = "&eLocation:&a " + generateCordsString(player.getLocation());
        String playerHealth = "&eHealth:&a " + player.getHealth();
        String playerLevel = "&eLevel:&a " + player.getLevel();
        String playerFood = "&eFood:&a " + player.getFoodLevel();
        String playerLastDeathCoordinates = "&eLast death location:&a " + (player.getLastDeathLocation() == null ? "&cNone" : generateCordsString(player.getLastDeathLocation()));
        EntityDamageEvent damageEvent = player.getLastDamageCause();
        String lastDamageCause = "&eLast damage cause:&a " + (damageEvent == null ? "&cNone" : damageEvent.getCause().name());

        List<String> result = Arrays.asList(ping, isOp, coordinates, playerLevel, playerHealth, playerFood, playerLastDeathCoordinates, lastDamageCause);
        result.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));

        return result;
    }

    private String generateCordsString(Location location) {
        return "x: " +
                String.format("%.2f", location.getX()) +
                "; y: " +
                String.format("%.2f", location.getY()) +
                "; z: " +
                String.format("%.2f", location.getZ()) +
                ";";
    }

}
