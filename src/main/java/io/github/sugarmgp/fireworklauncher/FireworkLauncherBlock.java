package io.github.sugarmgp.fireworklauncher;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class FireworkLauncherBlock extends BlockWithEntity {
    public static final BooleanProperty TRIGGERED;
    public static final MapCodec<FireworkLauncherBlock> CODEC = createCodec(FireworkLauncherBlock::new);

    static {
        TRIGGERED = Properties.TRIGGERED;
    }

    public FireworkLauncherBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(TRIGGERED, false));
    }

    @Override
    public MapCodec<FireworkLauncherBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireworkLauncherBlockEntity(pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean isReceivingPower = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean isTriggered = state.get(TRIGGERED);
        if (isReceivingPower && !isTriggered) {
            world.scheduleBlockTick(pos, this, 4);
            world.setBlockState(pos, state.with(TRIGGERED, true));
        } else if (!isReceivingPower && isTriggered) {
            world.setBlockState(pos, state.with(TRIGGERED, false));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.launchRandomFirework(world, pos);
    }

    private void launchRandomFirework(World world, BlockPos pos) {
        Random r = world.getRandom();

        // 随机颜色
        IntList colors = IntArrayList.of(
                getColorFromRGB(r.nextInt(156) + 100, r.nextInt(156) + 100, r.nextInt(156) + 100),
                getColorFromRGB(r.nextInt(136) + 120, r.nextInt(136) + 120, r.nextInt(136) + 120),
                getColorFromRGB(r.nextInt(116) + 140, r.nextInt(116) + 140, r.nextInt(116) + 140),
                getColorFromRGB(r.nextInt(96) + 160, r.nextInt(96) + 160, r.nextInt(96) + 160)
        );

        IntList fadeColors = IntArrayList.of(
                getColorFromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
                getColorFromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255))
        );

        // 随机效果
        boolean flicker = false;
        boolean trail = false;
        int t = r.nextInt(64);
        if (t % 2 == 0) {
            flicker = true;
        }
        if (t % 3 == 0 || t % 13 == 0) {
            trail = true;
        }

        // 随机形状
        FireworkExplosionComponent.Type type = FireworkExplosionComponent.Type.values()[r.nextInt(FireworkExplosionComponent.Type.values().length)];
        int flightTime = r.nextInt(3) + 2;

        // 创建烟花实体
        FireworkRocketEntity firework = new FireworkRocketEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 1.25,
                pos.getZ() + 0.5,
                createFireworkItem(type, colors, fadeColors, trail, flicker, flightTime)
        );

        world.spawnEntity(firework);
        world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    private int getColorFromRGB(int r, int g, int b) {
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private ItemStack createFireworkItem(FireworkExplosionComponent.Type type, IntList colors, IntList fadeColors, boolean hasTrail, boolean hasTwinkle, int flightTime) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        FireworkExplosionComponent component = new FireworkExplosionComponent(type, colors, fadeColors, hasTrail, hasTwinkle);
        ComponentMap map = ComponentMap.builder()
                .add(DataComponentTypes.FIREWORKS, new FireworksComponent(flightTime, List.of(component)))
                .build();
        stack.applyComponentsFrom(map);
        return stack;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }
}