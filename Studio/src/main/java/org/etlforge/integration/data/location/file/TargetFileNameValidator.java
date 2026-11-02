package org.etlforge.integration.data.location.file;

import org.apache.commons.lang.StringUtils;
import org.etlforge.util.file.FileNameValidator;
import org.etlforge.util.name.IllegalNameException;

public final class TargetFileNameValidator implements FileNameValidator {
   private boolean allowFolderSeparator;
   private TargetFileNameValidator.IllegalCharacterSet illegalCharacters = getIllegalCharacterSet(false);

   @Override
   public void validate(String var1) throws IllegalNameException {
      if (var1 == null || var1.length() == 0) {
         throw new IllegalNameException("The name cannot be empty.");
      }

      if (this.containsIllegalCharacter(var1)) {
         throw new IllegalNameException(null, var1, this.illegalCharacters.toString());
      }

      if (this.endsWithSeparator(var1)) {
         throw new IllegalNameException("The file name may not end with the character '" + var1.charAt(var1.length() - 1) + "'.", var1);
      }
   }

   public void setAllowFolderSeparator(boolean var1) {
      if (var1 != this.allowFolderSeparator) {
         this.allowFolderSeparator = var1;
         this.illegalCharacters = getIllegalCharacterSet(var1);
      }
   }

   public boolean containsIllegalCharacter(String var1) {
      return this.illegalCharacters.isPartOf(var1);
   }

   public boolean endsWithSeparator(String var1) {
      return var1.endsWith("/") || var1.endsWith("\\");
   }

   public boolean endsWithSeparatorAndDots(String var1) {
      String var2 = StringUtils.substringAfterLast(var1, "/");
      if (this.hasOnlyDots(var2)) {
         return true;
      }

      var2 = StringUtils.substringAfterLast(var1, "\\");
      return this.hasOnlyDots(var2);
   }

   public boolean hasOnlyDots(String var1) {
      if (var1.isEmpty()) {
         return false;
      }

      for (char var5 : var1.toCharArray()) {
         if (var5 != '.') {
            return false;
         }
      }

      return true;
   }

   private static TargetFileNameValidator.IllegalCharacterSet getIllegalCharacterSet(boolean var0) {
      char[] var1 = var0 ? new char[]{'*', '"', '?', ',', ':', '<', '>', '|'} : new char[]{'*', '"', '?', '/', '\\', ',', ':', '<', '>', '|'};
      return new TargetFileNameValidator.IllegalCharacterSetImpl(var1);
   }

   private interface IllegalCharacterSet {
      boolean isPartOf(String var1);
   }

   private static class IllegalCharacterSetImpl implements TargetFileNameValidator.IllegalCharacterSet {
      private final char[] illegal;
      private String stringRep;

      public IllegalCharacterSetImpl(char[] var1) {
         this.illegal = var1;
      }

      @Override
      public boolean isPartOf(String var1) {
         for (char var5 : this.illegal) {
            if (var1.indexOf(var5) >= 0) {
               return true;
            }
         }

         return false;
      }

      @Override
      public String toString() {
         if (this.stringRep == null) {
            StringBuilder var1 = new StringBuilder();
            int var2 = 0;

            for (char var6 : this.illegal) {
               if (var2 > 0) {
                  var1.append(" ");
               }

               var1.append(var6);
               var2++;
            }

            this.stringRep = var1.toString();
         }

         return this.stringRep;
      }
   }
}
