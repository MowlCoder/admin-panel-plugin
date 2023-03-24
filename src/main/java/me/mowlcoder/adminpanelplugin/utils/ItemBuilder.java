package me.mowlcoder.adminpanelplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(
                ChatColor.translateAlternateColorCodes('&', name)
        );
        return this;
    }

    public ItemBuilder setValueInPersistentDataContainer(String key, PersistentDataType type, Object value) {
        itemMeta.getPersistentDataContainer().set(
                NamespacedKey.fromString(key),
                type,
                value
        );

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));

        itemMeta.setLore(lore);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
