
package de.unratedfilms.guilib.widgets.model;

import net.minecraft.item.ItemStack;
import de.unratedfilms.guilib.core.WidgetRigid;

/**
 * Abstract representation of a {@link Button} that displays an {@link ItemStack} on its surface.
 */
public interface ButtonItem extends Button, WidgetRigid {

    public ItemStack getItemStack();

    public void setItemStack(ItemStack itemStack);

}
