package dev.trashpanda.fromashes.block;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.serialization.MapCodec;

import dev.trashpanda.fromashes.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;

public class Rock extends HorizontalDirectionalBlock {
    public static final MapCodec<Rock> CODEC = simpleCodec(Rock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private final RandomSource random = RandomSource.create();
	public static final Logger LOGGER = LoggerFactory.getLogger(Rock.class);




    public Rock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

	@Override
	public MapCodec<Rock> codec() {
		return CODEC;
	}

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return Block.box(2, 0, 3, 9, 3, 9);
            case SOUTH:
                return Block.box(7, 0, 7, 14, 3, 13);
            case EAST:
                return Block.box(7, 0, 2, 13, 3, 9);
            case WEST:
                return Block.box(3, 0, 7, 9, 3, 14);
            default:
                return Shapes.empty();
        }
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
		return !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
		BlockPos belowPos = pos.below();
		return this.canSurviveOn(level, belowPos, level.getBlockState(belowPos));
	}

	protected boolean canSurviveOn(final LevelReader level, final BlockPos neightborPos, final BlockState neighborState) {
		return neighborState.isFaceSturdy(level, neightborPos, Direction.UP, SupportType.RIGID);
	}

	// @Override
	// protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
	// 	builder.add(FACING);
	// }

	// @Override
	// protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
	// 	// pick up the rock when right-clicked
	// 	if (!world.isClient) {
	// 		if (player.getInventory().insertStack(this.asItem().getDefaultStack())) {
	// 			world.removeBlock(pos, false);
	// 		}
	// 	} else {
	// 		world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
	// 	}
	// 	return ActionResult.SUCCESS;
	// }

    @Override
    protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            if (player.getInventory().add(this.asItem().getDefaultInstance())) {
                level.removeBlock(pos, false);

                level.playSound(
                    null, // null = broadcast to nearby players
                    pos,
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS,
                    0.2F,
                    (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F
                );
            }
        }

        return InteractionResult.SUCCESS;
    }

	@Override
	public void playerDestroy(
		final Level level, final Player player, final BlockPos pos, final BlockState state, @Nullable final BlockEntity blockEntity, final ItemStack destroyedWith
	) {
		LOGGER.info("playerDestroy called");
		if (!player.getMainHandItem().is(ModBlocks.ROCK.asItem())) {
			LOGGER.info("Player is not holding a rock");
			super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
			return;
		}

		if (this.random.nextFloat() < 0.8f) {
			LOGGER.info("Consuming one rock from player's hand");
			player.getMainHandItem().shrink(1);
		}

		if (this.random.nextFloat() < 0.5f) {
			LOGGER.info("Stopping rock from breaking");
			level.setBlock(pos, state, Block.UPDATE_ALL); // TODO fix this hack to reset the block's state and prevent it from breaking
		}

		if (this.random.nextFloat() < 0.2f) {
			LOGGER.info("Dropping rock chunk");
            popResource(level, pos, new ItemStack(ModItems.ROCK_CHUNK, 1));
		}

		if (this.random.nextFloat() < 0.6f) {
			int itemCount = 1 + this.random.nextInt(2);
			LOGGER.info("Dropping {} rock shards", itemCount);
            popResource(level, pos, new ItemStack(ModItems.ROCK_SHARD, itemCount));
		}
	}
}
