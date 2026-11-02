package org.etlforge.application;

public final class NonInteractiveMode {
   private static final boolean VALUE = Boolean.getBoolean("etlforge.noninteractivemode");

   public static boolean on() {
      return VALUE;
   }

   public static boolean off() {
      return !VALUE;
   }

   private NonInteractiveMode() {
   }
}
