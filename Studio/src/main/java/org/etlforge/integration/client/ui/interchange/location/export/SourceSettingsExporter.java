package org.etlforge.integration.client.ui.interchange.location.export;

import java.text.MessageFormat;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.entity.source.resources.SourceResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.ui.wait.WaitService;

public final class SourceSettingsExporter {
   private final SourceTargetExportHelper helper;

   public SourceSettingsExporter(ProjectPersistor var1) {
      this.helper = new SourceTargetExportHelper(var1);
   }

   public void setWaitService(WaitService var1) {
      this.helper.setWaitService(var1);
   }

   public void exportSettings(Source var1) {
      if (this.checkValidSource(var1)) {
         List var2 = this.helper.getAvailableExportDestinations(var1);
         if (var2.isEmpty()) {
            this.handleNoAvailableDestinations(var1);
         } else {
            this.doExport(var1, var2);
         }
      }
   }

   private boolean checkValidSource(Source var1) {
      boolean var2 = var1.getLocation() != null;
      if (!var2) {
         Alert.error(SourceResources.getString("Source.TypeMissing"), ApplicationResources.getString("Error"));
      }

      return var2;
   }

   private void handleNoAvailableDestinations(Source var1) {
      Alert.info(getNoDestinationMessage(var1), SourceResources.getString("Source.Export.NoDestinations.Title"));
   }

   private static String getNoDestinationMessage(Source var0) {
      DataLocationType var1 = var0.getLocation().getDataLocationType();
      String var2 = SourceResources.getString("Source.Export.NoDestinations.Message");
      return MessageFormat.format(var2, var1);
   }

   private void doExport(final Source var1, final List<? extends IntegrationEntity> var2) {
      this.helper.runWhileWaiting(new Runnable() {
         @Override
         public void run() {
            DataLocation var1x = var1.getLocation();
            SourceSettingsExporter.this.helper.doExport(var1x, var2);
         }
      });
   }
}
