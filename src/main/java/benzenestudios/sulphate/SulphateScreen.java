package benzenestudios.sulphate;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

/**
 * Screen that handles automatic placement of widgets.
 */
public abstract class SulphateScreen extends Screen {
	protected SulphateScreen(Component title) {
		this(title, null);
	}

	protected SulphateScreen(Component title, @Nullable Screen parent) {
		super(title);
		this.parent = parent;
	}

	// mostly appending and iterating so LinkedList
	private List<AbstractWidget> toRePositionY = new LinkedList<>();

	private Anchor anchor = Anchor.CENTRE;
	private IntSupplier anchorY = () -> this.height / 2;
	private IntSupplier anchorX = () -> this.width / 2;
	private int rows = 1;
	private ToIntFunction<AbstractWidget> ySeparation = w -> w.getHeight() + 4;
	private int xSeparation = 10;

	private int yOff;
	protected final Screen parent;

	// settings

	/**
	 * Set the current anchor on the x axis to match the the given anchor.
	 */
	protected void setAnchorX(Anchor anchor) {
		this.setAnchorX(anchor, this.anchorX);
	}

	protected void setAnchorX(Anchor anchor, IntSupplier position) {
		this.anchor = this.anchor.withX(anchor.x);
		this.anchorX = position;
	}

	/**
	 * Set the current anchor on the y axis to match the given anchor.
	 */
	protected void setAnchorY(Anchor anchor) {
		this.setAnchorX(anchor, this.anchorY);
	}

	protected void setAnchorY(Anchor anchor, IntSupplier position) {
		this.anchor = this.anchor.withY(anchor.y);
		this.anchorY = position;
	}

	protected void setAnchor(Anchor anchor) {
		this.setAnchor(anchor, this.anchorX, this.anchorY);
	}

	protected void setAnchor(Anchor anchor, IntSupplier xPos, IntSupplier yPos) {
		this.anchor = anchor;
		this.anchorX = xPos;
		this.anchorY = yPos;
	}

	/**
	 * @return get the anchor of the widgets on this screen.
	 */
	public Anchor getAnchor() {
		return this.anchor;
	}

	/**
	 * @return the position the widgets will be anchored to, at the current scale and dimensions.
	 */
	public Vec2i getAnchorPosition() {
		return new Vec2i(this.anchorX.getAsInt(), this.anchorY.getAsInt());
	}

	protected void setYSeparation(int separation) {
		this.ySeparation = w -> separation;
	}

	protected void setYSeparation(ToIntFunction<AbstractWidget> separation) {
		this.ySeparation = separation;
	}

	protected void setXSeparation(int separation) {
		this.xSeparation = separation;
	}

	protected void setSeparation(int x, int y) {
		this.ySeparation = w -> y;
		this.xSeparation = x;
	}

	public int getXSeparation() {
		return this.xSeparation;
	}

	public int getYSeparation(AbstractWidget widget) {
		return this.ySeparation.applyAsInt(widget);
	}

	protected void setRows(int rows) {
		this.rows = rows;
	}

	// implement this

	protected abstract void addWidgets();

	// can implement this

	public void afterInit() {
	}

	// adding stuff

	protected <T extends AbstractWidget> T addWidget(WidgetConstructor<T> constr, Component text) {
		return this.addWidget(constr, text, 200 - ((this.rows - 1) * 50), 20);
	}

	protected <T extends AbstractWidget> T addWidget(WidgetConstructor<T> constr, Component text, int width, int height) {
		T widget = constr.create(0, 0, width, height, text);
		this.toRePositionY.add(widget);
		return (T) super.addRenderableWidget(widget);
	}

	// the 10 billion overloads of addButton

	private static int defaultWidthFor(int rows) {
		return rows == 1 ? 200 : 150;
	}

	protected Button addButton(Component text, Button.OnPress onPress) {
		return this.addButton(Button::new, text, defaultWidthFor(this.rows), 20, onPress, Button.NO_TOOLTIP);
	}

	protected <T extends AbstractButton> T addButton(ButtonConstructor<T> constr, Component text, Button.OnPress onPress) {
		return this.addButton(constr, text, defaultWidthFor(this.rows), 20, onPress, Button.NO_TOOLTIP);
	}

	protected Button addButton(Component text, Button.OnPress onPress, Button.OnTooltip onTooltip) {
		return this.addButton(Button::new, text, defaultWidthFor(this.rows), 20, onPress, onTooltip);
	}

	protected <T extends AbstractButton> T addButton(ButtonConstructor<T> constr, Component text, Button.OnPress onPress, Button.OnTooltip onTooltip) {
		return this.addButton(constr, text, defaultWidthFor(this.rows), 20, onPress, onTooltip);
	}

	protected Button addButton(int width, int height, Component text, Button.OnPress onPress) {
		return this.addButton(Button::new, text, width, height, onPress, Button.NO_TOOLTIP);
	}

	protected <T extends AbstractButton> T addButton(ButtonConstructor<T> constr, Component text, int width, int height, Button.OnPress onPress) {
		return this.addButton(constr, text, width, height, onPress, Button.NO_TOOLTIP);
	}

	protected Button addButton(int width, int height, Component text, Button.OnPress onPress, Button.OnTooltip onTooltip) {
		return this.addButton(Button::new, text, width, height, onPress, onTooltip);
	}

	protected <T extends AbstractButton> T addButton(ButtonConstructor<T> constr, Component text, int width, int height, Button.OnPress onPress, Button.OnTooltip onTooltip) {
		T widget = constr.create(AUTO, AUTO, width, height, text, onPress, onTooltip);
		this.toRePositionY.add(widget);
		return (T) super.addRenderableWidget(widget);
	}

	// it's everywhere

	private AbstractButton done;

	protected AbstractButton addDone() {
		return this.addDone(Button::new, AUTO);
	}

	protected AbstractButton addDone(ButtonConstructor<?> cstr) {
		return this.addDone(cstr, AUTO);
	}

	protected AbstractButton addDone(int y) {
		return this.addDone(Button::new, y);
	}

	protected AbstractButton addDone(ButtonConstructor<?> cstr, int y) {
		this.addRenderableWidget(this.done = cstr.create(this.width / 2 - 100, y, 200, 20, CommonComponents.GUI_DONE, button -> this.onClose(), Button.NO_TOOLTIP));
		return this.done;
	}

	protected AbstractButton addDoneWithOffset(int yOffset) {
		return this.addDoneWithOffset(Button::new, yOffset);
	}

	protected AbstractButton addDoneWithOffset(ButtonConstructor<?> cstr, int yOffset) {
		this.addRenderableWidget(this.done = cstr.create(this.width / 2 - 100, AUTO_ADJUST + yOffset, 200, 20, CommonComponents.GUI_DONE, button -> this.onClose(), Button.NO_TOOLTIP));
		return this.done;
	}

	// impl stuff

	private int calculateHeight() {
		int nextSeparation = 0;
		int runningHeight = 0;
		int objs = 0;

		for (int i : this.toRePositionY.stream().mapToInt(this.ySeparation::applyAsInt).toArray()) {
			nextSeparation = Math.max(nextSeparation, i);

			if (++objs == this.rows) {
				runningHeight += nextSeparation;
				nextSeparation = 0;
				objs = 0; // reset object count
			}
		}

		return runningHeight + nextSeparation; // in case another row hasn't been completed
	}

	@Override
	protected final void init() {
		yOff = 2 * this.height / 3; // just in case
		this.addWidgets();

		if (!this.toRePositionY.isEmpty()) {
			yOff = this.anchorY.getAsInt();

			switch (this.anchor.y) {
			case -1:
				break;
			case 1:
				yOff -= calculateHeight();
				break;
			default:
				yOff -= calculateHeight() / 2;
				break;
			}

			int objs = 0;

			// so we can centre everything along x axis
			List<AbstractWidget> toRePositionX = new LinkedList<>();
			int rowWidth = 0;

			int nextSeparation = 0;

			for (AbstractWidget widget : this.toRePositionY) {
				widget.y = yOff;
				nextSeparation = Math.max(nextSeparation, this.ySeparation.applyAsInt(widget));
				++objs;
				rowWidth += widget.getWidth() + this.xSeparation;
				toRePositionX.add(widget);

				if (objs == this.rows) {
					this.repositionX(toRePositionX, rowWidth);

					yOff += nextSeparation;
					nextSeparation = 0; // reset next separation
					objs = rowWidth = 0; // reset row width and object count
					toRePositionX = new LinkedList<>();
				}
			}

			if (!toRePositionX.isEmpty()) {
				this.repositionX(toRePositionX, rowWidth);
				yOff += nextSeparation;
			}

			if (this.done != null && this.done.y > AUTO) yOff += this.done.y - AUTO_ADJUST; // auto adjust
		}

		if (this.done != null && this.done.y >= AUTO) {
			this.done.y = yOff;
		}

		this.afterInit();
	}

	private void repositionX(List<AbstractWidget> toRePositionX, int xOffset) {
		xOffset -= this.xSeparation; // clear the final spacer

		int x = this.anchorX.getAsInt();

		switch (this.anchor.x) {
		case -1:
			break;
		case 1:
			x -= xOffset;
			break;
		default:
			x -= xOffset / 2;
			break;
		}

		for (AbstractWidget widget : toRePositionX) {
			widget.x = x;
			x += widget.getWidth() + this.xSeparation;
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		drawCenteredString(matrices, this.font, this.title, this.width / 2, 15, 0xFFFFFF);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		this.toRePositionY.clear();
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}

	// this is the year cosmetica been providing better minecraft cosmetics for free since
	private static final int AUTO = 42069;
	private static final int AUTO_ADJUST = 69420;

	@FunctionalInterface
	public interface WidgetConstructor<T extends AbstractWidget> {
		T create(int x, int y, int width, int height, Component text);
	}

	@FunctionalInterface
	public interface ButtonConstructor<T extends AbstractButton> {
		T create(int x, int y, int width, int height, Component text, Button.OnPress onPress, Button.OnTooltip onTooltip);
	}
}
