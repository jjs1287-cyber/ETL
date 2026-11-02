package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.bulkload.TargetFields;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class TargetFieldUpdateWorker {
   private final HeaderMappingsModel model;
   private final TargetFieldsUpdater updater;
   private final WaitService waitService;

   public TargetFieldUpdateWorker(HeaderMappingsModel var1, TargetFieldsUpdater var2, WaitService var3) {
      this.model = var1;
      this.updater = var2;
      this.waitService = var3;
   }

   public void execute() {
      Computation var1 = this.createJob();
      var1.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).withWaitService(this.waitService).buildAndExecute();
   }

   private Computation<TargetFields> createJob() {
      Callable var1 = new Callable<TargetFields>() {
         public TargetFields call() throws Exception {
            return TargetFieldUpdateWorker.this.updater.getTargetFields();
         }
      };
      Receiver var2 = new Receiver<TargetFields>() {
         public void handle(TargetFields var1) {
            TargetFieldUpdateWorker.this.model.updateAndRetainMappings(TargetFieldUpdateWorker.this.model.getSourceHeader(), var1);
         }
      };
      return Computation.create(var1, var2);
   }
}
