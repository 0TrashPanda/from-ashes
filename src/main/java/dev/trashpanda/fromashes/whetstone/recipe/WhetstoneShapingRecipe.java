package dev.trashpanda.fromashes.whetstone.recipe;

import java.util.List;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class WhetstoneShapingRecipe extends SingleItemRecipe {
    public static final MapCodec<WhetstoneShapingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(WhetstoneShapingRecipe::input),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(WhetstoneShapingRecipe::resultTemplate),
            ComponentSerializationCodec.CODEC.fieldOf("display_name").forGetter(WhetstoneShapingRecipe::displayName),
            ComponentSerializationCodec.CODEC.optionalFieldOf("description", Component.empty()).forGetter(WhetstoneShapingRecipe::description)
    ).apply(instance, WhetstoneShapingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WhetstoneShapingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            recipe -> recipe.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            WhetstoneShapingRecipe::input,
            ItemStackTemplate.STREAM_CODEC,
            WhetstoneShapingRecipe::resultTemplate,
            ComponentSerializationCodec.STREAM_CODEC,
            WhetstoneShapingRecipe::displayName,
            ComponentSerializationCodec.STREAM_CODEC,
            WhetstoneShapingRecipe::description,
            WhetstoneShapingRecipe::new
    );
    public static final RecipeSerializer<WhetstoneShapingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final Component displayName;
    private final Component description;

    public WhetstoneShapingRecipe(
            final Recipe.CommonInfo commonInfo,
            final Ingredient ingredient,
            final ItemStackTemplate result,
            final Component displayName,
            final Component description
    ) {
        super(commonInfo, ingredient, result);
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return ModWhetstoneRecipes.WHETSTONE_SHAPING_TYPE;
    }

    @Override
    public String group() {
        return "whetstone_shaping";
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.STONECUTTER;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new StonecutterRecipeDisplay(this.input().display(), new SlotDisplay.ItemStackSlotDisplay(this.resultTemplate()), SlotDisplay.Empty.INSTANCE));
    }

    public ItemStackTemplate resultTemplate() {
        return this.result();
    }

    public Component displayName() {
        return this.displayName;
    }

    public Component description() {
        return this.description;
    }
}
