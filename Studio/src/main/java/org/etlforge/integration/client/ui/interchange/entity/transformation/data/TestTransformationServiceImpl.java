package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.test.TestResultException;
import org.etlforge.integration.data.test.TestTransformationHistory;
import org.etlforge.integration.data.test.TestTransformationResult;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.system.callback.CallbackResultAdapter;
import org.etlforge.ui.util.UiUtils;

final class TestTransformationServiceImpl implements TestTransformationService {
   private final DebugService debugService;
   private final DeployFacade deployFacade;
   private final TransformationPage page;
   private final SourceDataService loadSourceService;
   private final TestProcessCallback callback;

   public TestTransformationServiceImpl(TransformationPage var1, DebugService var2, SourceDataService var3, DeployFacade var4, TestProcessCallback var5) {
      this.page = var1;
      this.debugService = var2;
      this.loadSourceService = var3;
      this.deployFacade = var4;
      this.callback = var5;
   }

   @Override
   public void testTransformation() {
      Transformation var1 = this.getTransformation();
      if (this.needsSourceData(var1) && this.loadSourceService.getSourceDataToken() == null) {
         this.promptForSourceData();
      } else {
         this.testWithExistingSourceData(var1);
      }
   }

   private Transformation getTransformation() {
      return this.page.getObject();
   }

   private boolean needsSourceData(Transformation var1) {
      return var1.isSourceDataUsedInMapping() && var1.getInputStructure().getStructureType().isSourceDataRequired();
   }

   private void promptForSourceData() {
      Answer var1 = Ask.yesOrNo("You must load source data into the transformation.\n\nDo you want to continue?", "Load source data?");
      if (var1.isYes()) {
         this.loadSourceData();
      }
   }

   private void loadSourceData() {
      this.loadSourceService.loadSourceData(new CallbackResultAdapter() {
         @Override
         public void succeeded(Object var1) {
            TestTransformationServiceImpl.this.testTransformation();
         }
      });
   }

   private void testWithExistingSourceData(Transformation var1) {
      DeployGate var2 = this.deployFacade.newDeployGate();
      var2.setItemsToCheck(var1);
      var2.setJob(new Runnable() {
         @Override
         public void run() {
            TestTransformationServiceImpl.this.testTransformationImpl();
         }
      });
      var2.start();
   }

   private void testTransformationImpl() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            TestTransformationServiceImpl.this.removeCurrentTargetData();
            TestTransformationServiceImpl.TestTransformationHandler var1x = TestTransformationServiceImpl.this.new TestTransformationHandler();
            DataStructure var2 = TestTransformationServiceImpl.this.getTransformation().getInputStructure();
            if (var2.getStructureType().isSourceDataRequired()) {
               SourceDataIdentifier var3 = TestTransformationServiceImpl.this.loadSourceService.getSourceDataToken();
               TestTransformationServiceImpl.this.submitTestTransformationJob(var3, var1x);
            } else {
               TestTransformationServiceImpl.this.submitTestTransformationJob(null, var1x);
            }
         }
      };
      UiUtils.runOnEventThread(var1);
   }

   private void removeCurrentTargetData() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.removeTargetData();
   }

   private void submitTestTransformationJob(SourceDataIdentifier var1, ServerTestCallback var2) {
      Transformation var3 = this.page.getObject();
      TestTransformationJob var4 = new TestTransformationJob(var3, var1, this.debugService, var2);
      var4.submit(this.page.getWaitService(), 0);
   }

   private class TestTransformationHandler extends ServerTestCallbackAdapter {
      private final TestTransformationHistory history;

      public TestTransformationHandler() {
         ManagedProject var2 = ProjectUtils.getManagedProject(TestTransformationServiceImpl.this.getTransformation());
         this.history = var2.getTestHistory().getTestTransformationHistory();
      }

      @Override
      public void completed(DebugSession var1, TestResult var2) {
         this.history.add(TestTransformationResult.success(TestTransformationServiceImpl.this.getTransformation(), var2.getWarnings()));
         if (TestTransformationServiceImpl.this.callback != null) {
            TestTransformationServiceImpl.this.callback.testCompletedSuccessfully(var2);
         }

         this.showWarnings(var2.getWarnings());
      }

      @Override
      public void caught(Throwable var1) {
         this.persistError(var1);
         ErrorLog.attention(TestTransformationServiceImpl.TestTransformationHandler.class, Strings.get("TestTransformation.Error.Message"), var1);
      }

      private void persistError(Throwable var1) {
         TestResultException var2 = new TestResultException(var1);
         TestTransformationResult var3 = TestTransformationResult.error(TestTransformationServiceImpl.this.getTransformation(), var2);
         this.history.add(var3);
      }

      private void showWarnings(String var1) {
         if (var1 != null && var1.length() > 0) {
            WarningsDisplayer.display(var1, Strings.get("TestTransformation.Warning.Title"));
         }
      }
   }
}
