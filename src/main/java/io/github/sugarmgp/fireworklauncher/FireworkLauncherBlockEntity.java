package io.github.sugarmgp.fireworklauncher;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class FireworkLauncherBlockEntity extends BlockEntity {
    public FireworkLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FIREWORK_LAUNCHER_ENTITY, pos, state);
    }
}
