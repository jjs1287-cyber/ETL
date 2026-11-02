package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.export.MappingExporter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;

public final class ExportMultipleFoldersAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.ExportMultipleFolders";
   private final TransformationPage page;

   public ExportMultipleFoldersAction(TransformationPage var1) {
      super("action.ic.tx.ExportMultipleFolders", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportMultipleFoldersAction.this.startExport();
         }
      });
   }

   private void startExport() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            Transformation var1 = ExportMultipleFoldersAction.this.page.getObject();
            MappingExporter var2 = MappingExporter.Factory.create(ExportMultipleFoldersAction.this.page);
            var2.exportMultipleFolders(var1);
         }
      });
   }
}
