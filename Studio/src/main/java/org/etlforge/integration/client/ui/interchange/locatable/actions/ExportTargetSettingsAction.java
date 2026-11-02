package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.interchange.core.action.EntityPageSaveGate;
import org.etlforge.integration.client.ui.interchange.location.export.TargetSettingsExporter;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ExportTargetSettingsAction extends AbstractSingleActiveObjectAction<Target> {
   public static final String ID = "action.ic.ExportTargetSettings";
   private final ApplicationWindow appWin;
   private final ProjectPersistor projectPersistor;
   private final EntityPageSaveGate saveGate;

   public ExportTargetSettingsAction(ApplicationWindow var1, ProjectPersistor var2, EntityPageSaveGate var3) {
      super(var1.getActiveObjectService(), "action.ic.ExportTargetSettings", LocatableActionDictionary.DICTIONARY, Target.class);
      this.appWin = var1;
      this.projectPersistor = var2;
      this.saveGate = var3;
   }

   protected void runOnObject(final Target var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportTargetSettingsAction.this.startExport(var1);
         }
      });
   }

   private void startExport(final Target var1) {
      this.saveGate.runInBackgroundThread(var1, new Runnable() {
         @Override
         public void run() {
            TargetSettingsExporter var1x = new TargetSettingsExporter(ExportTargetSettingsAction.this.projectPersistor);
            var1x.setWaitService(ExportTargetSettingsAction.this.appWin);
            var1x.exportSettings(var1);
         }
      });
   }
}
