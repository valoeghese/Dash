package valoeghese.dash.client.screen;

import benzenestudios.sulphate.SulphateScreen;
import benzenestudios.sulphate.WidgetConstructor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import valoeghese.dash.Dash;
import valoeghese.dash.ScreenPosition;
import valoeghese.dash.config.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

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
	private boolean settingsModified = false;
	private AbstractButton done = null;
	private final Set<String> invalid = new HashSet<>();

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
							this.settingsModified = true;
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
							this.settingsModified = true;
							button.setMessage(option.getComponent(new TranslatableComponent("dtdash." + opt.name + "." + opt.get().toString().toLowerCase(Locale.ROOT))));
						},
						this.unlocked ? Button.NO_TOOLTIP : this.disabledTooltip);

				bn.active = this.unlocked;
			} else if (option instanceof NumericalOption<?> opt) {
				Label label = this.addWidget(Label::new, option.getComponent());
				label.colour = 0xAAAAAA;

				NumberEditBox edit = this.addWidget(NumberEditBox::new, option.getComponent());
				edit.setMaxLength(128);
				edit.setValue(String.valueOf(opt.getAsDouble()));
				edit.setResponder((string) -> {
					try {
						opt.setFromDouble(edit.getDoubleValue());
						this.invalid.remove(option.name);
						this.settingsModified = true;
					} catch (IllegalArgumentException e) {
						this.invalid.add(option.name);
					}

					this.done.active = this.invalid.isEmpty();
				});
			} else if (option instanceof ScreenPositionOption opt) {
				WidgetConstructor<EditBox> editBoxMaker = (x, y, width, height, component) -> new EditBox(
						this.font, x, y, width, height, component
				);

				// Edit X value -- when setting, use existing Y value
				Label label = this.addWidget(Label::new, option.getComponent("X"));
				label.colour = 0xAAAAAA;

				EditBox editX = this.addWidget(
						editBoxMaker,
						option.getComponent("X")
				);
				editX.setMaxLength(128);
				editX.setValue(opt.get().getXRepresentation());
				editX.setResponder((string) -> {
					try {
						opt.set(ScreenPosition.parse(editX.getValue(), opt.get().getYRepresentation()));
						this.invalid.remove(option.name);
						this.settingsModified = true;
					} catch (IllegalArgumentException e) {
						this.invalid.add(option.name);
					}

					this.done.active = this.invalid.isEmpty();
				});

				// Edit Y value -- when setting, use existing X value
				label = this.addWidget(Label::new, option.getComponent("Y"));
				label.colour = 0xAAAAAA;

				EditBox editY = this.addWidget(
						editBoxMaker,
						option.getComponent("Y")
				);
				editY.setMaxLength(128);
				editY.setValue(opt.get().getYRepresentation());
				editY.setResponder((string) -> {
					try {
						opt.set(ScreenPosition.parse(opt.get().getXRepresentation(), editY.getValue()));
						this.invalid.remove(option.name);
						this.settingsModified = true;
					} catch (IllegalArgumentException e) {
						this.invalid.add(option.name);
					}

					this.done.active = this.invalid.isEmpty();
				});
			} else {
				throw new RuntimeException("Unknown option type " + option.getClass().getSimpleName());
			}
		}

		this.done = this.addDone((x, y, width, height, text, onPress, onTooltip) -> new Button(
				x, y, width, height, text, onPress, new Button.OnTooltip() {
			@Override
			public void onTooltip(Button button, PoseStack poseStack, int i, int j) {
				if (!DashConfigSubScreen.this.done.active) {
					Screen screen = DashConfigSubScreen.this;
					Component text = new TranslatableComponent("screen.dtdash.config.invalidOptions",
							String.join(" ", DashConfigSubScreen.this.invalid));

					screen.renderTooltip(poseStack, Minecraft.getInstance().font.split(
							text,
							Math.max(screen.width / 2 - 43, 170)
					), i, j + 18);
				}
			}

			@Override
			public void narrateTooltip(Consumer<Component> consumer) {
				if (!DashConfigSubScreen.this.done.active) {
					Component text = new TranslatableComponent("screen.dtdash.config.invalidOptions",
							String.join(" ", DashConfigSubScreen.this.invalid));

					consumer.accept(text);
				}
			}
		}
		));
	}

	@Override
	public void onClose() {
		// Save config if settings changed

		if (this.settingsModified) {
			// the only config that can be modified in the settings is the one stored in localConfig so save that
			CONFIG_SAVE.execute(() -> {
				Properties properties = new Properties();
				Dash.localConfig.save(properties, true);

				try (FileWriter writer = new FileWriter(DashConfig.FILE)) {
					properties.store(writer, "Double-Tap Dash mod config.");
					Dash.LOGGER.info("Saved dash config.");
				} catch (IOException e) {
					Dash.LOGGER.error("Failed to save dash config!", e);
				}
			});
		}

		// set screen to parent
		super.onClose();
	}

	private static final Executor CONFIG_SAVE = Executors.newSingleThreadExecutor();
}
