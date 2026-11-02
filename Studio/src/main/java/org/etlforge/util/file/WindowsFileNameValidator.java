package org.etlforge.util.file;

import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;
import org.etlforge.io.FileUtils;
import org.etlforge.util.name.IllegalNameException;

public final class WindowsFileNameValidator implements FileNameValidator {
   private static final String[] RESERVED_WORDS = new String[]{
      "CON",
      "PRN",
      "AUX",
      "NUL",
      "COM1",
      "COM2",
      "COM3",
      "COM4",
      "COM5",
      "COM6",
      "COM7",
      "COM8",
      "COM9",
      "LPT1",
      "LPT2",
      "LPT3",
      "LPT4",
      "LPT5",
      "LPT6",
      "LPT7",
      "LPT8",
      "LPT9"
   };
   private String illegalCharsPattern = "The filename may not contain any of the following characters: {0}";
   private String reservedNameMessage = getReservedNameMessage();
   private int maxLength = -1;

   public static final char[] illegalChars() {
      return FileUtils.illegalChars();
   }

   public static final String[] reservedWords() {
      return (String[])RESERVED_WORDS.clone();
   }

   public boolean isValid(String var1) {
      if (StringUtils.isBlank(var1)) {
         return false;
      } else if (this.isTooLong(var1)) {
         return false;
      } else {
         return this.hasIllegalChars(var1) ? false : !this.isReservedWord(var1);
      }
   }

   private boolean isTooLong(String var1) {
      return this.maxLength > 0 && var1.length() > this.maxLength;
   }

   private boolean hasIllegalChars(String var1) {
      for (char var5 : illegalChars()) {
         if (var1.indexOf(var5) != -1) {
            return true;
         }
      }

      return false;
   }

   private boolean isReservedWord(String var1) {
      String var2 = StringUtils.substringBefore(var1, ".");

      for (String var6 : RESERVED_WORDS) {
         if (var6.equalsIgnoreCase(var2)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void validate(String var1) throws IllegalNameException {
      if (StringUtils.isBlank(var1)) {
         throw new IllegalNameException("The name cannot be empty");
      }

      this.checkLength(var1);
      this.checkIllegalChars(var1);
      this.checkReservedNames(var1);
   }

   private void checkLength(String var1) throws IllegalNameException {
      if (this.isTooLong(var1)) {
         throw new IllegalNameException("The name is too long. The name can only be " + this.maxLength + " characters long.");
      }
   }

   private void checkIllegalChars(String var1) throws IllegalNameException {
      if (this.hasIllegalChars(var1)) {
         String var2 = MessageFormat.format(this.illegalCharsPattern, getIllegalCharsString());
         throw new IllegalNameException(var2);
      }
   }

   private void checkReservedNames(String var1) throws IllegalNameException {
      if (this.isReservedWord(var1)) {
         throw new IllegalNameException(this.reservedNameMessage);
      }
   }

   public void setMaxLength(int var1) {
      this.maxLength = var1;
   }

   public void setIllegalCharactersMessagePattern(String var1) {
      this.illegalCharsPattern = var1;
   }

   public void setReservedNameMessage(String var1) {
      this.reservedNameMessage = var1;
   }

   private static String getReservedNameMessage() {
      return "The name is a reserved word on Windows";
   }

   private static String getIllegalCharsString() {
      StringBuilder var0 = new StringBuilder();

      for (char var4 : illegalChars()) {
         var0.append(" ").append(var4);
      }

      return var0.toString();
   }
}
