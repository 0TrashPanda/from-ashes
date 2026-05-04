package dev.trashpanda.fromashes.worldgen;

import dev.trashpanda.fromashes.worldgen.rock.RockWorldgenKeys;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class FromAshesBiomeModifications {
    private FromAshesBiomeModifications() {
    }

    public static void initialize() {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_RIVER),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                RockWorldgenKeys.RIVERBANK_PLACED
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.TAIGA,
                        Biomes.OLD_GROWTH_PINE_TAIGA,
                        Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                        Biomes.WINDSWEPT_HILLS,
                        Biomes.WINDSWEPT_GRAVELLY_HILLS,
                        Biomes.SNOWY_TAIGA,
                        Biomes.SNOWY_SLOPES,
                        Biomes.FROZEN_PEAKS,
                        Biomes.JAGGED_PEAKS,
                        Biomes.GROVE
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                RockWorldgenKeys.ROUGH_BIOME_PLACED
        );

        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                RockWorldgenKeys.FOREST_PLACED
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                RockWorldgenKeys.GLOBAL_SLOPE_PLACED
        );
    }
}
