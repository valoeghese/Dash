package valoeghese.dash.adapter.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;

/**
 * Server-to-client packet context. Received on the client.
 */
public record S2CContext(Minecraft client, Connection connection) {
}
