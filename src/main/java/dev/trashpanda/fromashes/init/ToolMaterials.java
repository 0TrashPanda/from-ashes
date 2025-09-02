package dev.trashpanda.fromashes.init;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class ToolMaterials {
    public static final ToolMaterial ROCK_TOOL_MATERIAL = new ToolMaterial(
		BlockTags.INCORRECT_FOR_WOODEN_TOOL,
		16,
		1F,
		2F,
		1,
        ItemTags.STONE_TOOL_MATERIALS
);
}
