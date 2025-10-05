package com.lolwm.properer_ping

import net.fabricmc.api.ModInitializer

/**
 * Properer Ping - A Minecraft Fabric mod that tracks packet latency between client and server.
 *
 * This mod uses mixins to intercept Minecraft's native keep-alive packets to measure
 * round-trip time (RTT) and provides APIs to access the latency data for the last 5 packets.
 */
class PropererPing : ModInitializer {

    override fun onInitialize() {
        // Server-side initialization (if needed in the future)
    }
}
