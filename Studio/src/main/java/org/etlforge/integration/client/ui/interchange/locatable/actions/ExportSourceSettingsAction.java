package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.interchange.core.action.EntityPageSaveGate;
import org.etlforge.integration.client.ui.interchange.location.export.SourceSettingsExporter;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ExportSourceSettingsAction extends AbstractSingleActiveObjectAction<Source> {
   public static final String ID = "action.ic.ExportSourceSettings";
   private final ApplicationWindow appWin;
   private final ProjectPersistor projectPersistor;
   private final EntityPageSaveGate saveGate;

   public ExportSourceSettingsAction(ApplicationWindow var1, ProjectPersistor var2, EntityPageSaveGate var3) {
      super(var1.getActiveObjectService(), "action.ic.ExportSourceSettings", LocatableActionDictionary.DICTIONARY, Source.class);
      this.appWin = var1;
      this.projectPersistor = var2;
      this.saveGate = var3;
   }

   protected void runOnObject(final Source var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportSourceSettingsAction.this.startExport(var1);
         }
      });
   }

   private void startExport(final Source var1) {
      this.saveGate.runInBackgroundThread(var1, new Runnable() {
         @Override
         public void run() {
            SourceSettingsExporter var1x = new SourceSettingsExporter(ExportSourceSettingsAction.this.projectPersistor);
            var1x.setWaitService(ExportSourceSettingsAction.this.appWin);
            var1x.exportSettings(var1);
         }
      });
   }
}
