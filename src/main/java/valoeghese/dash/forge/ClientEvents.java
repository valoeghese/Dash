package valoeghese.dash.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import valoeghese.dash.adapter.client.S2CContext;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid=DashMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void onDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		DashMod.getInstance().client.onDisconnect();
	}

	static <T> void receivePacket(BiConsumer<T, S2CContext> handler, T packet, NetworkEvent.Context context) {
		S2CContext s2cContext = new S2CContext(
				Minecraft.getInstance(),
				context.getNetworkManager(),
				context::enqueueWork);

		handler.accept(packet, s2cContext);
	}
}
