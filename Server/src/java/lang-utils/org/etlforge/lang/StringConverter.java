  
                             
   
                                        
                                   
                                  
   
package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.Map;

public abstract class StringConverter<V>
implements Function<String, V> {
    public static final StringConverter<String> IDENTITY = new StringConverter<String>(){

        public String apply(String string) {
            return string;
        }
    };
    public static final StringConverter<Integer> TO_INT = new StringConverter<Integer>(){

        public Integer apply(String string) {
            return Integer.valueOf(string);
        }
    };
    public static final StringConverter<Long> TO_LONG = new StringConverter<Long>(){

        public Long apply(String string) {
            return Long.valueOf(string);
        }
    };
    public static final StringConverter<Short> TO_SHORT = new StringConverter<Short>(){

        public Short apply(String string) {
            return Short.valueOf(string);
        }
    };
    public static final StringConverter<Character> TO_CHAR = new StringConverter<Character>(){

        public Character apply(String string) {
            if (string.length() != 1) {
                throw new IllegalArgumentException("s must be of length 1 (was " + string.length() + ")");
            }
            return Character.valueOf(string.charAt(0));
        }
    };
    public static final StringConverter<Byte> TO_BYTE = new StringConverter<Byte>(){

        public Byte apply(String string) {
            return Byte.valueOf(string);
        }
    };
    public static final StringConverter<Float> TO_FLOAT = new StringConverter<Float>(){

        public Float apply(String string) {
            return Float.valueOf(string);
        }
    };
    public static final StringConverter<Double> TO_DOUBLE = new StringConverter<Double>(){

        public Double apply(String string) {
            return Double.valueOf(string);
        }
    };
    public static final StringConverter<Boolean> TO_BOOLEAN = new StringConverter<Boolean>(){

        public Boolean apply(String string) {
            return Boolean.valueOf(string);
        }
    };
    public static final StringConverter<Class> TO_CLASS = new StringConverter<Class>(){

        public Class apply(String string) {
            try {
                return Class.forName(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new RuntimeException(classNotFoundException);
            }
        }
    };
    private static final Map<Class, StringConverter> builtIn = Maps.newHashMap();

    public static <E extends Enum<E>> StringConverter<E> toEnum(final Class<E> clazz) {
        return new StringConverter<E>(){

            public E apply(String string) {
                return Enum.valueOf(clazz, string);
            }
        };
    }

    public static <T> StringConverter<T> builtIn(Class<T> clazz) {
        StringConverter stringConverter = builtIn.get(clazz);
        if (stringConverter != null) {
            return stringConverter;
        }
        if (clazz.isEnum()) {
                             
        }
        if (Class.class.isAssignableFrom(clazz)) {
            return (StringConverter<T>)(StringConverter<?>) TO_CLASS;
        }
        return null;
    }

    static {
        builtIn.put(Boolean.class, TO_BOOLEAN);
        builtIn.put(Byte.class, TO_BYTE);
        builtIn.put(Character.class, TO_CHAR);
        builtIn.put(Double.class, TO_DOUBLE);
        builtIn.put(Float.class, TO_FLOAT);
        builtIn.put(Integer.class, TO_INT);
        builtIn.put(Long.class, TO_LONG);
        builtIn.put(Short.class, TO_SHORT);
    }
}


