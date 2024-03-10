package valoeghese.dash.adapter;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

/**
 * Client-to-server
 */
public record C2SContext(ServerPlayer player, Connection connection, Consumer<Runnable> workEnqueuer) {
}
