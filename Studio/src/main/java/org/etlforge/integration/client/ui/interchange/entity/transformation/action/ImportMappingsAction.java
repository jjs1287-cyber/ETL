package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.export.MappingImporter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;

public final class ImportMappingsAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.ImportMappings";
   private final TransformationPage page;

   public ImportMappingsAction(TransformationPage var1) {
      super("action.ic.tx.ImportMappings", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImportMappingsAction.this.startExport();
         }
      });
   }

   private void startExport() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            Transformation var1 = ImportMappingsAction.this.page.getObject();
            MappingImporter var2 = MappingImporter.Factory.create(ImportMappingsAction.this.page);
            var2.importMappings(var1);
         }
      });
   }
}
