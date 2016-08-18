
package de.unratedfilms.guilib.widgets.model;

import net.minecraft.item.ItemStack;

/**
 * Abstract representation of a {@link Button} that displays an {@link ItemStack} on its surface.
 */
public interface ButtonItem extends Button {

    public ItemStack getItemStack();

    public void setItemStack(ItemStack itemStack);

}
