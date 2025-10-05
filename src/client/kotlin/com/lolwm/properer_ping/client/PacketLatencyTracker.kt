package com.lolwm.properer_ping.client

import java.util.concurrent.ConcurrentLinkedQueue

class PacketLatencyTracker {
    val latencies = ConcurrentLinkedQueue<Long>()

    companion object {
        const val MAX_LATENCIES = 5
    }

    fun recordPacketReceived(startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val latency = currentTime - startTime
        addLatency(latency)
    }

    private fun addLatency(latency: Long) {
        latencies.add(latency)
        while (latencies.size > MAX_LATENCIES) {
            latencies.poll()
        }
    }

//    fun getLastLatencies(): List<Long> {
//        return latencies.toList().reversed()
//    }

    fun getAverageLatency(): Long? {
        val latencyList = latencies.toList()
        return if (latencyList.isNotEmpty()) {
            latencyList.sum() / latencyList.size
        } else {
            null
        }
    }

//    fun getLatestLatency(): Long? {
//        return latencies.toList().reversed().first()
//    }

    fun clear() {
        latencies.clear()
    }
}
