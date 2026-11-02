package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import org.etlforge.lang.Arrays2;

final class WizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String OBJECT = "Object";
   public static final String QUERY = "Query";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Object", "Query");
   }

   private WizardSections() {
   }
}
