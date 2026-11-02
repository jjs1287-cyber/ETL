package org.etlforge.integration.client.ui.interchange;

import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.client.ui.operation.runner.DefaultOperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerPreConditions;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class InterchangeOperationRunnerFactory implements OperationRunnerFactory {
   private final JobControl jobControl;
   private final DeployFacade deployFacade;
   private final EntityPersistor operationSaver;

   public InterchangeOperationRunnerFactory(JobControl var1, DeployFacade var2, EntityPersistor var3) {
      this.jobControl = var1;
      this.deployFacade = var2;
      this.operationSaver = var3;
   }

   @Override
   public OperationRunner newRunner() {
      DeployGate var1 = this.deployFacade.newDeployGate();
      OperationRunnerPreConditions var2 = new OperationRunnerPreConditions(var1, this.operationSaver);
      var2.setTextToShowIfNotRunnable("Not allowed");
      return new DefaultOperationRunner(this.jobControl, var2);
   }
}
