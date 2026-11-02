package org.etlforge.util.name;

import org.apache.commons.lang.StringUtils;

public class IllegalNameException extends RuntimeException {
   private String message;
   private final String attemptedName;
   private final String illegalCharacters;

   public IllegalNameException(String var1) {
      this(var1, null, null);
   }

   public IllegalNameException(String var1, String var2) {
      this(var1, var2, null);
   }

   public IllegalNameException(String var1, String var2, String var3) {
      if (var1 != null) {
         this.message = var1.trim();
      }

      this.attemptedName = var2;
      this.illegalCharacters = var3;
   }

   public String getAttemptedName() {
      return this.attemptedName;
   }

   public String getIllegalCharacters() {
      return this.illegalCharacters;
   }

   @Override
   public String getMessage() {
      boolean var1 = StringUtils.isNotBlank(this.message);
      boolean var2 = StringUtils.isNotBlank(this.attemptedName);
      boolean var3 = StringUtils.isNotBlank(this.illegalCharacters);
      StringBuffer var4 = new StringBuffer();
      if (var1) {
         var4.append(this.message);
      }

      if (var2) {
         if (var1 && !this.message.endsWith(".")) {
            var4.append(". ");
         }

         var4.append("Illegal name: ");
         var4.append(this.attemptedName);
         if (var3) {
            var4.append(". The name cannot contain any of the following characters: ");
            var4.append(this.illegalCharacters);
         }
      }

      return var4.toString();
   }

   @Override
   public String toString() {
      return this.getMessage();
   }

   public String getFormattedMessage() {
      boolean var1 = !StringUtils.isBlank(this.message);
      boolean var2 = !StringUtils.isBlank(this.attemptedName);
      boolean var3 = !StringUtils.isBlank(this.illegalCharacters);
      StringBuffer var4 = new StringBuffer();
      if (var1) {
         var4.append(this.message);
      }

      if (var2) {
         if (var1) {
            var4.append("\n");
         }

         var4.append("Illegal name: ");
         var4.append(this.attemptedName);
         if (var3) {
            var4.append("\nThe name cannot contain any of the following characters:\n");
            var4.append(this.illegalCharacters);
         }
      }

      return var4.toString();
   }
}
