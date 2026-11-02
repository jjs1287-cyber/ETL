package org.etlforge.integration.client.ui.connector.sap.function.wizard;

import org.etlforge.lang.Arrays2;

final class WizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String TYPE = "Type";
   public static final String CATEGORY = "Category";
   public static final String FUNCTION = "Function";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Type", "Category", "Function");
   }

   private WizardSections() {
   }
}
