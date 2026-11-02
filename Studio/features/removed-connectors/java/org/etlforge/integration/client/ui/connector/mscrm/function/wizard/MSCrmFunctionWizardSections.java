package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import org.etlforge.lang.Arrays2;

public final class MSCrmFunctionWizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String OBJECT = "Object";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Object");
   }

   private MSCrmFunctionWizardSections() {
   }
}
