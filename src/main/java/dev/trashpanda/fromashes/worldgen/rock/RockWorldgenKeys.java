package dev.trashpanda.fromashes.worldgen.rock;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class RockWorldgenKeys {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RIVERBANK_CONFIGURED = configured("rock/riverbank");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROUGH_BIOME_CONFIGURED = configured("rock/rough_biome");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_CONFIGURED = configured("rock/forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOBAL_SLOPE_CONFIGURED = configured("rock/global_slope");

    public static final ResourceKey<PlacedFeature> RIVERBANK_PLACED = placed("rock/riverbank");
    public static final ResourceKey<PlacedFeature> ROUGH_BIOME_PLACED = placed("rock/rough_biome");
    public static final ResourceKey<PlacedFeature> FOREST_PLACED = placed("rock/forest");
    public static final ResourceKey<PlacedFeature> GLOBAL_SLOPE_PLACED = placed("rock/global_slope");

    private RockWorldgenKeys() {
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(final String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, path));
    }

    private static ResourceKey<PlacedFeature> placed(final String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, path));
    }
}
