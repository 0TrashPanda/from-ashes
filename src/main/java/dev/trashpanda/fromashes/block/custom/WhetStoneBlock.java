package dev.trashpanda.fromashes.block.custom;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.block.entity.custom.WhetStoneBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import dev.trashpanda.fromashes.whetstone.WhetstoneDialogFactory;
import dev.trashpanda.fromashes.whetstone.WhetstoneRecipeFinder;

public class WhetStoneBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public WhetStoneBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(WhetStoneBlock::new);
    }

    public static boolean canBePlacedAt(final Level level, final BlockPos pos) {
        //check if this bock is air
        if (!level.getBlockState(pos).isAir()) {
            return false;
        }
		// check if the block below is stone
        BlockState state = level.getBlockState(pos.below());
        if (!state.is(Blocks.STONE)) {
            return false;
        }
		// check if any adjacent block is water
        if (getWaterFacing(level, pos) != null) {
            return true;
        }
        return false;
	}

    public static Direction getWaterFacing(Level level, BlockPos pos) {
        Direction facing = getHorizontalWaterFacing(level, pos);
        return facing != null ? facing : getHorizontalWaterFacing(level, pos.below());
    }

    private static Direction getHorizontalWaterFacing(Level level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                return direction;
            }
        }
        return null;
    }

    @Override
    protected BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WhetStoneBlockEntity(pos, state);
    }

    public static BlockState getState(final BlockGetter level, final Direction facing) {
        return ModBlocks.WHET_STONE.defaultBlockState().setValue(FACING, facing);
    }

    	@Override
	protected BlockState updateShape(
		final BlockState state,
		final LevelReader level,
		final ScheduledTickAccess ticks,
		final BlockPos pos,
		final Direction directionToNeighbour,
		final BlockPos neighbourPos,
		final BlockState neighbourState,
		final RandomSource random
	) {
		if (!level.getFluidState(pos.relative(state.getValue(FACING))).is(FluidTags.WATER) && !level.getFluidState(pos.relative(state.getValue(FACING)).above()).is(FluidTags.WATER)) {
            return Blocks.STONE.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

    @Override
    protected InteractionResult useItemOn(
            ItemStack itemStack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            var selections = WhetstoneRecipeFinder.findMatches((ServerLevel) serverPlayer.level(), itemStack);
            if (selections.isEmpty()) {
                return InteractionResult.PASS;
            }
            serverPlayer.openDialog(
                    WhetstoneDialogFactory.createShapeSelectionDialog(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            itemStack,
                            selections
                    )
            );
        }

        return InteractionResult.SUCCESS;
    }
}
