package valoeghese.dash.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=DashMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class ClientEvents {
	public static void onDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		DashMod.getInstance().client.onDisconnect();
	}
}
