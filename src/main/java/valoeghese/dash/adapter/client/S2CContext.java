package valoeghese.dash.adapter.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;

import java.util.function.Consumer;

/**
 * Server-to-client packet context. Received on the client.
 */
public record S2CContext(Minecraft client, Connection connection, Consumer<Runnable> workEnqueuer) {
}
