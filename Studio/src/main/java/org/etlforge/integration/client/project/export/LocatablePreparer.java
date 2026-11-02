package org.etlforge.integration.client.project.export;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.jitterpack.ExportItemPreparer;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;

public final class LocatablePreparer extends ExportItemPreparer {
   private final Locatable locatable;

   @ExtensionObjectConstructor
   public LocatablePreparer(Locatable var1) {
      this.locatable = var1;
   }

   @Override
   public void prepareItem(PostExportManager var1) {
      if (!(this.locatable instanceof BuiltInIntegrationEntity)) {
         DataLocation var2 = this.locatable.getLocation();
         if (var2 != null) {
            DataLocationPreparer var3 = new DataLocationPreparer(var2);
            var3.prepareItem(var1);
         }
      }
   }
}
