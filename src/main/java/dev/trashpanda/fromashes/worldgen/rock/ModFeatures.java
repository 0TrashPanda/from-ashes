package dev.trashpanda.fromashes.worldgen.rock;

import dev.trashpanda.fromashes.FromAshes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class ModFeatures {
    public static final Feature<RockScatterFeatureConfig> ROCK_SCATTER = Registry.register(
            BuiltInRegistries.FEATURE,
            Identifier.fromNamespaceAndPath(FromAshes.MOD_ID, "rock_scatter"),
            new RockScatterFeature()
    );

    private ModFeatures() {
    }

    public static void initialize() {
    }
}
