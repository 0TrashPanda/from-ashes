package dev.trashpanda.fromashes;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.item.ModItems;
import dev.trashpanda.fromashes.worldgen.FromAshesBiomeModifications;
import dev.trashpanda.fromashes.worldgen.rock.ModFeatures;
import net.fabricmc.api.ModInitializer;
public class FromAshes implements ModInitializer {
	public static final String MOD_ID = "from-ashes";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		ModFeatures.initialize();
		FromAshesBiomeModifications.initialize();
	}
}
