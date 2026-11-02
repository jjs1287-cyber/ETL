package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import java.util.List;
import java.util.logging.Logger;

final class RetryPolicyChange implements RequestMappingsModifier {
   private final RetryPolicy newPolicy;
   private Logger logger = Logger.getAnonymousLogger();

   public RetryPolicyChange(RetryPolicy var1) {
      this.newPolicy = var1;
   }

   @Override
   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Override
   public void modifyMapList(List<String> var1) {
      RetryPolicyMapper var2 = new RetryPolicyMapper(this.newPolicy);
      var2.setLogger(this.logger);
      var2.applyRetryPolicy(var1);
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + " --> " + this.newPolicy;
   }
}
