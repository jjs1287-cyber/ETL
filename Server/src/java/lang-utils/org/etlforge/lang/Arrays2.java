  
                             
   
                                        
                                   
                                    
                                    
                                   
                                       
   
package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public final class Arrays2 {
    public static <F> Object[] transform(F[] FArray, Function<? super F, ?> function) {
        int n = FArray.length;
        Object[] objectArray = new Object[n];
        for (int i = 0; i < n; ++i) {
            objectArray[i] = function.apply(FArray[i]);
        }
        return objectArray;
    }

    public static <F, T> T[] transform(F[] FArray, Class<T> clazz, Function<? super F, ? extends T> function) {
        int n = FArray.length;
        T[] objectArray = (T[])Array.newInstance(clazz, n);
        for (int i = 0; i < n; ++i) {
            objectArray[i] = function.apply(FArray[i]);
        }
        return objectArray;
    }

    public static <F> String[] stringify(F[] FArray) {
        return Arrays2.transform(FArray, String.class, Functions.toStringFunction());
    }

    public static <T> String join(T[] TArray, Function<? super T, String> function, String string) {
        Object[] objectArray = Arrays2.transform(TArray, String.class, function);
        return StringUtils.join((Object[])objectArray, (String)string);
    }

    public static <T> T[] append(T[] TArray, T[] TArray2) {
        T[] TArray3 = Arrays.copyOf(TArray, TArray.length + TArray2.length);
        System.arraycopy(TArray2, 0, TArray3, TArray.length, TArray2.length);
        return TArray3;
    }

    public static <T> T[] reverse(T[] TArray) {
        int n = 0;
        for (int i = TArray.length - 1; n < i; ++n, --i) {
            T t = TArray[n];
            TArray[n] = TArray[i];
            TArray[i] = t;
        }
        return TArray;
    }

    public static int sum(int ... nArray) {
        int n = 0;
        for (int i = 0; i < nArray.length; ++i) {
            n += nArray[i];
        }
        return n;
    }

    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> clazz) {
        ArrayList<T> arrayList = Lists.newArrayList(iterable);
        return arrayList.toArray((T[])Array.newInstance(clazz, arrayList.size()));
    }

    public static <T> T first(T[] TArray, Predicate<? super T> predicate) {
        return Arrays2.first(TArray, 0, predicate);
    }

    public static <T> T first(T[] TArray, int n, Predicate<? super T> predicate) {
        for (int i = n; i < TArray.length; ++i) {
            if (!predicate.apply(TArray[i])) continue;
            return TArray[i];
        }
        return null;
    }

    public static <T> T last(T[] TArray, Predicate<? super T> predicate) {
        return Arrays2.last(TArray, TArray.length - 1, predicate);
    }

    public static <T> T last(T[] TArray, int n, Predicate<? super T> predicate) {
        for (int i = n; i >= 0; --i) {
            if (!predicate.apply(TArray[i])) continue;
            return TArray[i];
        }
        return null;
    }

    public static <F, T> List<T> toList(F[] FArray, Class<T> clazz, Function<? super F, ? extends T> function) {
        return Arrays.asList(Arrays2.transform(FArray, clazz, function));
    }

    public static <T> T[] of(T ... TArray) {
        return TArray;
    }

    private Arrays2() {
    }
}


