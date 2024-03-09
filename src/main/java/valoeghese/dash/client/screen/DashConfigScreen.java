package valoeghese.dash.client.screen;

import benzenestudios.sulphate.SulphateScreen;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import valoeghese.dash.Dash;
import valoeghese.dash.config.Option;

public class DashConfigScreen extends SulphateScreen {
	public DashConfigScreen(Screen parent) {
		super(new TranslatableComponent("screen.dtdash.config"), parent);
	}

	@Override
	protected void addWidgets() {
		// show the values actively being used
		// thus for server-controlled options, show activeConfig
		this.addSubMenu(new TranslatableComponent("screen.dtdash.momentum"), ImmutableList.of(
				Dash.activeConfig.strength,
				Dash.activeConfig.yVelocity,
				Dash.activeConfig.momentumMode
		), false);

		this.addSubMenu(new TranslatableComponent("screen.dtdash.directions"), ImmutableList.of(
				Dash.activeConfig.diagonalDash,
				Dash.activeConfig.forwardDash,
				Dash.activeConfig.backwardsDash,
				Dash.activeConfig.leftDash,
				Dash.activeConfig.rightDash
		), false);

		this.addSubMenu(new TranslatableComponent("screen.dtdash.contexts"), ImmutableList.of(
				Dash.activeConfig.dashMidair,
				Dash.activeConfig.dashWhileGliding,
				Dash.activeConfig.dashWhileSwimming,
				Dash.activeConfig.dashWhileFloating
		), false);

		this.addSubMenu(new TranslatableComponent("screen.dtdash.client"), ImmutableList.of(
				Dash.localConfig.doubleTapDash,
				Dash.localConfig.sensitivity,
				Dash.localConfig.iconPosition
		), true);

		this.addSubMenu(new TranslatableComponent("screen.dtdash.miscellaneous"), ImmutableList.of(
				Dash.activeConfig.cooldown,
				Dash.activeConfig.resetAttack,
				Dash.activeConfig.exhaustion
		), false);

		this.addDone();
	}

	private void addSubMenu(Component name, ImmutableList<Option<?>> options, boolean client) {
		this.addButton(name, bn -> this.minecraft.setScreen(
				new DashConfigSubScreen(title, this, options, client || Dash.activeConfig == Dash.localConfig)
		));
	}
}
