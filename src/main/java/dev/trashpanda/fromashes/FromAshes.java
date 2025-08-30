package dev.trashpanda.fromashes;

import net.fabricmc.api.ModInitializer;
import dev.trashpanda.fromashes.init.BlockContent;
import dev.trashpanda.fromashes.init.ItemContent;
import dev.trashpanda.fromashes.init.ItemGroups;
import dev.trashpanda.fromashes.init.world.FromAshesWorldgen;

public class FromAshes implements ModInitializer {
	public static final String MOD_ID = "from-ashes";

	@Override
	public void onInitialize() {
		ItemContent.initialize();
		BlockContent.initialize();
		FromAshesWorldgen.initialize();
		ItemGroups.initialize();
	}
}