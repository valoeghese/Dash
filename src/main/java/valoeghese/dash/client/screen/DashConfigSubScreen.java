package valoeghese.dash.client.screen;

import benzenestudios.sulphate.SulphateScreen;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import valoeghese.dash.config.*;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static net.minecraft.network.chat.CommonComponents.OPTION_OFF;
import static net.minecraft.network.chat.CommonComponents.OPTION_ON;

public class DashConfigSubScreen extends SulphateScreen {
	protected DashConfigSubScreen(Component title, DashConfigScreen parent, ImmutableList<Option<?>> options, boolean unlocked) {
		super(title, parent);
		this.options = options;
		this.unlocked = unlocked;
	}

	private final List<Option<?>> options;
	private final boolean unlocked;

	private final Button.OnTooltip disabledTooltip = new Button.OnTooltip() {
		@Override
		public void onTooltip(Button button, PoseStack poseStack, int i, int j) {
			Screen screen = DashConfigSubScreen.this;
			Component text = new TranslatableComponent("screen.dtdash.config.disabled");

			screen.renderTooltip(poseStack, Minecraft.getInstance().font.split(
					text,
					Math.max(screen.width / 2 - 43, 170)
			), i, j + 18);
		}

		@Override
		public void narrateTooltip(Consumer<Component> consumer) {
			consumer.accept(new TranslatableComponent("screen.dtdash.config.disabled"));
		}
	};

	@Override
	protected void addWidgets() {
		for (Option<?> option : this.options) {
			// would put this in a method, but GUI is client only.
			if (option instanceof BooleanOption opt) {
				Button bn = this.addButton(
						option.getComponent(opt.get() ? OPTION_ON : OPTION_OFF),
						button -> {
							opt.set(!opt.get());
							button.setMessage(option.getComponent(opt.get() ? OPTION_ON : OPTION_OFF));
						},
						this.unlocked ? Button.NO_TOOLTIP : this.disabledTooltip);

				bn.active = this.unlocked;
			} else if (option instanceof EnumOption<?> opt) {
				Button bn = this.addButton(
						option.getComponent(new TranslatableComponent("dtdash." + opt.name + "." + opt.get().toString().toLowerCase(Locale.ROOT))),
						button -> {
							int index = (opt.get().ordinal() + 1) % opt.getValues().length;
							((Option<Object>) opt).set(opt.getValues()[index]);
							button.setMessage(option.getComponent(new TranslatableComponent("dtdash." + opt.name + "." + opt.get().toString().toLowerCase(Locale.ROOT))));
						},
						this.unlocked ? Button.NO_TOOLTIP : this.disabledTooltip);

				bn.active = this.unlocked;
			} else if (option instanceof DoubleOption || option instanceof FloatOption) {

			} else if (option instanceof TimeOption) {

			} else if (option instanceof ScreenPositionOption) {
				// don't handle this yet
			} else {
				throw new RuntimeException("Unknown option type " + option.getClass().getSimpleName());
			}
		}
	}

	@Override
	public void onClose() {
		// Save config if settings changed
		// TODO

		// set screen to parent
		super.onClose();
	}
}
