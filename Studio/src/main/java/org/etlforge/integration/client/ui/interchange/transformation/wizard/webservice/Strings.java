package org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.resources.TransWizResources;

final class Strings {
   static final String COMBO_LABEL = get("WebService.Label.Combo");

   public static String get(String var0) {
      return TransWizResources.getString(var0);
   }

   private Strings() {
   }
}
