package org.etlforge.util.persist;

final class KeyValidator {
   private KeyValidator() {
      throw new UnsupportedOperationException();
   }

   static String checkKeyRelaxed(String var0) {
      return var0 != null ? var0.trim() : null;
   }

   static String checkKey(String var0) throws IllegalArgumentException {
      if (var0 == null) {
         throw new IllegalArgumentException("key cannot be null");
      } else {
         String var1 = var0.trim();
         if (var1.length() == 0) {
            throw new IllegalArgumentException("key cannot be blank");
         } else if (var1.startsWith("konga.")) {
            throw new IllegalArgumentException("key cannot start with konga.");
         } else {
            return var1;
         }
      }
   }
}
