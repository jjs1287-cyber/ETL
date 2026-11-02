package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.TestOperationHistory;
import org.etlforge.integration.data.test.TestOperationResult;
import org.etlforge.integration.data.test.TestResultError;
import org.etlforge.integration.data.test.TestResultException;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;

final class TestOperationServiceImpl implements TestOperationService {
   private final TransformationPage page;
   private final DebugService debugService;
   private final TestProcessCallback callback;

   public TestOperationServiceImpl(TransformationPage var1, DebugService var2, TestProcessCallback var3) {
      this.page = var1;
      this.debugService = var2;
      this.callback = var3;
   }

   @Override
   public void testOperation(Operation var1) {
      this.callback.beforeTestStarts();
      Transformation var2 = this.page.getObject();
      TestOperationServiceImpl.TestOperationHandler var3 = new TestOperationServiceImpl.TestOperationHandler(var1, var2, this.callback);
      TestOperationJob var4 = new TestOperationJob(var1, var2, this.debugService, var3);
      var4.setUseProgressDialog(0);
      var4.submit(this.page.getWaitService(), 0);
   }

   private static final class TestOperationHandler extends ServerTestCallbackAdapter {
      private final Operation operation;
      private final Transformation transformation;
      private final TestOperationServiceImpl.TestOperationHandler.TestHistoryWriter historyWriter;
      private final TestProcessCallback interestedInSuccess;

      public TestOperationHandler(Operation var1, Transformation var2, TestProcessCallback var3) {
         this.operation = var1;
         this.transformation = var2;
         this.interestedInSuccess = var3;
         ManagedProject var4 = ProjectUtils.getManagedProject(var2);
         this.historyWriter = new TestOperationServiceImpl.TestOperationHandler.TestHistoryWriter(var4);
      }

      @Override
      public void completed(DebugSession var1, TestResult var2) {
         this.showWarnings(var2.getWarnings());
         this.historyWriter.handleSuccess(var2.getWarnings());
         if (this.interestedInSuccess != null) {
            this.interestedInSuccess.testCompletedSuccessfully(var2);
         }
      }

      @Override
      public void caught(Throwable var1) {
         this.persistError(var1);
         ErrorLog.attention(TestOperationServiceImpl.TestOperationHandler.class, Strings.get("TestOperation.Error.Message"), var1);
      }

      private void persistError(Throwable var1) {
         TestResultException var2 = new TestResultException(var1);
         this.historyWriter.handleError(var2);
      }

      private void showWarnings(String var1) {
         if (!StringUtils.isBlank(var1) && !this.isBogusWarning(var1)) {
            WarningsDisplayer.display(var1, Strings.get("TestOperation.Warning.Title"));
         }
      }

      private boolean isBogusWarning(String var1) {
         if (this.transformation.isPartOfSalesforcePartnerWizard()) {
            String var2 = "Query returned \\d+ records\\.";
            return Pattern.matches(var2, var1.trim());
         } else {
            return false;
         }
      }

      private class TestHistoryWriter {
         private final TestOperationHistory opHistory;

         public TestHistoryWriter(ManagedProject var2) {
            this.opHistory = var2.getTestHistory().getTestOperationHistory();
         }

         public void handleError(TestResultError var1) {
            TestOperationResult var2 = TestOperationResult.error(TestOperationHandler.this.operation, TestOperationHandler.this.transformation, var1);
            this.opHistory.add(var2);
         }

         public void handleSuccess(String var1) {
            TestOperationResult var2 = TestOperationResult.success(TestOperationHandler.this.operation, TestOperationHandler.this.transformation, var1);
            this.opHistory.add(var2);
         }
      }
   }
}
