package org.etlforge.integration.client.ui.connector.autodesk.function.wizard;

import org.etlforge.lang.Arrays2;

public final class AutoDeskFunctionWizardSections {
   public static final String ENDPOINT = "Endpoint";
   public static final String OBJECT = "Workspace";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Workspace");
   }

   private AutoDeskFunctionWizardSections() {
   }
}
