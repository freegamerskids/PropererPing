package com.lolwm.properer_ping.mixin.client;

import com.lolwm.properer_ping.client.PropererPingClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onPingResult(Lnet/minecraft/network/packet/s2c/query/PingResultS2CPacket;)V", at = @At("HEAD"), remap = false)
    private void onPingResultReceived(PingResultS2CPacket packet, CallbackInfo ci) {
        PropererPingClient.onPingResultReceived(packet);
    }
}
