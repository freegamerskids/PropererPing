package com.lolwm.properer_ping.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientLoginNetworkHandler
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object PropererPingClient : ClientModInitializer {
    final const val MOD_ID = "properer_ping"
    @JvmField
    final val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    public val latencyTracker = PacketLatencyTracker()

    override fun onInitializeClient() {
        latencyTracker.clear()

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickCallback())
        ClientPlayConnectionEvents.DISCONNECT.register { handler, client ->
            latencyTracker.clear()
        }
    }

    fun sendQueryPing(client: MinecraftClient) {
        if (client.networkHandler != null) {
            val startTime = System.currentTimeMillis()
            val packet = QueryPingC2SPacket(startTime)
            client.networkHandler?.sendPacket(packet)
        }
    }

    @JvmStatic
    fun getAverageLatency(): Long? {
        return latencyTracker.getAverageLatency()
    }

//    @JvmStatic
//    fun onPacketSent(packet: Packet<*>, ci: CallbackInfo) {
//        if (packet is ReadyS2CPacket) {
//            latencyTracker.clear()
//        }
//    }

    @JvmStatic
    fun onPingResultReceived(packet: PingResultS2CPacket, ci: CallbackInfo) {
        latencyTracker.recordPacketReceived(packet.startTime)
    }

    private class ClientTickCallback : EndTick {
        private var tickCounter = 0

        override fun onEndTick(client: MinecraftClient) {
            tickCounter++
            if (tickCounter >= 10) { // Every 10 ticks = ~0.5 seconds
                tickCounter = 0
                sendQueryPing(client)
            }
        }
    }
}
