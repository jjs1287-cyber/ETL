package org.etlforge.integration.client.project.export;

import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;

final class DataLocationPreparer extends ExportItemPreparer {
   private final DataLocation loc;
   private PostExportManager manager;

   public DataLocationPreparer(DataLocation var1) {
      this.loc = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      this.manager = var1;
      if (this.loc instanceof LoginProtectedResource) {
         String var2 = ((LoginProtectedResource)this.loc).getPasswordPropertyName();
         this.blankOutPassword(var2);
      }
   }

   private void blankOutPassword(String var1) {
      this.blankOutPassword(this.loc, var1, this.manager);
   }
}
