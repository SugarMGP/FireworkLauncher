package io.github.sugarmgp.fireworklauncher;

import net.fabricmc.api.ModInitializer;

public class FireworkLauncher implements ModInitializer {
    public static final String MOD_ID = "fireworklauncher";

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntities.register();
    }
}
