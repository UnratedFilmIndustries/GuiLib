
package de.unratedfilms.guilib.widgets.view.impl;

import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetParent;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.util.FontUtils;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.Button.ButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Dropdown;
import de.unratedfilms.guilib.widgets.model.Dropdown.Option;
import de.unratedfilms.guilib.widgets.model.Scrollbar;
import de.unratedfilms.guilib.widgets.view.adapters.DropdownAdapter;

/**
 * Default style {@link Dropdown} that displays labels for the different options.
 *
 * @param <O> The type of {@link Option} that can be selected through this dropdown menu.
 */
public class DropdownLabelImpl<O extends Option<String>> extends DropdownAdapter<O> implements WidgetParent {

    private static final int              EXT_MARGIN       = 10;
    private static final int              OPTION_HEIGHT    = 12;
    private static final int              OPTION_H_PADDING = 6;

    private static final ResourceLocation TEXTURE          = new ResourceLocation("textures/gui/widgets.png");

    // The dropdown that appears when you click on the dropdown button
    private ContainerFlexible             ext;
    private Scrollbar                     extScrollbar;

    public DropdownLabelImpl(Collection<O> options) {

        this(options, null);
    }

    public DropdownLabelImpl(Collection<O> options, DropdownHandler<O> handler) {

        super(options, handler);

        setSize(200, 20);
    }

    public int getMaxOptionTextWidth() {

        int maxOptionWidth = 0;
        for (O option : getOptions()) {
            maxOptionWidth = Math.max(maxOptionWidth, MC.fontRendererObj.getStringWidth(option.getDisplayObject()));
        }
        return maxOptionWidth;
    }

    public int getExtWidth() {

        return getMaxOptionTextWidth() + 2 * OPTION_H_PADDING + extScrollbar.getWidth(); // horizontal gap on each side, plus the scrollbar width
    }

    /*
     * Event handlers
     */

    @Override
    public DropdownLabelImpl<O> setOptions(Collection<O> options) {

        super.setOptions(options);

        generateExt();
        invalidate();

        return this;
    }

    private void generateExt() {

        extScrollbar = new ScrollbarImpl(0);
        ext = new ContainerScrollableImpl(extScrollbar);
        ext
                .appendLayoutManager(c -> {
                    extScrollbar.setPosition(ext.getWidth() - extScrollbar.getWidth(), 0);

                    for (Widget widget : ext.getWidgets()) {
                        ((ButtonLabel) widget).setSize(getMaxOptionTextWidth() + 2 * OPTION_H_PADDING, OPTION_HEIGHT);
                    }
                })
                .appendLayoutManager(new FlowLayout(Axis.Y, 0, 0));

        for (final O option : getOptions()) {
            Button optionButton = new ButtonLabelImpl(option.getDisplayObject(), new ButtonHandler() {

                @Override
                public void buttonClicked(Button button, MouseButton mouseButton) {

                    setSelectedOption(option);
                    focusLost();

                    if (getHandler() != null) {
                        getHandler().optionSelected(DropdownLabelImpl.this, option);
                    }
                }

            });
            ext.addWidgets(optionButton);
        }
    }

    @Override
    protected void revalidateThis(Viewport viewport) {

        ext.setY(getY() + getHeight());
        ext.setWidth(getExtWidth());

        // Make sure that ext does not peek out of the right side of the Minecraft window
        int maxX = viewport.localX(viewport.getScreenSize().getWidth() - EXT_MARGIN - ext.getWidth());
        ext.setX(MathHelper.clamp(0, getX(), maxX));

        // Make ext as high as necessary, but don't let it peek out of the lower side of the Minecraft window
        int maxHeight = viewport.getScreenSize().getHeight() - EXT_MARGIN - viewport.globalY(getY() + ext.getY());
        ext.setHeight(MathHelper.clamp(getOptions().size() * OPTION_HEIGHT, 0, maxHeight));
    }

    /*
     * Drawing code
     */

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        boolean hover = inLocalBounds(viewport, lmx, lmy);

        int u = 0, v = 46 + (hover ? 40 : 20);
        GuiUtils.drawContinuousTexturedBox(TEXTURE, getX(), getY(), u, v, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, zLevel);

        String actualLabel = FontUtils.abbreviateIfTooLong(MC.fontRendererObj, getSelectedOption().getDisplayObject(), getWidth() - 6);
        drawCenteredString(MC.fontRendererObj, actualLabel, getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2, hover ? 16777120 : 14737632);
    }

    /*
     * Forwarding events to ext, removing the scissor area
     */

    @Override
    public List<Widget> getChildren() {

        return isFocused() ? ImmutableList.of(ext) : ImmutableList.of();
    }

    @Override
    public Viewport getChildViewport(Viewport viewport, Widget child) {

        if (child == ext) {
            return viewport.withoutScissor();
        }

        throw new UnknownChildException(this, child);
    }

}
