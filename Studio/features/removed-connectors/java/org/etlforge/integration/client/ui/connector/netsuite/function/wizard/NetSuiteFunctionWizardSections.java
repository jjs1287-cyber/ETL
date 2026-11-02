package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import org.etlforge.lang.Arrays2;

public final class NetSuiteFunctionWizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String OBJECT = "Object";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Object");
   }

   public static String[] endpointOnly() {
      return Arrays2.of("Endpoint");
   }

   private NetSuiteFunctionWizardSections() {
   }
}
