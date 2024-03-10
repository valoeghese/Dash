package valoeghese.dash.adapter;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.function.Consumer;

/**
 * Client-to-server
 */
public record C2SContext(ServerPlayer player, ServerGamePacketListenerImpl packetListener, Consumer<Runnable> workEnqueuer) {
}
