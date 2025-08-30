package dev.trashpanda.fromashes.init;

import dev.trashpanda.fromashes.FromAshes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroups {
    public static final RegistryKey<ItemGroup> FROMASHES_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(FromAshes.MOD_ID, "item_group"));
    public static final ItemGroup FROMASHES_ITEM_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack(BlockContent.ROCK))
		.displayName(Text.translatable("itemGroup.fromashes"))
		.build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FROMASHES_ITEM_GROUP_KEY, FROMASHES_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(FROMASHES_ITEM_GROUP_KEY).register(content -> {
            content.add(BlockContent.ROCK);

            content.add(ItemContent.ROCK_SHARD);
            content.add(ItemContent.ROCK_CHUNK);
        });
    }
}
