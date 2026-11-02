package org.etlforge.integration.client.ui.operation.runner;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationExecutionMode;
import org.etlforge.integration.data.entity.operation.RunnableStatus;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;

public final class OperationRunnerPreConditions {
   private final DeployGate deployGate;
   private final EntityPersistor operationSaver;
   private Operation operation;
   private Runnable jobToRun;
   private String textToShowIfNotRunnable;

   public OperationRunnerPreConditions(DeployGate var1, EntityPersistor var2) {
      this.deployGate = var1;
      this.operationSaver = var2;
      this.textToShowIfNotRunnable = "The selected operation can not be run stand-alone.";
   }

   public void setTextToShowIfNotRunnable(String var1) {
      this.textToShowIfNotRunnable = var1;
   }

   public void checkAndRun(Operation var1, Runnable var2) {
      this.operation = var1;
      this.jobToRun = var2;
      RunnableStatus var3 = this.checkRunnableStatus();
      if (var3.isRunnable()) {
         this.saveAndDeploy();
      } else {
         NonBlockingAlert.error(var3.getReasonNotRunnable(), this.textToShowIfNotRunnable);
      }
   }

   private RunnableStatus checkRunnableStatus() {
      OperationClientStartChecker var1 = new OperationClientStartChecker(OperationExecutionMode.RUN);
      return var1.checkRunnableStatus(this.operation);
   }

   private void saveAndDeploy() {
      try {
         this.operationSaver.save(this.operation);
         this.checkDeployStatusAndSubmit();
      } catch (InterchangeSavingException var2) {
         ErrorLog.attention(this.getClass(), null, var2);
      }
   }

   private void checkDeployStatusAndSubmit() {
      this.deployGate.setItemsToCheck(this.operation);
      this.deployGate.setJob(this.jobToRun);
      this.deployGate.start();
   }
}
