package dev.trashpanda.fromashes;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.item.ModItems;
import dev.trashpanda.fromashes.worldgen.FromAshesBiomeModifications;
import dev.trashpanda.fromashes.worldgen.FromAshesWorldPlacedFeatures;
import dev.trashpanda.fromashes.worldgen.rock.ModFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
public class FromAshes implements ModInitializer {
	public static final String MOD_ID = "from-ashes";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		ModFeatures.initialize();
		FromAshesBiomeModifications.initialize();

		BiomeModifications.addFeature(
			BiomeSelectors.tag(BiomeTags.IS_FOREST),
			GenerationStep.Decoration.VEGETAL_DECORATION,
			FromAshesWorldPlacedFeatures.DIAMOND_TREE_PLACED_KEY
		);
	}
}
