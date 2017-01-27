
package de.unratedfilms.guilib.util;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemNameResolver {

    public static String resolveUnknownName(Item item) {

        if (item == null) {
            return translateToLocal("itemname.air");
        }

        if (item instanceof ItemBlock) {
            Class<? extends Block> blockClass = ((ItemBlock) item).getBlock().getClass();
            String langKey = "itemname.blockclass." + blockClass.getSimpleName();
            if (I18n.hasKey(langKey)) {
                return translateToLocal(langKey);
            }
        }

        return translateToLocal("itemname.unknown");
    }

    private ItemNameResolver() {}

    private static String translateToLocal(String key)
    {
        return I18n.format(key, new Object[0]);
    }

}
