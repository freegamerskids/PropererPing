package com.lolwm.properer_ping.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket

object PropererPingClient : ClientModInitializer {
    //final const val MOD_ID = "properer_ping"
    //@JvmField
    //final val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    val latencyTracker = PacketLatencyTracker()

    override fun onInitializeClient() {
        latencyTracker.clear()

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickCallback())
        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
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
    fun getAverageLatency(): Long {
        val latency = latencyTracker.getAverageLatency()
        return latency ?: 0L
    }

//    @JvmStatic
//    fun onPacketSent(packet: Packet<*>, ci: CallbackInfo) {
//        if (packet is ReadyS2CPacket) {
//            latencyTracker.clear()
//        }
//    }

    @JvmStatic
    fun onPingResultReceived(packet: PingResultS2CPacket) {
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
