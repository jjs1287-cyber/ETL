  
                             
   
package org.etlforge.lang;

import java.lang.reflect.Constructor;
import org.etlforge.lang.KongaClassUtils;

public class SingleArgInstantiator<T> {
    private final Class<T> clazz;
    private Class<?>[] argTypesInOrderToLook;
    private Object[] args;

    public static <E> SingleArgInstantiator<E> create(Class<E> clazz) {
        return new SingleArgInstantiator<E>(clazz);
    }

    public SingleArgInstantiator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setArgumentTypesInOrderToLookFor(Class<?> ... classArray) {
        this.argTypesInOrderToLook = classArray;
    }

    public void setArguments(Object ... objectArray) {
        this.args = objectArray;
    }

    public T instantiate() throws Exception {
        for (int i = 0; i < this.argTypesInOrderToLook.length; ++i) {
            Constructor<T> constructor = KongaClassUtils.getSingleArgumentConstructor(this.clazz, this.argTypesInOrderToLook[i]);
            if (constructor == null) continue;
            return constructor.newInstance(this.args[i]);
        }
        return null;
    }
}

