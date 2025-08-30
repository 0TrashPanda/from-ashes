package dev.trashpanda.fromashes.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class Rock extends HorizontalFacingBlock {
    public static final MapCodec<Rock> CODEC = createCodec(Rock::new);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public Rock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends Rock> getCodec() {
        return CODEC;
    }

    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		if (ctx != null && ctx.getPlayer() != null) {
			return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
		}
		return this.getDefaultState();
	}

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return VoxelShapes.cuboid(.125f, 0f, .1875f, .5625f, .1875f, 0.5625f);
            case SOUTH:
                return VoxelShapes.cuboid(.4375f, 0f, .4375f, .875f, .1875f, .8125f);
            case EAST:
                return VoxelShapes.cuboid(.4375f, 0f, .125f, .8125f, .1875f, .5625f);
            case WEST:
                return VoxelShapes.cuboid(.1875f, 0f, .4375f, .5625f, .1875f, .875f);
            default:
                return VoxelShapes.fullCube();
        }
    }

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		return direction == Direction.DOWN && !state.canPlaceAt(world, pos)
			? Blocks.AIR.getDefaultState()
			: super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		return hasTopRim(world, blockPos);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		// pick up the rock when right-clicked
		if (!world.isClient) {
			if (player.getInventory().insertStack(this.asItem().getDefaultStack())) {
				world.removeBlock(pos, false);
			}
		} else {
			world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (!world.isClient && !player.isCreative()) {
			world.spawnEntity(new net.minecraft.entity.ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), this.asItem().getDefaultStack()));
		}
	}
}