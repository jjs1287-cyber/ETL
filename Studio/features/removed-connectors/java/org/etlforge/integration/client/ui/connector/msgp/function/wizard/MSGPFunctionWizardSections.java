package org.etlforge.integration.client.ui.connector.msgp.function.wizard;

import org.etlforge.lang.Arrays2;

public final class MSGPFunctionWizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String OBJECT = "Module";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Module");
   }

   private MSGPFunctionWizardSections() {
   }
}
