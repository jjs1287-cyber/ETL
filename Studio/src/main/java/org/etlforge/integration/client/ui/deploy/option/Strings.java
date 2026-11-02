package org.etlforge.integration.client.ui.deploy.option;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.deploy.resources.DeployResources;

final class Strings {
   static String get(String var0) {
      return DeployResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
