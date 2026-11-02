package org.etlforge.integration.client.ui.operation.runner;

import com.google.common.base.Preconditions;
import java.io.Reader;
import java.util.List;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.callback.Receiver;

public final class DefaultOperationRunner implements OperationRunner {
   private final JobControl jobControl;
   private final OperationRunnerPreConditions preConditions;

   public DefaultOperationRunner(JobControl var1, OperationRunnerPreConditions var2) {
      this.jobControl = Preconditions.checkNotNull(var1);
      this.preConditions = Preconditions.checkNotNull(var2);
   }

   @Override
   public void run(final Operation var1, final int var2, final Receiver<Long> var3) {
      Preconditions.checkNotNull(var1, "operation");
      ArgCheck.checkPositive(var2, "timesToRun");
      this.preConditions.checkAndRun(var1, new Runnable() {
         @Override
         public void run() {
            DefaultOperationRunner.this.submitOperation(var1, var2, var3);
         }
      });
   }

   private void submitOperation(Operation var1, int var2, Receiver<Long> var3) {
      SubmitOperationJob var4 = new SubmitOperationJob(this.jobControl, var1, var2, var3);
      var4.submit();
   }

   @Override
   public void runWithSourceData(final Operation var1, final Reader var2, final Receiver<Long> var3) {
      Preconditions.checkNotNull(var1, "operation");
      Preconditions.checkNotNull(var2, "sourceData");
      this.preConditions.checkAndRun(var1, new Runnable() {
         @Override
         public void run() {
            DefaultOperationRunner.this.submitOperationWithSourceData(var1, var2, var3);
         }
      });
   }

   private void submitOperationWithSourceData(Operation var1, Reader var2, Receiver<Long> var3) {
      SubmitOperationJob var4 = new SubmitOperationJob(this.jobControl, var1, var2, var3);
      var4.submit();
   }

   @Override
   public void runAll(List<Operation> var1) {
      for (Operation var3 : var1) {
         this.run(var3, 1, NO_CALLBACK);
      }
   }
}
