package me.mowlcoder.adminpanelplugin.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIManager {

    private final Map<UUID, CustomGUI> openedGUI = new HashMap<>();

    public void addOpenedGUI(UUID uuid, CustomGUI gui) {
        openedGUI.put(uuid, gui);
    }

    public CustomGUI getOpenedGUI(UUID uuid) {
        return openedGUI.get(uuid);
    }

    public void deleteOpenedGUI(UUID uuid) {
        openedGUI.remove(uuid);
    }

    private static final GUIManager instance = new GUIManager();

    public static GUIManager getInstance() {
        return instance;
    }

    private GUIManager() {}

}
