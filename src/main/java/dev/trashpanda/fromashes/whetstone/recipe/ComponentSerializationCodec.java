package dev.trashpanda.fromashes.whetstone.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

import com.mojang.serialization.Codec;

final class ComponentSerializationCodec {
    static final Codec<Component> CODEC = ComponentSerialization.CODEC;
    static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = ComponentSerialization.STREAM_CODEC;

    private ComponentSerializationCodec() {
    }
}
