package io.github.sugarmgp.fireworklauncher;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static void register() {
        Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(FireworkLauncher.MOD_ID, "firework_launcher_entity"),
                FIREWORK_LAUNCHER_ENTITY
        );
    }

    public static final BlockEntityType<FireworkLauncherBlockEntity> FIREWORK_LAUNCHER_ENTITY = BlockEntityType.Builder.create(FireworkLauncherBlockEntity::new, ModBlocks.FIREWORK_LAUNCHER).build(null);
}
