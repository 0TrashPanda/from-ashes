package dev.trashpanda.fromashes.init.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;


public class FromAshesWorldgen {

	public static final Identifier ROCK_PILE_FEATURE_ID = Identifier.of("from-ashes", "rock_pile");
	public static final Identifier ROCK_PILE_FOREST_FEATURE_ID = Identifier.of("from-ashes", "rock_pile_forest");
	public static final Identifier ROCK_PILE_PLAINS_FEATURE_ID = Identifier.of("from-ashes", "rock_pile_plains");

    public static void initialize() {
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_RIVER)
                .or(BiomeSelectors.includeByKey(
                    BiomeKeys.STONY_SHORE,
                    BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                    BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                    BiomeKeys.STONY_PEAKS,
                    BiomeKeys.JAGGED_PEAKS
                )),
            GenerationStep.Feature.VEGETAL_DECORATION,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, ROCK_PILE_FEATURE_ID));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_FOREST),
            GenerationStep.Feature.VEGETAL_DECORATION,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, ROCK_PILE_FOREST_FEATURE_ID));
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.PLAINS),
            GenerationStep.Feature.VEGETAL_DECORATION,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, ROCK_PILE_PLAINS_FEATURE_ID));
    }
}