package dev.trashpanda.fromashes.mixin;

import dev.trashpanda.fromashes.whetstone.WhetstoneDialogActionHandler;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
    @Inject(method = "handleCustomClickAction", at = @At("HEAD"), cancellable = true)
    private void fromAshes$handleCustomClickAction(final ServerboundCustomClickActionPacket packet, final CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl gamePacketListener
                && WhetstoneDialogActionHandler.handle(gamePacketListener.player, packet.id(), packet.payload())) {
            ci.cancel();
        }
    }
}
