package dev.trashpanda.fromashes.whetstone.recipe;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class ModWhetstoneRecipes {
    public static final RecipeType<WhetstoneShapingRecipe> WHETSTONE_SHAPING_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, "whetstone_shaping"),
            new RecipeType<>() {
                @Override
                public String toString() {
                    return FromAshes.MOD_ID + ":whetstone_shaping";
                }
            }
    );

    public static final RecipeSerializer<WhetstoneShapingRecipe> WHETSTONE_SHAPING_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, "whetstone_shaping"),
            WhetstoneShapingRecipe.SERIALIZER
    );

    private ModWhetstoneRecipes() {
    }

    public static void initialize() {
    }
}
