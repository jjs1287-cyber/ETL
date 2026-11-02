package org.etlforge.integration.client.ui.connector.ebs.function.wizard;

import org.etlforge.lang.Arrays2;

final class WizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String CATALOG = "Catalog";
   public static final String PROGRAM = "Program";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Catalog", "Program");
   }

   private WizardSections() {
   }
}
