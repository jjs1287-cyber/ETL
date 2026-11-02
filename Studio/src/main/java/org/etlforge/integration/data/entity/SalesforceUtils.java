package org.etlforge.integration.data.entity;

public final class SalesforceUtils {
   public static final String SALESFORCE_WIZARD_OBJECT = "salesforce_wizard_object";

   public static boolean isSalesforceWizardObject(IntegrationEntity var0) {
      return var0.getBooleanProperty("salesforce_wizard_object", false);
   }

   public static void setSalesforceWizardObject(IntegrationEntity var0, boolean var1) {
      var0.setBooleanProperty("salesforce_wizard_object", var1);
   }

   private SalesforceUtils() {
   }
}
