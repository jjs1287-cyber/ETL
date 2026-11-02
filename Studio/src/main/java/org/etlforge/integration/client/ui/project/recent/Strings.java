package org.etlforge.integration.client.ui.project.recent;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;

final class Strings {
   static String get(String var0) {
      return ProjectUiResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
