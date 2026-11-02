package org.etlforge.integration.salesforce;

public final class SalesforceSessionVariables {
   public static final String SESSION_ID = "__Salesforce.SessionID";
   public static final String SERVICE_URL = "__Salesforce.ServiceURL";

   public static final String getSessionId(String var0) {
      return "__Salesforce.SessionID." + var0;
   }

   private SalesforceSessionVariables() {
   }
}
