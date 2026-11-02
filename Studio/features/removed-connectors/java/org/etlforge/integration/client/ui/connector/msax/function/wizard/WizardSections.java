package org.etlforge.integration.client.ui.connector.msax.function.wizard;

import org.etlforge.lang.Arrays2;

final class WizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String WSDLURL = "WSDL URL";
   public static final String OPERATION = "Operation";

   public static String[] all() {
      return Arrays2.of("Endpoint", "WSDL URL", "Operation");
   }

   private WizardSections() {
   }
}
