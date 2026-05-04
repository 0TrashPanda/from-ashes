package dev.trashpanda.fromashes.worldgen.rock;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum RockPlacementProfile implements StringRepresentable {
    RIVERBANK("riverbank"),
    ROUGH("rough"),
    FOREST("forest"),
    GLOBAL("global");

    public static final Codec<RockPlacementProfile> CODEC = StringRepresentable.fromEnum(RockPlacementProfile::values);

    private final String name;

    RockPlacementProfile(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
