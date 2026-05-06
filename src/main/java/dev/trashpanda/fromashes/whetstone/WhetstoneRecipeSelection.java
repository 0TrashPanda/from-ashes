package dev.trashpanda.fromashes.whetstone;

import dev.trashpanda.fromashes.whetstone.recipe.WhetstoneShapingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public record WhetstoneRecipeSelection(
        RecipeHolder<WhetstoneShapingRecipe> holder,
        WhetstoneShapingRecipe recipe
) {
}
