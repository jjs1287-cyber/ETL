package org.etlforge.integration.client.ui.admin.core.action;

import java.io.Reader;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.client.ui.operation.runner.AbstractRunOperationAction;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.client.ui.operation.runner.SubmitOperationJob;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.callback.Receiver;

public final class OperationDoNowAction extends AbstractRunOperationAction {
   private static final String ID = "admin.action.RunOperation";

   public OperationDoNowAction(ApplicationWindow var1, JobControl var2) {
      super("admin.action.RunOperation", lookupName(), var1, new OperationDoNowAction.OperationRunnerFactoryImpl(var2));
      this.putValue("ShortDescription", AdminActionDictionary.DICTIONARY.getToolTip("admin.action.RunOperation"));
      this.setSwitchToEditor(false);
   }

   private static String lookupName() {
      return AdminActionDictionary.DICTIONARY.getName("admin.action.RunOperation").getRaw();
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof Operation;
   }

   @Override
   protected Operation getOperationToRun() {
      List var1 = this.getActiveObjects(Operation.class);
      return var1.size() == 1 ? (Operation)var1.get(0) : null;
   }

   @Override
   protected Editor getPageForObjectToRun() {
      return null;
   }

   private static class OperationRunnerFactoryImpl implements OperationRunnerFactory {
      private final JobControl jobControl;

      public OperationRunnerFactoryImpl(JobControl var1) {
         this.jobControl = var1;
      }

      @Override
      public OperationRunner newRunner() {
         return new OperationRunner() {
            @Override
            public void run(Operation var1, int var2, Receiver<Long> var3) {
               SubmitOperationJob var4 = new SubmitOperationJob(OperationRunnerFactoryImpl.this.jobControl, var1, 1, var3);
               var4.submit();
            }

            @Override
            public void runWithSourceData(Operation var1, Reader var2, Receiver<Long> var3) {
               throw new UnsupportedOperationException();
            }

            @Override
            public void runAll(List<Operation> var1) {
               throw new UnsupportedOperationException();
            }
         };
      }
   }
}
