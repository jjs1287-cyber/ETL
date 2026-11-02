package com.etlforge.integration.client.ui.interchange.core.action;

import com.etlforge.integration.operation.profiling.client.ProfilingOrchestrator;
import com.etlforge.integration.operation.profiling.client.ProfilingResultCollectionPresenter;
import com.etlforge.integration.operation.profiling.server.OperationProfiler;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerPreConditions;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class ProfileOperationAction extends AbstractSingleActiveObjectAction<Operation> {
   public static final String ID = "action.ic.ProfileOperation";
   private final ApplicationWindow appWin;
   private final ProjectPersistor projectPersistor;
   private final DeployFacade deployFacade;

   public ProfileOperationAction(ApplicationWindow var1, ProjectPersistor var2, DeployFacade var3) {
      super(var1.getActiveObjectService(), "action.ic.ProfileOperation", EnterpriseActionDictionary.DICTIONARY, Operation.class);
      this.appWin = var1;
      this.projectPersistor = var2;
      this.deployFacade = var3;
   }

   protected void runOnObject(Operation var1) {
      OperationRunnerPreConditions var2 = this.createPreconditions();
      var2.checkAndRun(var1, new ProfileOperationAction.StartProfilingJob(var1));
   }

   private OperationRunnerPreConditions createPreconditions() {
      DeployGate var1 = this.deployFacade.newDeployGate();
      OperationRunnerPreConditions var2 = new OperationRunnerPreConditions(var1, this.projectPersistor);
      var2.setTextToShowIfNotRunnable("The selected operation can not be profiled.");
      return var2;
   }

   private class StartProfilingJob implements Runnable {
      private final Operation op;

      public StartProfilingJob(Operation var2) {
         this.op = var2;
      }

      @Override
      public void run() {
         ProfilingOrchestrator var1 = this.createOrchestrator(this.op);
         var1.start();
      }

      private ProfilingOrchestrator createOrchestrator(Operation var1) {
         ProfilingOrchestrator var2 = new ProfilingOrchestrator();
         var2.setOperation(var1.getID())
            .setProfiler(this.getProfiler())
            .setOperationLookup(this.getOperationLookup(var1))
            .setResultPresenter(new ProfilingResultCollectionPresenter())
            .setWaitService(ProfileOperationAction.this.appWin);
         return var2;
      }

      private OperationProfiler getProfiler() {
         IntegrationServer var1 = IntegrationServer.getInstance();
         return var1.getServerCall(OperationProfiler.class);
      }

      private IntegrationEntityLookup getOperationLookup(Operation var1) {
         IntegrationProject var2 = ProjectUtils.getProject(var1);
         return var2 != null ? var2.getItemLookup() : null;
      }
   }
}
