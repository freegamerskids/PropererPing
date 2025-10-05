# Properer Ping

A Minecraft Fabric mod that tracks and provides access to packet latency measurements between the client and server.

Best paired with a mod like [Ping View](https://modrinth.com/mod/ping-view) or [Better Ping Display](https://modrinth.com/mod/better-ping-display-fabric) (whenever it gets updated).
## Features

- Tracks latency for the last 5 packets sent between client and server
- Measures latency in milliseconds with nanosecond precision
- Thread-safe implementation using concurrent collections

## How It Works

The mod uses mixins to intercept Minecraft's query ping/pong packets to measure round-trip time (RTT):

1. **Client sends QueryPingC2SPacket**: Contains a timestamp and requests server information
2. **Server responds with PingResultS2CPacket**: Echoes back the original timestamp
3. **ClientPlayNetworkHandlerMixin**: Intercepts the pong response
4. **PacketLatencyTracker**: Calculates latency as (response time - request time)

RTT is measured as the time between sending a query ping and receiving the pong response.

## Building

This is a standard Fabric mod project. To build:

```bash
./gradlew build
```

## Requirements

- Minecraft 1.21.8
- Fabric Loader 0.17.2
- Fabric API 0.134.0+1.21.8
- Kotlin 2.2.20

## License

Apache 2.0
