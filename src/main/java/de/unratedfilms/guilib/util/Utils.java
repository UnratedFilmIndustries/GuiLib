
package de.unratedfilms.guilib.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

    @SafeVarargs
    public static <E> List<E> mutableListWith(List<E> base, E... append) {

        List<E> newList = new ArrayList<>(base.size() + append.length);

        newList.addAll(base);
        Collections.addAll(newList, append);

        return newList;
    }

    public Utils() {}

}
