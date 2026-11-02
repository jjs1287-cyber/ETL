package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

final class WizardSections {
   public static final String WSDL = "WSDL";
   public static final String OPERATION = "Operation";
   public static final String DETAILS = "Details";

   public static String[] all() {
      return new String[]{"WSDL", "Operation", "Details"};
   }

   private WizardSections() {
   }
}
