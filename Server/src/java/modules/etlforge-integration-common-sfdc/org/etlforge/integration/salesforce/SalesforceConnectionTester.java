package org.etlforge.integration.salesforce;

public final class SalesforceConnectionTester {
   private final SalesforceConnectionFactory connectionFactory;

   public SalesforceConnectionTester(SalesforceConnectionFactory var1) {
      this.connectionFactory = var1;
   }

   public ConnectionResult testConnection(OrgIdentifier var1) {
      if (var1.isIncomplete()) {
         return ConnectionResult.error(ErrorCode.INCOMPLETE_CREDENTIALS, "Incomplete credentials.");
      }

      SalesforceConnection var2 = this.connectionFactory.newConnection(var1);

      try {
         var2.open();
         return ConnectionResult.success();
      } catch (SalesforceException var8) {
         return ConnectionResult.error(var8);
      } finally {
         var2.close();
      }
   }
}
