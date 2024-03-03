package valoeghese.dash.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import valoeghese.dash.Dash;
import valoeghese.dash.DashTracker;
import valoeghese.dash.adapter.client.ClientAdapter;
import valoeghese.dash.config.SynchronisedConfig;
import valoeghese.dash.network.ClientboundResetTimerPacket;
import valoeghese.dash.network.ClientboundSyncConfigPacket;
import valoeghese.dash.network.ServerboundDashPacket;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

public class DashClient {
	// Events
	public void setup() {
		Dash.LOGGER.info("Initialising Double-Tap Dash Client");

		ClientAdapter.INSTANCE.registerClientboundReceiver(ClientboundResetTimerPacket.PACKET, (packet, context) -> {
			context.client().player.resetAttackStrengthTicker();
		});

		ClientAdapter.INSTANCE.registerClientboundReceiver(ClientboundSyncConfigPacket.PACKET, (packet, context) -> {
			try {
				SynchronisedConfig config = new SynchronisedConfig();
				config.read(packet.properties(), false); // client-side options cannot be controlled by server.

				Dash.activeConfig = config;
				Dash.LOGGER.info("Successfully synchronised config.");
			} catch (Exception e) {
				context.connection().disconnect(new TranslatableComponent("dtdash.err.sync_parse", e.toString()));
			}
		});
	}

	public void onRegisterKeyMappings(UnaryOperator<KeyMapping> registry) {
		// register dash keys
		dashKey = registry.apply(new KeyMapping(
				"key.dtdash.dash",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(), // not bound by default
				"key.categories.dtdash"
		));

		// register single tap dash keys

		singleDirectionKeys[0] = registry.apply(new KeyMapping(
				"key.dtdash.dash_forward",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(), // not bound by default
				"key.categories.dtdash"
		));

		singleDirectionKeys[1] = registry.apply(new KeyMapping(
				"key.dtdash.dash_backwards",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(), // not bound by default
				"key.categories.dtdash"
		));

		singleDirectionKeys[2] = registry.apply(new KeyMapping(
				"key.dtdash.dash_left",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(), // not bound by default
				"key.categories.dtdash"
		));

		singleDirectionKeys[3] = registry.apply(new KeyMapping(
				"key.dtdash.dash_right",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(), // not bound by default
				"key.categories.dtdash"
		));
	}

	public void onDisconnect() {
		// Synchronise Settings on Join
		// Restore Settings on Leave

		// in case was using server config, ensure active config is reset to the client's config.
		if (Dash.activeConfig != Dash.localConfig) {
			Dash.LOGGER.info("Disconnected from server: Switching to Client's local Config");
			Dash.activeConfig = Dash.localConfig;
		}
	}

	// General Utilities

	private static final ResourceLocation DASH_ICONS = new ResourceLocation("dtdash", "textures/dash_icons.png");
	private static KeyMapping dashKey;
	// Forward, Backwards, Left, Right (mirrors Dash.DashDirection)
	private static final KeyMapping[] singleDirectionKeys = new KeyMapping[4];

	/**
	 * Options hack.
	 * Temporarily stores the options during onOptionsLoad.
	 */
	@Nullable
	public static Options options;

	public static void renderBar(PoseStack stack, Gui gui) {
		Player player = Minecraft.getInstance().player;

		if (player != null) {
			float progress = Mth.clamp(((DashTracker) player).getDashCooldown(), 0, 1);

			if (progress < 1) {
				int blitHeight = (int) (progress * 32);
				stack.pushPose();
				RenderSystem.disableBlend();
				// set texture
				AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(DASH_ICONS);
				RenderSystem.setShaderTexture(0, texture.getId());
				// render
				Window window = Minecraft.getInstance().getWindow();
				int x = (int) Dash.localConfig.iconPosition.get()
						.x(window.getGuiScaledWidth(), window.getGuiScaledHeight());
				int y = (int) Dash.localConfig.iconPosition.get()
						.y(window.getGuiScaledWidth(), window.getGuiScaledHeight());
//				System.out.println(x + " " + y);

				gui.blit(stack, x, y - 8, 0, 0, 32, 32); // render the background
				gui.blit(stack, x, y + 32 - blitHeight - 8, 0, 32 + (32 - blitHeight), 32, blitHeight); // render the foreground
				stack.popPose();
			}
		}
	}

	public static boolean consumeDash() {
		return dashKey.consumeClick();
	}

	// This code could be much better beautified by extracting commonalities but it's not gonna change any time soon and it's 1am
	public static boolean tryDash(boolean dashKeyPressed) {
		// always consume click, not conditionally
		boolean fwdDash = singleDirectionKeys[0].consumeClick();
		boolean backDash = singleDirectionKeys[1].consumeClick();
		boolean leftDash = singleDirectionKeys[2].consumeClick();
		boolean rightDash = singleDirectionKeys[3].consumeClick();

		Set<Dash.DashDirection> attempted = new HashSet<>();

		if (Dash.canDash(Minecraft.getInstance().player)) {
			// determine dash direction from input keys

			if (DashInputHandler.FORWARD_DASH.shouldDash(dashKeyPressed) || fwdDash) {
				DashInputHandler.FORWARD_DASH.reset();
				attempted.add(Dash.DashDirection.FORWARD);
			}

			if (DashInputHandler.BACKWARDS_DASH.shouldDash(dashKeyPressed) || backDash) {
				DashInputHandler.BACKWARDS_DASH.reset();
				attempted.add(Dash.DashDirection.BACKWARD);
			}

			if (DashInputHandler.LEFT_DASH.shouldDash(dashKeyPressed) || leftDash) {
				DashInputHandler.LEFT_DASH.reset();
				attempted.add(Dash.DashDirection.LEFT);
			}

			if (DashInputHandler.RIGHT_DASH.shouldDash(dashKeyPressed) || rightDash) {
				DashInputHandler.RIGHT_DASH.reset();
				attempted.add(Dash.DashDirection.RIGHT);
			}

			// remove mirrors (cancel out)
			if (attempted.contains(Dash.DashDirection.FORWARD) && attempted.contains(Dash.DashDirection.BACKWARD)) {
				attempted.remove(Dash.DashDirection.FORWARD);
				attempted.remove(Dash.DashDirection.BACKWARD);
			}

			if (attempted.contains(Dash.DashDirection.LEFT) && attempted.contains(Dash.DashDirection.RIGHT)) {
				attempted.remove(Dash.DashDirection.LEFT);
				attempted.remove(Dash.DashDirection.RIGHT);
			}
		}

		// now resolve the actual dash direction. either one or two directions in attempted at this point.
		// And if two, will be one F-B and one L-R
		Dash.DashDirection finalDirection = null;

		if (attempted.size() == 2) {
			if (Dash.activeConfig.diagonalDash.get()) {
				// compute the diagonal dash
				if (attempted.contains(Dash.DashDirection.FORWARD)) {
					if (attempted.contains(Dash.DashDirection.LEFT)) {
						finalDirection = Dash.DashDirection.FORWARD_LEFT;
					} else {
						finalDirection = Dash.DashDirection.FORWARD_RIGHT;
					}
				} else { // must be backwards
					if (attempted.contains(Dash.DashDirection.LEFT)) {
						finalDirection = Dash.DashDirection.BACKWARD_LEFT;
					} else {
						finalDirection = Dash.DashDirection.BACKWARD_RIGHT;
					}
				}
			} else {
				// pick the forward/backward direction
				finalDirection = attempted.contains(Dash.DashDirection.FORWARD) ? Dash.DashDirection.FORWARD : Dash.DashDirection.BACKWARD;
			}
		} else if (attempted.size() == 1) {
			finalDirection = attempted.iterator().next();
		}

		// try move in that direction
		if (finalDirection != null) {
			sendDash(finalDirection);
			return true;
		}
		else {
			return false;
		}
	}

	private static void sendDash(Dash.DashDirection direction) {
		ClientAdapter.INSTANCE.sendToServer(new ServerboundDashPacket(direction));
	}
}
