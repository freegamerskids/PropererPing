package com.lolwm.properer_ping.mixin.client;

import com.lolwm.properer_ping.client.PropererPingClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

    @Inject(method = "getLatency()I", at = @At("HEAD"),  cancellable = true)
    private void getLatency(CallbackInfoReturnable<Integer> cir) {
        PlayerListEntry entry = (PlayerListEntry)(Object)this;
        assert MinecraftClient.getInstance().player != null;
        if (entry.getProfile().getId().equals(MinecraftClient.getInstance().player.getUuid())) {
            cir.setReturnValue((int) PropererPingClient.getAverageLatency());
        }
    }
}
