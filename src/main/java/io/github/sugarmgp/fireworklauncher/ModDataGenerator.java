package io.github.sugarmgp.fireworklauncher;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(RecipeGenerator::new);
        pack.addProvider(BlockLootTableProvider::new);
    }

    private static class RecipeGenerator extends FabricRecipeProvider {
        public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void generate(RecipeExporter recipeExporter) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.FIREWORK_LAUNCHER)
                    .pattern("b b").pattern("bib").pattern("bfb")
                    .input('b', Blocks.COBBLESTONE)
                    .input('i', Items.GUNPOWDER)
                    .input('f', Blocks.DISPENSER)
                    .criterion(FabricRecipeProvider.hasItem(Items.GUNPOWDER),
                            FabricRecipeProvider.conditionsFromItem(Items.GUNPOWDER))
                    .criterion(FabricRecipeProvider.hasItem(Blocks.DISPENSER),
                            FabricRecipeProvider.conditionsFromItem(Blocks.DISPENSER))
                    .offerTo(recipeExporter);
        }
    }

    private static class BlockLootTableProvider extends FabricBlockLootTableProvider {
        public BlockLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        public void generate() {
            addDrop(ModBlocks.FIREWORK_LAUNCHER);
        }
    }
}