package dev.trashpanda.fromashes;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.block.entity.ModBlockEntities;
import dev.trashpanda.fromashes.item.ModItems;
import dev.trashpanda.fromashes.worldgen.FromAshesBiomeModifications;
import dev.trashpanda.fromashes.worldgen.FromAshesWorldPlacedFeatures;
import dev.trashpanda.fromashes.whetstone.recipe.ModWhetstoneRecipes;
import dev.trashpanda.fromashes.worldgen.rock.ModFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
public class FromAshes implements ModInitializer {
	public static final String MOD_ID = "from-ashes";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModItems.initialize();
        ModWhetstoneRecipes.initialize();
		ModFeatures.initialize();
		FromAshesBiomeModifications.initialize();

		BiomeModifications.addFeature(
			BiomeSelectors.tag(BiomeTags.IS_FOREST),
			GenerationStep.Decoration.VEGETAL_DECORATION,
			FromAshesWorldPlacedFeatures.DIAMOND_TREE_PLACED_KEY
		);

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (player.isCreative()) return InteractionResult.PASS; // allow creative players to break blocks
			if (player.isSpectator()) return InteractionResult.PASS; // allow spectator players to break blocks
			var state = world.getBlockState(pos);

			if (!canPlayerBreak(player, state)) {
				// Optional feedback
				player.swing(hand); // or skip if you want zero feedback

				return InteractionResult.FAIL; // stops everything
			}

			return InteractionResult.PASS;
		});
	}

	private static boolean canPlayerBreak(Player player, BlockState state) {
		// only allow breaking if player has the correct tool for the block
		if (state.canBeReplaced()) {
			return true; // allow breaking of replaceable blocks (like tall grass)
		}
		// intended tool hand and insta minable

		var tool = player.getMainHandItem();
		return tool.isCorrectToolForDrops(state);
	}
}
