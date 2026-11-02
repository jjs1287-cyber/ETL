package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.TestTransformationService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class TestTransformationAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.TestTransformation";
   private final TransformationPage page;

   public TestTransformationAction(TransformationPage var1) {
      super("action.ic.tx.TestTransformation", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestTransformationAction.this.saveAndStartTest();
         }
      });
   }

   private void saveAndStartTest() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            TestTransformationService var1 = TestTransformationAction.this.page.getTreeDataService().getTestTransformationService();
            TestTransformationAction.this.runTest(var1);
         }
      });
   }

   private void runTest(final TestTransformationService var1) {
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            var1.testTransformation();
         }
      });
   }
}
