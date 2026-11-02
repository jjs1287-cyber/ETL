  
                             
   
package org.etlforge.lang;

import java.util.Arrays;

public final class Objects {
    public static boolean equal(Object object, Object object2) {
        return object == null ? object2 == null : object.equals(object2);
    }

    public static int hashCode(Object ... objectArray) {
        return Arrays.hashCode(objectArray);
    }

    private Objects() {
    }
}

