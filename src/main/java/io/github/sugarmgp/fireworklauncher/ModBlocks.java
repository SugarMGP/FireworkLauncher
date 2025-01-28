package io.github.sugarmgp.fireworklauncher;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block FIREWORK_LAUNCHER = new FireworkLauncherBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.4f)
    );
    public static final BlockItem FIREWORK_LAUNCHER_ITEM = new BlockItem(FIREWORK_LAUNCHER, new BlockItem.Settings());

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(FireworkLauncher.MOD_ID, "firework_launcher"), FIREWORK_LAUNCHER);
        Registry.register(Registries.ITEM, Identifier.of(FireworkLauncher.MOD_ID, "firework_launcher"), FIREWORK_LAUNCHER_ITEM);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> entries.add(FIREWORK_LAUNCHER_ITEM));
    }
}