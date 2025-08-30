package dev.trashpanda.fromashes.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import java.util.function.Function;

import dev.trashpanda.fromashes.FromAshes;
import dev.trashpanda.fromashes.item.RockBlockItem;

public class ItemContent {
	public static final Item ROCK = register("rock", (settings) -> new RockBlockItem(BlockContent.ROCK, settings, 2F, -3F), new Item.Settings());
	public static final Item ROCK_SHARD = register("rock_shard", (settings) -> new Item(settings), new Item.Settings());
	public static final Item ROCK_CHUNK = register("rock_chunk", (settings) -> new Item(settings), new Item.Settings().sword(ToolMaterials.ROCK_TOOL_MATERIAL, 0, -3.0F));

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FromAshes.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}


    public static void initialize() {
    }
}
