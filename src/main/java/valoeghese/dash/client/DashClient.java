package valoeghese.dash.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import valoeghese.dash.DashTracker;
import valoeghese.dash.network.DashNetworking;
import valoeghese.dash.network.ServerboundDashPacket;

import java.util.function.Supplier;

public class DashClient {
	private static final ResourceLocation DASH_ICONS = new ResourceLocation("dtdash", "textures/dash_icons.png");

	public static void resetAttackTimer(Supplier<NetworkEvent.Context> ctx) {
		Minecraft.getInstance().player.resetAttackStrengthTicker();
		ctx.get().setPacketHandled(true);
	}

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
				gui.blit(stack, 8, window.getGuiScaledHeight() - 32 - 8, 0, 0, 32, 32); // render the background
				gui.blit(stack, 8, window.getGuiScaledHeight() - blitHeight - 8, 0, 32 + (32 - blitHeight), 32, blitHeight); // render the foreground
				stack.popPose();
			}
		}
	}

	// This code could be much better beautified by extracting commonalities but it's not gonna change any time soon and it's 1am
	public static boolean tryDash() {
		boolean attempted = false;

		if (DoubleTapHandler.FORWARD_DASH.doubleTapped()) {
			DoubleTapHandler.FORWARD_DASH.reset();
			sendDash(DashNetworking.FORWARD);
			attempted = true;
		}

		if (DoubleTapHandler.BACKWARDS_DASH.doubleTapped()) {
			DoubleTapHandler.BACKWARDS_DASH.reset();
			sendDash(DashNetworking.BACKWARDS);
			attempted = true;
		}

		if (DoubleTapHandler.LEFT_DASH.doubleTapped()) {
			DoubleTapHandler.LEFT_DASH.reset();
			sendDash(DashNetworking.LEFT);
			attempted = true;
		}

		if (DoubleTapHandler.RIGHT_DASH.doubleTapped()) {
			DoubleTapHandler.RIGHT_DASH.reset();
			sendDash(DashNetworking.RIGHT);
			attempted = true;
		}

		return attempted;
	}

	private static void sendDash(byte direction) {
		DashNetworking.DASH_CHANNEL.sendToServer(new ServerboundDashPacket(direction));
	}
}
