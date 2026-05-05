package dev.trashpanda.fromashes.block.entity;

import dev.trashpanda.fromashes.FromAshes;
import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.block.entity.custom.WhetStoneBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<WhetStoneBlockEntity> WHET_STONE_BLOCK_ENTITY =
			register("whet_stone", WhetStoneBlockEntity::new, ModBlocks.WHET_STONE);

	private static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		Identifier id = Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, name);
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {
	}
}
