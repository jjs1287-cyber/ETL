  
                             
   
                                        
                                        
   
package org.etlforge.system;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;

public final class ArgCheck {
    public static int checkInRange(int n, int n2, int n3) {
        Preconditions.checkArgument((n >= n2 && n <= n3 ? 1 : 0) != 0);
        return n;
    }

    public static int checkInRange(int n, int n2, int n3, String string) {
        Preconditions.checkArgument((n >= n2 && n <= n3 ? 1 : 0) != 0, (Object)(string + " must be in the range [" + n2 + ", " + n3 + "] (was " + n + ")"));
        return n;
    }

    public static long checkInRange(long l, long l2, long l3) {
        Preconditions.checkArgument((l >= l2 && l <= l3 ? 1 : 0) != 0);
        return l;
    }

    public static long checkInRange(long l, long l2, long l3, String string) {
        Preconditions.checkArgument((l >= l2 && l <= l3 ? 1 : 0) != 0, (Object)(string + " must be in the range [" + l2 + ", " + l3 + "] (was " + l + ")"));
        return l;
    }

    public static int checkGreaterThan(int n, int n2) {
        Preconditions.checkArgument((n > n2 ? 1 : 0) != 0);
        return n;
    }

    public static int checkGreaterThan(int n, int n2, String string) {
        Preconditions.checkArgument((n > n2 ? 1 : 0) != 0, (Object)(string + " must be >= 0 (was " + n + ")"));
        return n;
    }

    public static int checkPositive(int n) {
        return ArgCheck.checkGreaterThan(n, 0);
    }

    public static int checkPositive(int n, String string) {
        return ArgCheck.checkGreaterThan(n, 0, string);
    }

    public static int checkNotNegative(int n) {
        Preconditions.checkArgument((n >= 0 ? 1 : 0) != 0);
        return n;
    }

    public static int checkNotNegative(int n, String string) {
        Preconditions.checkArgument((n >= 0 ? 1 : 0) != 0, (Object)(string + " must be >= 0 (was " + n + ")"));
        return n;
    }

    public static long checkPositive(long l) {
        Preconditions.checkArgument((l > 0L ? 1 : 0) != 0);
        return l;
    }

    public static long checkPositive(long l, String string) {
        Preconditions.checkArgument((l > 0L ? 1 : 0) != 0, (Object)(string + " must be >= 0 (was " + l + ")"));
        return l;
    }

    public static long checkNotNegative(long l) {
        Preconditions.checkArgument((l >= 0L ? 1 : 0) != 0);
        return l;
    }

    public static long checkNotNegative(long l, String string) {
        Preconditions.checkArgument((l >= 0L ? 1 : 0) != 0, (Object)(string + " must be >= 0 (was " + l + ")"));
        return l;
    }

    public static String checkNotEmpty(String string) {
        Preconditions.checkArgument((boolean)ArgCheck.isNotEmpty(string));
        return string;
    }

    public static String checkNotEmpty(String string, String string2) {
        Preconditions.checkArgument((boolean)ArgCheck.isNotEmpty(string), (Object)(string2 + " cannot be null or empty"));
        return string;
    }

    private static boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static String checkNotBlank(String string) {
        Preconditions.checkArgument((boolean)ArgCheck.isNotBlank(string));
        return string;
    }

    public static String checkNotBlank(String string, String string2) {
        Preconditions.checkArgument((boolean)ArgCheck.isNotBlank(string), (Object)(string2 + " cannot be null or blank"));
        return string;
    }

    private static boolean isNotBlank(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public static <T> T[] checkNotEmpty(T[] TArray) {
        Preconditions.checkArgument((TArray.length > 0 ? 1 : 0) != 0);
        return TArray;
    }

    public static <T> T[] checkNotEmpty(T[] TArray, String string) {
        Preconditions.checkArgument((TArray.length > 0 ? 1 : 0) != 0, (Object)(string + " cannot be empty"));
        return TArray;
    }

    public static <T, C extends Collection<T>> C checkNotEmpty(C c) {
        Preconditions.checkArgument((c.size() > 0 ? 1 : 0) != 0);
        return c;
    }

    public static <T, C extends Collection<T>> C checkNotEmpty(C c, String string) {
        Preconditions.checkArgument((c.size() > 0 ? 1 : 0) != 0, (Object)(string + " cannot be empty"));
        return c;
    }

    public static <T> List<T> checkNotEmpty(List<T> list) {
        Preconditions.checkArgument((list.size() > 0 ? 1 : 0) != 0);
        return list;
    }

    public static <T, L extends List<T>> L checkNotEmpty(L l, String string) {
        Preconditions.checkArgument((l.size() > 0 ? 1 : 0) != 0, (Object)(string + " cannot be empty"));
        return l;
    }

    public static <T> T checkInstance(Object object, Class<T> clazz) {
        Preconditions.checkArgument((boolean)clazz.isInstance(object), (Object)("Only objects of type " + clazz.getSimpleName() + " accepted."));
        return clazz.cast(object);
    }

    private ArgCheck() {
    }
}

