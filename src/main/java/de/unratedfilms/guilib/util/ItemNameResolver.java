
package de.unratedfilms.guilib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.StatCollector;

public class ItemNameResolver {

    public static String resolveUnknownName(Item item) {

        if (item == null) {
            return StatCollector.translateToLocal("itemname.air");
        }

        if (item instanceof ItemBlock) {
            Class<? extends Block> blockClass = ((ItemBlock) item).field_150939_a.getClass();
            String langKey = "itemname.blockclass." + blockClass.getSimpleName();
            if (StatCollector.canTranslate(langKey)) {
                return StatCollector.translateToLocal(langKey);
            }
        }

        return StatCollector.translateToLocal("itemname.unknown");
    }

    private ItemNameResolver() {}

}
