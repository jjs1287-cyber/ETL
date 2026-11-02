package org.etlforge.system;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;

public final class ArgCheck {
   public static int checkInRange(int var0, int var1, int var2) {
      Preconditions.checkArgument(var0 >= var1 && var0 <= var2);
      return var0;
   }

   public static int checkInRange(int var0, int var1, int var2, String var3) {
      Preconditions.checkArgument(var0 >= var1 && var0 <= var2, var3 + " must be in the range [" + var1 + ", " + var2 + "] (was " + var0 + ")");
      return var0;
   }

   public static long checkInRange(long var0, long var2, long var4) {
      Preconditions.checkArgument(var0 >= var2 && var0 <= var4);
      return var0;
   }

   public static long checkInRange(long var0, long var2, long var4, String var6) {
      Preconditions.checkArgument(var0 >= var2 && var0 <= var4, var6 + " must be in the range [" + var2 + ", " + var4 + "] (was " + var0 + ")");
      return var0;
   }

   public static int checkGreaterThan(int var0, int var1) {
      Preconditions.checkArgument(var0 > var1);
      return var0;
   }

   public static int checkGreaterThan(int var0, int var1, String var2) {
      Preconditions.checkArgument(var0 > var1, var2 + " must be >= 0 (was " + var0 + ")");
      return var0;
   }

   public static int checkPositive(int var0) {
      return checkGreaterThan(var0, 0);
   }

   public static int checkPositive(int var0, String var1) {
      return checkGreaterThan(var0, 0, var1);
   }

   public static int checkNotNegative(int var0) {
      Preconditions.checkArgument(var0 >= 0);
      return var0;
   }

   public static int checkNotNegative(int var0, String var1) {
      Preconditions.checkArgument(var0 >= 0, var1 + " must be >= 0 (was " + var0 + ")");
      return var0;
   }

   public static long checkPositive(long var0) {
      Preconditions.checkArgument(var0 > 0L);
      return var0;
   }

   public static long checkPositive(long var0, String var2) {
      Preconditions.checkArgument(var0 > 0L, var2 + " must be >= 0 (was " + var0 + ")");
      return var0;
   }

   public static long checkNotNegative(long var0) {
      Preconditions.checkArgument(var0 >= 0L);
      return var0;
   }

   public static long checkNotNegative(long var0, String var2) {
      Preconditions.checkArgument(var0 >= 0L, var2 + " must be >= 0 (was " + var0 + ")");
      return var0;
   }

   public static String checkNotEmpty(String var0) {
      Preconditions.checkArgument(isNotEmpty(var0));
      return var0;
   }

   public static String checkNotEmpty(String var0, String var1) {
      Preconditions.checkArgument(isNotEmpty(var0), var1 + " cannot be null or empty");
      return var0;
   }

   private static boolean isNotEmpty(String var0) {
      return var0 != null && !var0.isEmpty();
   }

   public static String checkNotBlank(String var0) {
      Preconditions.checkArgument(isNotBlank(var0));
      return var0;
   }

   public static String checkNotBlank(String var0, String var1) {
      Preconditions.checkArgument(isNotBlank(var0), var1 + " cannot be null or blank");
      return var0;
   }

   private static boolean isNotBlank(String var0) {
      return var0 != null && !var0.trim().isEmpty();
   }

   public static <T> T[] checkNotEmpty(T[] var0) {
      Preconditions.checkArgument(var0.length > 0);
      return (T[])var0;
   }

   public static <T> T[] checkNotEmpty(T[] var0, String var1) {
      Preconditions.checkArgument(var0.length > 0, var1 + " cannot be empty");
      return (T[])var0;
   }

   public static <T, C extends Collection<T>> C checkNotEmpty(C var0) {
      Preconditions.checkArgument(var0.size() > 0);
      return (C)var0;
   }

   public static <T, C extends Collection<T>> C checkNotEmpty(C var0, String var1) {
      Preconditions.checkArgument(var0.size() > 0, var1 + " cannot be empty");
      return (C)var0;
   }

   public static <T> List<T> checkNotEmpty(List<T> var0) {
      Preconditions.checkArgument(var0.size() > 0);
      return var0;
   }

   public static <T, L extends List<T>> L checkNotEmpty(L var0, String var1) {
      Preconditions.checkArgument(var0.size() > 0, var1 + " cannot be empty");
      return (L)var0;
   }

   public static <T> T checkInstance(Object var0, Class<T> var1) {
      Preconditions.checkArgument(var1.isInstance(var0), "Only objects of type " + var1.getSimpleName() + " accepted.");
      return (T)var1.cast(var0);
   }

   private ArgCheck() {
   }
}
