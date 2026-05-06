package dev.trashpanda.fromashes.whetstone;

import java.util.Comparator;
import java.util.List;

import dev.trashpanda.fromashes.whetstone.recipe.ModWhetstoneRecipes;
import dev.trashpanda.fromashes.whetstone.recipe.WhetstoneShapingRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public final class WhetstoneRecipeFinder {
    private WhetstoneRecipeFinder() {
    }

    public static List<WhetstoneRecipeSelection> findMatches(final ServerLevel level, final ItemStack stack) {
        SingleRecipeInput input = new SingleRecipeInput(stack);
        return level.recipeAccess().getRecipes().stream()
                .filter(holder -> holder.value().getType() == ModWhetstoneRecipes.WHETSTONE_SHAPING_TYPE)
                .map(holder -> cast(holder))
                .filter(holder -> holder.value().matches(input, level))
                .map(holder -> new WhetstoneRecipeSelection(holder, holder.value()))
                .sorted(Comparator.comparing(selection -> selection.recipe().displayName().getString()))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<WhetstoneShapingRecipe> cast(final RecipeHolder<?> holder) {
        return (RecipeHolder<WhetstoneShapingRecipe>) holder;
    }
}
