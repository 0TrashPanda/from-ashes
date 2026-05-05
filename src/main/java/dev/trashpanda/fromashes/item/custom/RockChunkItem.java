package dev.trashpanda.fromashes.item.custom;

import dev.trashpanda.fromashes.block.custom.WhetStoneBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class RockChunkItem extends Item {
    public RockChunkItem(final Item.Properties properties) {
		super(properties);
	}

    @Override
	public InteractionResult useOn(final UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
        BlockPos relativePos = pos.relative(context.getClickedFace());
        // only allow if surface is top of the block
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        }
        if (WhetStoneBlock.canBePlacedAt(level, relativePos)) {
            level.playSound(player, relativePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            Direction facing = WhetStoneBlock.getWaterFacing(level, relativePos);
            BlockState state = WhetStoneBlock.getState(level, facing);
            level.setBlock(relativePos.below(), state, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos.below());
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, relativePos, itemStack);
                itemStack.consume(1, player);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }
}
