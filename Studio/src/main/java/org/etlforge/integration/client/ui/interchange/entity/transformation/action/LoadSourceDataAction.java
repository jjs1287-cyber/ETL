package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.system.callback.CallbackResultAdapter;

public final class LoadSourceDataAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.LoadSource";
   private final TransformationPage page;

   public LoadSourceDataAction(TransformationPage var1) {
      super("action.ic.tx.LoadSource", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      boolean var2 = this.isApplicable();
      this.setEnabled(var2);
   }

   private boolean isApplicable() {
      Transformation var1 = this.page.getObject();
      DataStructure var2 = var1.getInputStructure();
      return var2.getStructureType().isSourceDataRequired();
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LoadSourceDataAction.this.saveAndRun();
         }
      });
   }

   private void saveAndRun() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            SourceDataService var1 = LoadSourceDataAction.this.page.getTreeDataService().getSourceDataService();
            LoadSourceDataAction.this.loadSource(var1);
         }
      });
   }

   private void loadSource(final SourceDataService var1) {
      ApplicationWorker var2 = Application.getWorker();
      Runnable var3 = new Runnable() {
         @Override
         public void run() {
            var1.loadSourceData(new CallbackResultAdapter() {});
         }
      };
      var2.submitForParallel(ApplicationWorker.GUARD.protect(var3));
   }
}
