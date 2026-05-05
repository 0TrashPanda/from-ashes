package dev.trashpanda.fromashes.worldgen;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class FromAshesWorldConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> DIAMOND_TREE_CONFIGURED_KEY =
			ResourceKey.create(
					Registries.CONFIGURED_FEATURE,
					Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, "diamond_tree")
			);

    public static void configure(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        TreeConfiguration diamondTree = new TreeConfiguration.TreeConfigurationBuilder(
				// Trunk / Logs
				BlockStateProvider.simple(Blocks.OAK_FENCE),
				new StraightTrunkPlacer(4, 2, 0),
				// Leaves
				BlockStateProvider.simple(Blocks.OAK_LEAVES),
				new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),

				new TwoLayersFeatureSize(0, 0, 0)
		).build();
		// :::datagen-world:tree-feature-config

		// :::datagen-world:tree-register
		context.register(DIAMOND_TREE_CONFIGURED_KEY, new ConfiguredFeature<>(Feature.TREE, diamondTree));
    }
}
