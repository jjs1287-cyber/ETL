package org.etlforge.integration.client.project.export;

import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.callback.Receiver;

public final class ExportJitterpackDataPreparer implements Receiver<IntegrationEntity> {
   private final PostExportManager mgr;

   public ExportJitterpackDataPreparer(PostExportManager var1) {
      this.mgr = var1;
   }

   public void handle(IntegrationEntity var1) {
      ExportItemPreparer var2 = var1.getExtensionObject(ExportItemPreparer.class);
      if (var2 != null) {
         var2.prepareItem(this.mgr);
      }
   }
}
