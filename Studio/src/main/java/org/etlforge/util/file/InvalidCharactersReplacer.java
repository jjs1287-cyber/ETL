package org.etlforge.util.file;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public final class InvalidCharactersReplacer {
   private final ImmutableSet<Character> invalidChars = buildInvalidCharLookup();
   private final String replacement;

   public InvalidCharactersReplacer(String var1) {
      for (char var5 : var1.toCharArray()) {
         if (this.invalidChars.contains(var5)) {
            throw new IllegalArgumentException("replacement may not contain invalid characters");
         }
      }

      this.replacement = var1;
   }

   private static ImmutableSet<Character> buildInvalidCharLookup() {
      Builder var0 = ImmutableSet.builder();

      for (char var4 : WindowsFileNameValidator.illegalChars()) {
         var0.add(var4);
      }

      return var0.build();
   }

   public String replace(String var1) {
      StringBuilder var2 = new StringBuilder(var1.length());

      for (char var6 : var1.toCharArray()) {
         if (this.invalidChars.contains(var6)) {
            var2.append(this.replacement);
         } else {
            var2.append(var6);
         }
      }

      return var2.toString();
   }
}
