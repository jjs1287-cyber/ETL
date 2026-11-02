package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.export.MappingImporter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;

public final class ImportMultipleFoldersAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.ImportMultipleFolders";
   private final TransformationPage page;

   public ImportMultipleFoldersAction(TransformationPage var1) {
      super("action.ic.tx.ImportMultipleFolders", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImportMultipleFoldersAction.this.startImport();
         }
      });
   }

   private void startImport() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            Transformation var1 = ImportMultipleFoldersAction.this.page.getObject();
            MappingImporter var2 = MappingImporter.Factory.create(ImportMultipleFoldersAction.this.page);
            var2.importMultipleFolders(var1);
         }
      });
   }
}
