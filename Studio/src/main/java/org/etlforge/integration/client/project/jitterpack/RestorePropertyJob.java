package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.integration.data.IntegrationData;

final class RestorePropertyJob implements PostExportJob {
   private final IntegrationData instance;
   private final String propertyName;
   private final Object value;

   RestorePropertyJob(IntegrationData var1, String var2, String var3) {
      this.instance = var1;
      this.propertyName = var2;
      this.value = var3;
   }

   @Override
   public void doJob() {
      this.instance.setProperty(this.propertyName, this.value);
   }

   @Override
   public PostExportJob.Condition getCondition() {
      return PostExportJob.Condition.ALWAYS;
   }
}
