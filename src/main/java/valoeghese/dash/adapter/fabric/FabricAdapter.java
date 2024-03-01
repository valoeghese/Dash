package valoeghese.dash.adapter.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.C2SContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricAdapter implements Adapter {
	private final Map<Class<?>, Packet<?>> packets = new HashMap<>();

	@SuppressWarnings("unchecked")
	<T> Packet<T> getPacket(Class<T> clazz) {
		return (Packet<T>) this.packets.get(clazz);
	}

	@Override
	public boolean isDedicatedServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public <T> void registerPacket(ResourceLocation id, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder) {
		this.packets.put(clazz, new Packet<>(encoder, decoder));
	}

	@Override
	public <T> void registerServerboundReceiver(ResourceLocation id, BiConsumer<T, C2SContext> handler) {

	}

	@Override
	public void sendClientboundPacket(Player player, Object packet) {

	}
}

record Packet<T>(BiConsumer<?, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, ?> decoder ) {
}
