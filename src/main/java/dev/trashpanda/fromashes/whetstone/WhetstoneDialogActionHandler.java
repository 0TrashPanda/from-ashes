package dev.trashpanda.fromashes.whetstone;

import java.util.Optional;

import dev.trashpanda.fromashes.block.entity.custom.WhetStoneBlockEntity;
import dev.trashpanda.fromashes.whetstone.recipe.ModWhetstoneRecipes;
import dev.trashpanda.fromashes.whetstone.recipe.WhetstoneShapingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.chunk.LevelChunk;

public final class WhetstoneDialogActionHandler {
    public static final Identifier ACTION_ID = Identifier.fromNamespaceAndPath("from-ashes", "whetstone_select");

    private WhetstoneDialogActionHandler() {
    }

    public static boolean handle(final ServerPlayer player, final Identifier id, final Optional<Tag> payload) {
        if (!ACTION_ID.equals(id) || payload.isEmpty() || !(payload.get() instanceof CompoundTag tag)) {
            return false;
        }

        BlockPos pos = new BlockPos(
                tag.getIntOr("x", Integer.MIN_VALUE),
                tag.getIntOr("y", Integer.MIN_VALUE),
                tag.getIntOr("z", Integer.MIN_VALUE)
        );
        String recipeId = tag.getStringOr("recipe", "");
        if (recipeId.isEmpty()) {
            player.sendSystemMessage(Component.literal("recipeId is empty"));
            player.sendSystemMessage(Component.translatable("dialog.from-ashes.whetstone.invalid"));
            return true;
        }

        ServerLevel serverLevel = resolveLevel(player, tag);
        if (serverLevel == null) {
            player.sendSystemMessage(Component.translatable("dialog.from-ashes.whetstone.invalid"));
            return true;
        }
        if (!(serverLevel.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE) instanceof WhetStoneBlockEntity whetStoneBlockEntity)) {
            player.sendSystemMessage(Component.translatable("dialog.from-ashes.whetstone.invalid"));
            return true;
        }

        Identifier recipeIdentifier = Identifier.tryParse(recipeId);
        if (recipeIdentifier == null) {
            return true;
        }

        RecipeHolder<WhetstoneShapingRecipe> selectedRecipe = player.level().recipeAccess().getRecipes().stream()
                .filter(holder -> holder.id().identifier().equals(recipeIdentifier))
                .filter(holder -> holder.value().getType() == ModWhetstoneRecipes.WHETSTONE_SHAPING_TYPE)
                .map(WhetstoneDialogActionHandler::cast)
                .findFirst()
                .orElse(null);
        if (selectedRecipe == null) {
            selectedRecipe = player.level().recipeAccess().getRecipes().stream()
                    .filter(holder -> holder.value().getType() == ModWhetstoneRecipes.WHETSTONE_SHAPING_TYPE)
                    .map(WhetstoneDialogActionHandler::cast)
                    .filter(holder -> holder.id().identifier().toString().equals(recipeId))
                    .findFirst()
                    .orElse(null);
        }
        if (selectedRecipe == null && "from-ashes".equals(recipeIdentifier.getNamespace())) {
            whetStoneBlockEntity.setSelectedRecipeId(recipeIdentifier.toString());
            return true;
        }
        if (selectedRecipe == null) {
            return true;
        }

        whetStoneBlockEntity.setSelectedRecipeId(selectedRecipe.id().identifier().toString());
        player.sendSystemMessage(Component.translatable("dialog.from-ashes.whetstone.selected", selectedRecipe.value().displayName()));
        return true;
    }

    private static ServerLevel resolveLevel(final ServerPlayer player, final CompoundTag tag) {
        String dimensionId = tag.getStringOr("dimension", "");
        if (dimensionId.isEmpty()) {
            return (ServerLevel) player.level();
        }

        Identifier dimensionIdentifier = Identifier.tryParse(dimensionId);
        if (dimensionIdentifier == null) {
            return null;
        }

        ServerLevel currentLevel = (ServerLevel) player.level();
        if (!dimensionIdentifier.equals(currentLevel.dimension().identifier())) {
            return null;
        }
        return currentLevel;
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<WhetstoneShapingRecipe> cast(final RecipeHolder<?> holder) {
        return (RecipeHolder<WhetstoneShapingRecipe>) holder;
    }
}
