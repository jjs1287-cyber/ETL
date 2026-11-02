package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.integration.data.IntegrationData;

public abstract class ExportItemPreparer {
   public abstract void prepareItem(PostExportManager var1);

   protected final void blankOutPassword(IntegrationData var1, String var2, PostExportManager var3) {
      if (var3.blankOutPasswords()) {
         this.blankOutProperty(var1, var2, var3);
      }
   }

   private void blankOutProperty(IntegrationData var1, String var2, PostExportManager var3) {
      String var4 = var1.getProperty(var2);
      if (var4 != null) {
         var1.setProperty(var2, null);
         var3.addJob(new RestorePropertyJob(var1, var2, var4));
      }
   }
}
