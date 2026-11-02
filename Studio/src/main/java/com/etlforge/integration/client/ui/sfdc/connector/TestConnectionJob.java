package com.etlforge.integration.client.ui.sfdc.connector;

import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnectionTester;

public final class TestConnectionJob extends ResultProducingUiJob<ConnectionResult> {
   private final SalesforceConnectionTester connectionTester;
   private final OrgIdentifier orgId;

   public TestConnectionJob(SalesforceConnectionTester var1, OrgIdentifier var2) {
      super("Test Salesforce Connection");
      this.connectionTester = var1;
      this.orgId = var2;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setStartMessage();
      ConnectionResult var1 = this.connectionTester.testConnection(this.orgId);
      this.resultWasComputed(var1);
   }

   private void setStartMessage() {
      String var1 = "Testing the credentials for " + this.orgId.getUserName();
      this.setMessage(var1);
   }
}
