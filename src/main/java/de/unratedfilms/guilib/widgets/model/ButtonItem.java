
package de.unratedfilms.guilib.widgets.model;

import de.unratedfilms.guilib.core.WidgetRigid;
import net.minecraft.item.ItemStack;

/**
 * Abstract representation of a {@link Button} that displays an {@link ItemStack} on its surface.
 */
public interface ButtonItem extends Button, WidgetRigid {

    public ItemStack getItemStack();

    public ButtonItem setItemStack(ItemStack itemStack);

}
