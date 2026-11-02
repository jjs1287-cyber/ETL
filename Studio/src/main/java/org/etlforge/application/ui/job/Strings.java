package org.etlforge.application.ui.job;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ApplicationResources;

final class Strings {
   static String get(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
