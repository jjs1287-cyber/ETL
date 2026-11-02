package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.Map;

public abstract class StringConverter<V> implements Function<String, V> {
   public static final StringConverter<String> IDENTITY = new StringConverter<String>() {
      public String apply(String var1) {
         return var1;
      }
   };
   public static final StringConverter<Integer> TO_INT = new StringConverter<Integer>() {
      public Integer apply(String var1) {
         return Integer.valueOf(var1);
      }
   };
   public static final StringConverter<Long> TO_LONG = new StringConverter<Long>() {
      public Long apply(String var1) {
         return Long.valueOf(var1);
      }
   };
   public static final StringConverter<Short> TO_SHORT = new StringConverter<Short>() {
      public Short apply(String var1) {
         return Short.valueOf(var1);
      }
   };
   public static final StringConverter<Character> TO_CHAR = new StringConverter<Character>() {
      public Character apply(String var1) {
         if (var1.length() != 1) {
            throw new IllegalArgumentException("s must be of length 1 (was " + var1.length() + ")");
         } else {
            return var1.charAt(0);
         }
      }
   };
   public static final StringConverter<Byte> TO_BYTE = new StringConverter<Byte>() {
      public Byte apply(String var1) {
         return Byte.valueOf(var1);
      }
   };
   public static final StringConverter<Float> TO_FLOAT = new StringConverter<Float>() {
      public Float apply(String var1) {
         return Float.valueOf(var1);
      }
   };
   public static final StringConverter<Double> TO_DOUBLE = new StringConverter<Double>() {
      public Double apply(String var1) {
         return Double.valueOf(var1);
      }
   };
   public static final StringConverter<Boolean> TO_BOOLEAN = new StringConverter<Boolean>() {
      public Boolean apply(String var1) {
         return Boolean.valueOf(var1);
      }
   };
   public static final StringConverter<Class> TO_CLASS = new StringConverter<Class>() {
      public Class apply(String var1) {
         try {
            return Class.forName(var1);
         } catch (ClassNotFoundException var3) {
            throw new RuntimeException(var3);
         }
      }
   };
   private static final Map<Class, StringConverter> builtIn = Maps.newHashMap();

   public static <E extends Enum<E>> StringConverter<E> toEnum(final Class<E> var0) {
      return new StringConverter<E>() {
         public E apply(String var1) {
            return Enum.valueOf(var0, var1);
         }
      };
   }

   public static <T> StringConverter<T> builtIn(Class<T> var0) {
      StringConverter var1 = builtIn.get(var0);
      if (var1 != null) {
         return var1;
      }

      if (var0.isEnum()) {
      }

      return (StringConverter<T>)(Class.class.isAssignableFrom(var0) ? TO_CLASS : null);
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
