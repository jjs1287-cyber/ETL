package org.etlforge.integration.client.ui.connector.netsuite.function;

import org.etlforge.integration.client.ui.connector.RefreshFunctionJob;
import org.etlforge.integration.client.ui.connector.netsuite.search.NetSuiteQueryHelper;

public class RefreshNetSuiteFunctionJob extends RefreshFunctionJob {
   private final NetSuiteQueryHelper helper;

   public RefreshNetSuiteFunctionJob(RefreshFunctionJob var1) {
      this(var1, null);
   }

   public RefreshNetSuiteFunctionJob(RefreshFunctionJob var1, NetSuiteQueryHelper var2) {
      super(var1);
      this.helper = var2;
   }

   @Override
   public Void call() throws Exception {
      if (this.helper != null) {
         this.helper.refreshQueryXml();
         this.save();
      }

      return super.call();
   }
}
