package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.entity.selector.EntityListSelector;
import org.etlforge.integration.client.ui.entity.selector.EntitySelectorDialog;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.operation.runner.OperationClientStartChecker;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.OperationExecutionMode;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.RunnableStatus;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.ui.util.UiUtils;

public final class OperationTesterFactory {
   private final DeployFacade deployFacade;

   public OperationTesterFactory(DeployFacade var1) {
      this.deployFacade = var1;
   }

   public OperationTester newTester(TransformationPage var1) {
      DeployGate var2 = this.deployFacade.newDeployGate();
      TestOperationService var3 = var1.getTreeDataService().getTestOperationService();
      return new OperationTesterFactory.TesterImpl(var3, var2);
   }

   private static final class TesterImpl implements OperationTester {
      private final TestOperationService service;
      private final DeployGate deployGate;

      public TesterImpl(TestOperationService var1, DeployGate var2) {
         this.service = var1;
         this.deployGate = var2;
      }

      @Override
      public void testOperation(Transformation var1) {
         IntegrationProject var2 = ProjectUtils.getProject(var1);
         if (var2 != null) {
            DependencyStore var3 = var2.getCurrentDependencies();

            try {
               Operation var4 = this.getOperationToTest(var3, var1);
               if (var4 != null && this.isOperationTestable(var4)) {
                  this.checkDeployAndRun(var1, var4);
               }
            } finally {
               var3.dispose();
            }
         }
      }

      private Operation getOperationToTest(ProjectDependencies var1, Transformation var2) {
         List var3 = this.getAvailableOperations(var1, var2);
         if (var3.isEmpty()) {
            this.noOperationsAvailable();
            return null;
         } else {
            return this.selectOperation(var3);
         }
      }

      private List<Operation> getAvailableOperations(ProjectDependencies var1, Transformation var2) {
         return var1.getDependingObjects(var2).filter(Operation.class).toImmutableList();
      }

      private Operation selectOperation(List<Operation> var1) {
         Operation var2 = null;
         if (var1.size() == 1) {
            var2 = (Operation)var1.get(0);
         } else {
            EntityListSelector var3 = new EntityListSelector<>(var1, Operation.class);
            var2 = (Operation)EntitySelectorDialog.selectOne(var3, UiUtils.getActiveWindow(), "Select an operation", "Select the operation you want to test:");
         }

         return var2;
      }

      private void noOperationsAvailable() {
         Alert.error("There are no operations using this transformation.", "Error");
      }

      private boolean isOperationTestable(Operation var1) {
         OperationClientStartChecker var2 = new OperationClientStartChecker(OperationExecutionMode.TEST);
         RunnableStatus var3 = var2.checkRunnableStatus(var1);
         boolean var4 = var3.isRunnable();
         if (!var4) {
            this.showNotTestableMessage(var3);
         }

         return var4;
      }

      private void showNotTestableMessage(RunnableStatus var1) {
         Alert.error(UiUtils.getActiveWindow(), var1.getReasonNotRunnable(), "Not Testable");
      }

      private void checkDeployAndRun(final Transformation var1, final Operation var2) {
         this.deployGate.setItemsToCheck(var1, var2);
         this.deployGate.setJob(new Runnable() {
            @Override
            public void run() {
               TesterImpl.this.testOperation(var1, var2);
            }
         });
         this.deployGate.start();
      }

      private void testOperation(final Transformation var1, final Operation var2) {
         Runnable var3 = new Runnable() {
            @Override
            public void run() {
               Editor var1x = UiCompanion.of(var1).findOpenPage();
               if (var1x != null) {
                  TesterImpl.this.service.testOperation(var2);
               } else {
                  ErrorLog.log(OperationTesterFactory.class, "The page displaying the transformation to test has been closed.", new Exception());
               }
            }
         };
         UiUtils.runOnEventThread(var3);
      }

      @Override
      public void testOperation(Operation var1) {
         if (this.isOperationTestable(var1)) {
            Transformation var2 = this.getTransformationToTest(var1);
            if (var2 == null) {
               Alert.error("The operation \"" + var1.getName() + "\" is incomplete and cannot be tested.", "Incomplete Operation");
            } else {
               this.checkDeployAndRun(var2, var1);
            }
         }
      }

      private Transformation getTransformationToTest(Operation var1) {
         OperationPipeline var2 = var1.getPipeline();
         OperationType var3 = var2.getType();
         if (var3 == TransformationOperationType.TYPE) {
            return new TransformationPipelineBrowser(var2).getTransformation();
         } else if (var3 == WebServiceOperationType.TYPE) {
            return new WebServicePipelineBrowser(var2).getResponse();
         } else {
            throw new RuntimeException("An unexpected condition in the OperationTester.");
         }
      }
   }
}
