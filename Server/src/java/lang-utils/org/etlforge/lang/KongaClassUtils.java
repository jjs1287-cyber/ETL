  
                             
   
package org.etlforge.lang;

import java.lang.reflect.Constructor;

public final class KongaClassUtils {
    public static <T> Constructor<T> getSingleArgumentConstructor(Class<T> clazz, Class<?> clazz2) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] classArray = constructor.getParameterTypes();
            if (classArray.length != 1 || !clazz2.isAssignableFrom(classArray[0])) continue;
            return (Constructor<T>) constructor;
        }
        return null;
    }

    public static <T> Constructor<T> getNoArgumentConstuctor(Class<T> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length != 0) continue;
            return (Constructor<T>) constructor;
        }
        return null;
    }

    private KongaClassUtils() {
    }
}


