package dev.trashpanda.fromashes.worldgen;

import java.util.List;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class FromAshesWorldPlacedFeatures {
    public static final ResourceKey<PlacedFeature> DIAMOND_TREE_PLACED_KEY =
			ResourceKey.create(
				Registries.PLACED_FEATURE,
							Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, "diamond_tree_placed")
			);

    public static void configure(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        List<PlacementModifier> diamondTreeModifiers = List.of(
				RarityFilter.onAverageOnceEvery(1), // spawns once every 10 chunks on average
				BiomeFilter.biome(),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE

		);
        context.register(
				DIAMOND_TREE_PLACED_KEY,
				new PlacedFeature(
					configuredFeatures.getOrThrow(FromAshesWorldConfiguredFeatures.DIAMOND_TREE_CONFIGURED_KEY),
					diamondTreeModifiers
				)
		);
    }
}
