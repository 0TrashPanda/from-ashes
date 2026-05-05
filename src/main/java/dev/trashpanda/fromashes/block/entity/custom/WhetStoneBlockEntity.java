package dev.trashpanda.fromashes.block.entity.custom;

import dev.trashpanda.fromashes.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WhetStoneBlockEntity extends BlockEntity {
    public WhetStoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WHET_STONE_BLOCK_ENTITY, pos, state);
    }
}
