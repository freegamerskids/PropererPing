package com.lolwm.properer_ping.mixin.client;

import com.lolwm.properer_ping.client.PropererPingClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

    @Inject(method = "getLatency()I", at = @At("HEAD"), cancellable = true, remap = false)
    private void getLatency(CallbackInfoReturnable<Integer> cir) {
        PlayerListEntry entry = (PlayerListEntry)(Object)this;
        assert MinecraftClient.getInstance().player != null;
        Method idMethod = null;
        try {
            idMethod = GameProfile.class.getMethod("id");
        } catch (Exception e) {
            try {
                idMethod = GameProfile.class.getMethod("getId");
            } catch (Exception e1) {
                cir.setReturnValue(-1);
            }
        }
        UUID playerUUID = null;
        try {
            playerUUID = ((UUID)idMethod.invoke(entry.getProfile()));
        } catch (Exception e) {
            cir.setReturnValue(-1);
        }

        if (playerUUID.equals(MinecraftClient.getInstance().player.getUuid())) {
            cir.setReturnValue((int) PropererPingClient.getAverageLatency());
        }
    }
}
