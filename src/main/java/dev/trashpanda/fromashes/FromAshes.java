package dev.trashpanda.fromashes;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.item.ModItems;
import net.fabricmc.api.ModInitializer;
public class FromAshes implements ModInitializer {
	public static final String MOD_ID = "from-ashes";

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModBlocks.initialize();
	}
}