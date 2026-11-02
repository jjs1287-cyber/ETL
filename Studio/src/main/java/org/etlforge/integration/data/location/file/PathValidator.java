package org.etlforge.integration.data.location.file;

import org.etlforge.integration.dataelement.DataElementResolver;

public final class PathValidator {
   private static final char[] ILLEGAL_CHARS = new char[]{':', '~', '%', '$', ':', '?', '"', '<', '>'};
   private static final String FILESHARE = "\\\\";
   private static final String[] ILLEGAL_PATTERNS = new String[]{"..", "\\\\"};
   private String errorMessage;
   private boolean required;
   private boolean allowDriveLetter;
   private boolean allowFileshares;

   public void setRequired(boolean var1) {
      this.required = var1;
   }

   public void setAllowDriveLetter(boolean var1) {
      this.allowDriveLetter = var1;
   }

   public void setAllowFileshares(boolean var1) {
      this.allowFileshares = var1;
   }

   public boolean isValid(String var1) {
      this.errorMessage = null;
      String var2 = this.getPathToValidate(var1);
      if (var2.isEmpty()) {
         this.validateEmptyPath();
      } else {
         this.validateNonEmptyPath(var2);
      }

      return this.errorMessage == null;
   }

   private void validateEmptyPath() {
      if (this.required) {
         this.errorMessage = "The path cannot be empty.";
      }
   }

   private void validateNonEmptyPath(String var1) {
      this.lookForIllegalCharacters(var1);
      if (this.errorMessage == null) {
         this.lookForIllegalPatterns(var1);
      }
   }

   private String getPathToValidate(String var1) {
      return var1 == null ? "" : DataElementResolver.resolveDefaultValues(var1.trim());
   }

   private void lookForIllegalCharacters(String var1) {
      char[] var2 = ILLEGAL_CHARS;
      int var3 = var2.length;
      int var4 = 0;

      while (true) {
         if (var4 >= var3) {
            return;
         }

         char var5 = var2[var4];
         int var6 = var1.indexOf(var5);
         if (var6 >= 0) {
            if (var5 != ':' || !this.allowDriveLetter || var6 != 1) {
               break;
            }

            char var7 = var1.charAt(0);
            if (!Character.isLetter(var7)) {
               break;
            }

            int var8 = var1.indexOf(58, var6 + 1);
            if (var8 != -1) {
               break;
            }
         }

         var4++;
      }

      this.errorMessage = getIllegalCharacterMessage();
   }

   private void lookForIllegalPatterns(String var1) {
      for (String var5 : ILLEGAL_PATTERNS) {
         int var6 = var1.indexOf(var5);
         if (var6 == 0 && var5.equals("\\\\") && this.allowFileshares) {
            boolean var7 = this.validateStartOfFilesharePath(var1);
            if (var7) {
               var6 = var1.indexOf(var5, var6 + 1);
            }
         }

         if (var6 >= 0) {
            this.errorMessage = getIllegalPatternMessage();
            return;
         }
      }
   }

   private boolean validateStartOfFilesharePath(String var1) {
      if (var1.equals("\\\\")) {
         this.errorMessage = "Incomplete path.";
         return false;
      } else {
         char var2 = var1.charAt("\\\\".length());
         if (Character.isWhitespace(var2)) {
            this.errorMessage = "Invalid fileshare path: Whitespace not allowed immediately after \\\\";
            return false;
         } else {
            return true;
         }
      }
   }

   public String getLastError() {
      return this.errorMessage;
   }

   private static String getIllegalCharacterMessage() {
      StringBuilder var0 = new StringBuilder("The path may not contain any of the following characters:");

      for (char var4 : ILLEGAL_CHARS) {
         var0.append(" ").append(var4);
      }

      return var0.toString();
   }

   private static String getIllegalPatternMessage() {
      StringBuilder var0 = new StringBuilder("The path may not contain any of the following patterns:");

      for (String var4 : ILLEGAL_PATTERNS) {
         var0.append(" '").append(var4).append("'");
      }

      return var0.toString();
   }
}
