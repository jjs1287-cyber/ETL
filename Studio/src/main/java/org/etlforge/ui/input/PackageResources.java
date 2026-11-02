package org.etlforge.ui.input;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import org.etlforge.ui.resources.UiResourceBundle;

final class PackageResources {
   static final String INVALID_VALUE = getString("InputField.Invalid");
   static final String VALUE_CANNOT_BE_EMPTY = getString("InputField.Invalid.Empty");

   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   private static String getString(String var0) {
      return UiResourceBundle.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   static final class DateInputField {
      static String getInvalidFormat(DateFormat var0) {
         return PackageResources.getFormattedMessage("DateInputField.InvalidFormat", new Object[]{var0.format(new Date())});
      }
   }

   static final class IntegerInputField {
      static String getInvalidRange(int var0, int var1) {
         return PackageResources.getFormattedMessage("IntegerInputField.Invalid.Range", new Object[]{var0, var1});
      }
   }

   static final class LongInputField {
      static String getInvalidRange(long var0, long var2) {
         return PackageResources.getFormattedMessage("LongInputField.Invalid.Range", new Object[]{var0, var2});
      }
   }
}
