
package de.unratedfilms.guilib.widgets.view.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetTooltipable;
import de.unratedfilms.guilib.util.ItemNameResolver;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.ButtonItem;
import de.unratedfilms.guilib.widgets.view.adapters.ButtonAdapter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * This class implements a {@link Button} that shows an {@link ItemStack} instead of a label.
 * This button supports "Air" - an item stack without an item.
 * Note that the air representation is volatile - {@link #getItemStack()} returns null, and calling {@link ItemStack#toString()} on the item stack will crash.<br>
 * <br>
 * Note that items use zLevel for rendering - change zLevel as needed.
 */
public class ButtonItemImpl extends ButtonAdapter implements ButtonItem, WidgetTooltipable {

    public static final int        SIZE          = 18;
    public static final RenderItem ITEM_RENDERER = MC.getRenderItem();

    private ItemStack              itemStack;
    private TextTooltipImpl        tooltip;

    public ButtonItemImpl(ItemStack itemStack, ButtonHandler handler) {

        super(handler);

        zLevel = 100;
        setItemStack(itemStack);
    }

    @Override
    public ItemStack getItemStack() {

        return itemStack;
    }

    @Override
    public ButtonItemImpl setItemStack(ItemStack itemStack) {

        this.itemStack = itemStack;
        tooltip = generateTooltip();

        return this;
    }

    @SuppressWarnings ("unchecked")
    protected TextTooltipImpl generateTooltip() {

        List<String> tooltipLines;
        FontRenderer font = MC.fontRendererObj;

        if (itemStack.getItem() == null) {
            tooltipLines = Arrays.asList(ItemNameResolver.resolveUnknownName(null));
        } else {
            tooltipLines = itemStack.getTooltip(MC.player, MC.gameSettings.advancedItemTooltips);

            if (!tooltipLines.isEmpty()) {
                String line0 = tooltipLines.get(0);
                if (line0.startsWith("tile.null.name")) {
                    line0 = line0.replace("tile.null.name", ItemNameResolver.resolveUnknownName(itemStack.getItem()));
                }

                // Line 0 should have the rarity color
                tooltipLines.set(0, itemStack.getRarity().rarityColor + line0);

                // All other lines should be colored gray
                for (ListIterator<String> iter = tooltipLines.listIterator(1); iter.hasNext();) {
                    iter.set(ChatFormatting.GRAY + iter.next());
                }
            }

            // Get the custom font for the item; if there isn't one, use the default font
            if (itemStack.getItem() != null && itemStack.getItem().getFontRenderer(itemStack) != null) {
                font = itemStack.getItem().getFontRenderer(itemStack);
            }
        }

        return new TextTooltipImpl(tooltipLines, font);
    }

    @Override
    protected void doRevalidate() {

        setSize(SIZE, SIZE);
    }

    /**
     * Draws the item or string "Air" if stack.getItem() is null
     */
    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        boolean hover = inLocalBounds(viewport, lmx, lmy);
        if (hover) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x55909090);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (itemStack.getItem() != null) {
            RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            ITEM_RENDERER.zLevel = zLevel;
            ITEM_RENDERER.renderItemAndEffectIntoGUI(itemStack, getX() + 1, getY() + 1);
            ITEM_RENDERER.zLevel = 0;
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
        } else {
            // Air
            drawString(MC.fontRendererObj, "Air", getX() + 3, getY() + 5, -1);
        }
    }

    @Override
    public Widget getTooltip(int hoveredMillis) {

        return tooltip;
    }

}
