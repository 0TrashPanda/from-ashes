package dev.trashpanda.fromashes.block;

import java.util.function.Function;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block ROCK = register(
            "rock",
            Rock::new,
            BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(4.0F),
            false
    );
    public static final Item ROCK_ITEM = registerRockItem("rock", ROCK);

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
		// Create a registry key for the block
		ResourceKey<Block> blockKey = keyOfBlock(name);
		// Create the block instance
		Block block = blockFactory.apply(settings.setId(blockKey));

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
		if (shouldRegisterItem) {
			registerBlockItem(name, block, new Item.Properties());
		}

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	@SuppressWarnings("deprecation")
	private static Item registerRockItem(final String name, final Block block) {
		Item.Properties properties = new Item.Properties()
				.component(
						DataComponents.TOOL,
						new Tool(
								java.util.List.of(Tool.Rule.overrideSpeed(HolderSet.direct(block.builtInRegistryHolder()), 6.0F)),
								1.0F,
								0,
								true
						)
				);
		return registerBlockItem(name, block, properties);
	}

	private static Item registerBlockItem(final String name, final Block block, final Item.Properties properties) {
		ResourceKey<Item> itemKey = keyOfItem(name);
		BlockItem blockItem = new BlockItem(block, properties.setId(itemKey).useBlockDescriptionPrefix());
		return Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
	}

	private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, name));
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, name));
	}

    public static void initialize() {}
}
