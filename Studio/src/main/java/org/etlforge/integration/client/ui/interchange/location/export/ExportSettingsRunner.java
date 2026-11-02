package org.etlforge.integration.client.ui.interchange.location.export;

import java.util.List;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;

final class ExportSettingsRunner {
   private ExportableProperty[] propertiesToExport;
   private List<IntegrationEntity> destinations;

   public ExportSettingsRunner(ExportableProperty[] var1, List<IntegrationEntity> var2) {
      this.propertiesToExport = var1;
      this.destinations = var2;
   }

   public void doExport(ProjectPersistor var1) throws InterchangeSavingException {
      for (IntegrationEntity var3 : this.destinations) {
         if (this.exportTo(var3)) {
            var1.save(var3);
         }
      }
   }

   private boolean exportTo(IntegrationEntity var1) {
      return var1 instanceof Locatable ? this.exportToSource((Locatable)var1) : false;
   }

   private boolean exportToSource(Locatable var1) {
      return this.exportToLocation(var1.getLocation());
   }

   private boolean exportToLocation(DataLocation var1) {
      boolean var2 = false;

      for (ExportableProperty var6 : this.propertiesToExport) {
         if (var6.exportTo(var1)) {
            var2 = true;
         }
      }

      return var2;
   }
}
