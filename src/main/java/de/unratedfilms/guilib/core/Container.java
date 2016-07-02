
package de.unratedfilms.guilib.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import de.unratedfilms.guilib.core.Scrollbar.Shiftable;
import de.unratedfilms.guilib.focusable.FocusableWidget;

public class Container {

    protected final Minecraft           mc               = Minecraft.getMinecraft();

    private final List<Widget>          widgets          = new ArrayList<>();
    private final List<FocusableWidget> focusableWidgets = new ArrayList<>();

    private int                         left, right, top, bottom, contentHeight;
    private boolean                     clipping         = true;

    // Scrolling
    private final int                   shiftAmount, extraScrollHeight;
    private int                         scrollbarWidth;
    private final Scrollbar             scrollbar;

    // Focus
    private int                         focusIndex       = -1;
    private Widget                      lastSelected;

    /**
     * Create this Container, which by default will clip the elements contained.
     *
     * @param scrollbar The scrollbar for this container
     * @param shiftAmount The amount to shift the scrollbar when focus is shifted;
     *        this should normally be the height of the FocusableWidget, depending on spacing.
     * @param extraScrollHeight The sum of the supposed gap between the top+bottom of this container
     *        and the FocusableWidgets that are inside. Say that the items in the list should go from this container's top+2
     *        to this container's bottom-2, then extraScrollHeight should be 4.
     */
    public Container(Scrollbar scrollbar, int shiftAmount, int extraScrollHeight) {

        this.scrollbar = scrollbar;
        this.shiftAmount = shiftAmount;
        this.extraScrollHeight = extraScrollHeight;

        if (scrollbar != null) {
            scrollbar.setContainer(this);
            scrollbarWidth = scrollbar.width;
        }
    }

    /**
     * Create this Container, which by default will clip the elements contained.
     */
    public Container() {

        this(null, 0, 0);
    }

    public void revalidate(int x, int y, int width, int height) {

        left = x;
        right = x + width;
        top = y;
        bottom = y + height;
        calculateContentHeight();
        if (scrollbar != null) {
            scrollbar.revalidate(top, bottom);
            scrollbarWidth = scrollbar.width;
        }
    }

    public List<Widget> getWidgets() {

        return widgets;
    }

    public List<FocusableWidget> getFocusableWidgets() {

        return focusableWidgets;
    }

    public void addWidgets(Widget... widgets) {

        for (Widget widget : widgets) {
            this.widgets.add(widget);
            if (widget instanceof FocusableWidget) {
                focusableWidgets.add((FocusableWidget) widget);
            }
        }

        calculateContentHeight();
    }

    private void calculateContentHeight() {

        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Widget w : widgets) {
            if (w instanceof Shiftable) {
                if (w.y < minY) {
                    minY = w.y;
                }
                if (w.y + w.height > maxY) {
                    maxY = w.y + w.height;
                }
            }
        }
        contentHeight = minY > maxY ? 0 : maxY - minY + extraScrollHeight;
    }

    public int getContentHeight() {

        return contentHeight;
    }

    public void update() {

        for (Widget w : widgets) {
            w.update();
        }
    }

    public List<Widget> draw(int mouseX, int mouseY, int scale) {

        if (clipping) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(left * scale, mc.displayHeight - bottom * scale, (right - left - scrollbarWidth) * scale, (bottom - top) * scale);
        }

        List<Widget> overlays = new ArrayList<>();

        /*
         * Fix to prevent clipped widgets from thinking that they extend
         * outside of the container when checking for hover.
         */
        boolean mouseInBounds = inBounds(mouseX, mouseY);
        int widgetX = mouseInBounds || !clipping ? mouseX : -1;
        int widgetY = mouseInBounds || !clipping ? mouseY : -1;

        for (Widget w : widgets) {
            if (w.shouldRender(top, bottom)) {
                w.draw(widgetX, widgetY);
                overlays.addAll(w.getTooltips());
            }
        }

        // Don't clip the scrollbar!
        if (clipping) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.draw(mouseX, mouseY);
        }

        return overlays;
    }

    public void setFocused(FocusableWidget f) {

        int newIndex = f == null ? -1 : focusableWidgets.indexOf(f);
        if (focusIndex != newIndex) {
            if (focusIndex != -1) {
                focusableWidgets.get(focusIndex).focusLost();
            }
            if (newIndex != -1) {
                focusableWidgets.get(newIndex).focusGained();
            }

            focusIndex = newIndex;
        }
    }

    public boolean inBounds(int mx, int my) {

        return mx >= left && my >= top && mx < right && my < bottom;
    }

    public boolean mouseClicked(int mx, int my) {

        if (inBounds(mx, my)) {
            boolean resetFocus = true;

            if (scrollbar != null && scrollbar.shouldRender(top, bottom) && scrollbar.inBounds(mx, my)) {
                return true;
            }

            for (Widget w : widgets) {
                if (w.shouldRender(top, bottom) && w.click(mx, my)) {
                    lastSelected = w;
                    if (w instanceof FocusableWidget) {
                        setFocused((FocusableWidget) w);
                        resetFocus = false;
                    }
                    w.handleClick(mx, my);
                    break;
                }
            }
            if (resetFocus) {
                setFocused(null);
            }
            return true;
        }
        return false;
    }

    public FocusableWidget deleteFocused() {

        if (hasFocusedWidget()) {
            FocusableWidget w = getFocusedWidget();
            if (lastSelected == w) {
                lastSelected = null;
            }
            focusableWidgets.remove(focusIndex);
            if (focusableWidgets.size() == 0) {
                focusIndex = -1;
            } else {
                focusIndex = MathHelper.clamp_int(focusIndex, 0, focusableWidgets.size() - 1);
                focusableWidgets.get(focusIndex).focusGained();
            }

            int index = widgets.indexOf(w), offset = Integer.MAX_VALUE;
            for (int i = index + 1; i < widgets.size(); ++i) {
                Widget cur = widgets.get(i);
                if (cur instanceof Shiftable) {
                    if (offset == Integer.MAX_VALUE) {
                        offset = w.getY() - cur.getY();
                    }
                    ((Shiftable) cur).shiftY(offset);
                }
            }
            widgets.remove(w);
            calculateContentHeight();
            if (scrollbar != null) {
                scrollbar.onChildRemoved();
            }

            return w;
        }
        return null;
    }

    public void removeFocusableWidgets() {

        focusIndex = -1;
        if (lastSelected instanceof FocusableWidget) {
            lastSelected = null;
        }

        widgets.removeAll(focusableWidgets);
        focusableWidgets.clear();

        calculateContentHeight();
        if (scrollbar != null) {
            scrollbar.onChildRemoved();
        }
    }

    public void mouseReleased(int mx, int my) {

        if (lastSelected != null) {
            lastSelected.mouseReleased(mx, my);
            lastSelected = null;
        }
    }

    public boolean hasFocusedWidget() {

        return focusIndex != -1;
    }

    public FocusableWidget getFocusedWidget() {

        return hasFocusedWidget() ? focusableWidgets.get(focusIndex) : null;
    }

    public boolean keyTyped(char typedChar, int keyCode) {

        boolean handled = focusIndex != -1 ? focusableWidgets.get(focusIndex).keyTyped(typedChar, keyCode) : false;
        if (!handled) {
            switch (keyCode) {
                case Keyboard.KEY_UP:
                    shift(-1);
                    handled = true;
                    break;
                case Keyboard.KEY_DOWN:
                    shift(1);
                    handled = true;
                    break;
                case Keyboard.KEY_TAB:
                    shiftFocusToNext();
                    handled = true;
                    break;
            }
        }
        return handled;
    }

    protected void shiftFocusToNext() {

        if (focusIndex != -1 && focusableWidgets.size() > 1) {
            int newIndex = (focusIndex + 1) % focusableWidgets.size();
            if (newIndex != focusIndex) {
                focusableWidgets.get(focusIndex).focusLost();
                focusableWidgets.get(newIndex).focusGained();
                if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
                    scrollbar.shift( (focusIndex - newIndex) * shiftAmount);
                }
                focusIndex = newIndex;
            }
        }
    }

    protected void shiftFocus(int newIndex) {

        if (focusIndex != newIndex) {
            focusableWidgets.get(focusIndex).focusLost();
            focusableWidgets.get(newIndex).focusGained();
            if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
                scrollbar.shift( (focusIndex - newIndex) * shiftAmount);
            }
            focusIndex = newIndex;
        }
    }

    protected void shift(int delta) {

        if (focusIndex != -1) {
            shiftFocus(MathHelper.clamp_int(focusIndex + delta, 0, focusableWidgets.size() - 1));
        } else if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.shiftRelative(delta * -4);
        }
    }

    public void mouseWheel(int delta) {

        if (scrollbar != null && scrollbar.shouldRender(top, bottom)) {
            scrollbar.shiftRelative(delta);
        } else {
            boolean done = false;
            if (focusIndex != -1) {
                done = focusableWidgets.get(focusIndex).mouseWheel(delta);
            } else {
                for (Iterator<Widget> it = widgets.iterator(); it.hasNext() && !done;) {
                    done = it.next().mouseWheel(delta);
                }
            }
        }
    }

    public int left() {

        return left;
    }

    public int right() {

        return right;
    }

    public int top() {

        return top;
    }

    public int bottom() {

        return bottom;
    }

    public void setClipping(boolean clipping) {

        this.clipping = clipping;
    }

    public boolean isClipping() {

        return clipping;
    }

}
