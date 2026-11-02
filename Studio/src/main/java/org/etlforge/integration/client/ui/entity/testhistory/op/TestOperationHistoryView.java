package org.etlforge.integration.client.ui.entity.testhistory.op;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.ProjectTestHistory;
import org.etlforge.integration.client.ui.entity.testhistory.AbstractTestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.resources.TestHistoryResources;
import org.etlforge.integration.data.test.TestOperationResult;
import org.etlforge.integration.data.test.TestResultHistory;

public final class TestOperationHistoryView extends AbstractTestResultHistoryView<TestOperationResult> {
   public static final String CONTENT_ID = TestOperationHistoryView.class.getName();

   public TestOperationHistoryView(WindowSection var1) {
      super(CONTENT_ID, TestOperationResult.class, new TestOperationHistoryTable(null), var1);
   }

   @Override
   protected TestResultHistory<TestOperationResult> getHistory(ProjectTestHistory var1) {
      return var1.getTestOperationHistory();
   }

   @Override
   public String getTitle() {
      return TestHistoryResources.getString("TestOp.History.Title");
   }
}
