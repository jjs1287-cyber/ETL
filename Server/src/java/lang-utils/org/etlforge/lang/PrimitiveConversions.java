  
                             
   
package org.etlforge.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PrimitiveConversions {
    public static int[] toIntArray(Collection<Integer> collection) {
        int[] nArray = new int[collection.size()];
        int n = 0;
        for (Integer n2 : collection) {
            nArray[n++] = n2;
        }
        return nArray;
    }

    public static List<Integer> toIntegerList(int ... nArray) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int n : nArray) {
            arrayList.add(n);
        }
        return arrayList;
    }

    public static long[] toLongArray(Collection<Long> collection) {
        long[] lArray = new long[collection.size()];
        int n = 0;
        for (Long l : collection) {
            lArray[n++] = l;
        }
        return lArray;
    }

    private PrimitiveConversions() {
    }
}

