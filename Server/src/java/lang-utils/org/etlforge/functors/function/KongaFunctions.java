  
                             
   
                                        
                                   
   
package org.etlforge.functors.function;

import com.google.common.base.Function;
import java.io.Serializable;

public final class KongaFunctions {
    public static <T> Function<Object, T> cast(Class<T> clazz) {
        return new Cast<T>(clazz);
    }

    public static <T> Function<Object, T> strictCast(Class<T> clazz) {
        return new StrictCast<T>(clazz);
    }

    public static <E extends Enum<E>> Function<E, String> enumName() {
        return new EnumNameFunction();
    }

    public static Function<?, Boolean> notNull() {
        return NotNullFunction.INSTANCE;
    }

    public static Function<Boolean, Boolean> reverseBoolean() {
        return ReverseBooleanFunction.INSTANCE;
    }

    private KongaFunctions() {
    }

    private static enum ReverseBooleanFunction implements Function<Boolean, Boolean>
    {
        INSTANCE;


        public Boolean apply(Boolean bl) {
            return bl == false;
        }
    }

    private static enum NotNullFunction implements Function<Object, Boolean>
    {
        INSTANCE;


        public Boolean apply(Object object) {
            return object != null;
        }
    }

    private static class EnumNameFunction<E extends Enum<E>>
    implements Function<E, String>,
    Serializable {
        private EnumNameFunction() {
        }

        public String apply(E e) {
            return ((Enum)e).name();
        }
    }

    private static class StrictCast<T>
    implements Function<Object, T>,
    Serializable {
        private final Class<T> clazz;

        public StrictCast(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T apply(Object object) {
            return this.clazz.cast(object);
        }
    }

    private static class Cast<T>
    implements Function<Object, T>,
    Serializable {
        private final Class<T> clazz;

        public Cast(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T apply(Object object) {
            return this.clazz.isInstance(object) ? (T)this.clazz.cast(object) : null;
        }
    }
}

