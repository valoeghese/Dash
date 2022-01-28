package valoeghese.dash.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import valoeghese.dash.Dash;

public class DashClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(Dash.RESET_TIMER_PACKET, (client, handler, buf, responseSender) -> {
			client.player.resetAttackStrengthTicker();
		});
	}

	// This code could be much better beautified by extracting commonalities but it's not gonna change any time soon and it's 1am
	public static boolean tryDash() {
		boolean attempted = false;

		if (DoubleTapHandler.FORWARD_DASH.doubleTapped()) {
			DoubleTapHandler.FORWARD_DASH.reset();
			sendDash(Dash.FORWARD);
			attempted = true;
		}

		if (DoubleTapHandler.BACKWARDS_DASH.doubleTapped()) {
			DoubleTapHandler.BACKWARDS_DASH.reset();
			sendDash(Dash.BACKWARDS);
			attempted = true;
		}

		if (DoubleTapHandler.LEFT_DASH.doubleTapped()) {
			DoubleTapHandler.LEFT_DASH.reset();
			sendDash(Dash.LEFT);
			attempted = true;
		}

		if (DoubleTapHandler.RIGHT_DASH.doubleTapped()) {
			DoubleTapHandler.RIGHT_DASH.reset();
			sendDash(Dash.RIGHT);
			attempted = true;
		}

		return attempted;
	}

	private static void sendDash(int direction) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		buf.writeByte(direction);
		ClientPlayNetworking.send(Dash.DASH_PACKET, buf);
	}
}
