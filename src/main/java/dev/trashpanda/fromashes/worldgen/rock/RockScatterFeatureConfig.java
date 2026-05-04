package dev.trashpanda.fromashes.worldgen.rock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record RockScatterFeatureConfig(
        RockPlacementProfile profile,
        int minRocks,
        int maxRocks,
        int minSpread,
        int maxSpread,
        int maxWaterDistance,
        int minWaterRise,
        int maxWaterRise,
        int minSlope,
        float extraPlacementChance
) implements FeatureConfiguration {
    public static final Codec<RockScatterFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RockPlacementProfile.CODEC.fieldOf("profile").forGetter(RockScatterFeatureConfig::profile),
            Codec.intRange(1, 32).fieldOf("min_rocks").forGetter(RockScatterFeatureConfig::minRocks),
            Codec.intRange(1, 32).fieldOf("max_rocks").forGetter(RockScatterFeatureConfig::maxRocks),
            Codec.intRange(1, 16).fieldOf("min_spread").forGetter(RockScatterFeatureConfig::minSpread),
            Codec.intRange(1, 16).fieldOf("max_spread").forGetter(RockScatterFeatureConfig::maxSpread),
            Codec.intRange(0, 16).fieldOf("max_water_distance").forGetter(RockScatterFeatureConfig::maxWaterDistance),
            Codec.intRange(0, 8).fieldOf("min_water_rise").forGetter(RockScatterFeatureConfig::minWaterRise),
            Codec.intRange(0, 8).fieldOf("max_water_rise").forGetter(RockScatterFeatureConfig::maxWaterRise),
            Codec.intRange(0, 8).fieldOf("min_slope").forGetter(RockScatterFeatureConfig::minSlope),
            Codec.floatRange(0.0F, 1.0F).fieldOf("extra_placement_chance").forGetter(RockScatterFeatureConfig::extraPlacementChance)
    ).apply(instance, RockScatterFeatureConfig::new));
}
