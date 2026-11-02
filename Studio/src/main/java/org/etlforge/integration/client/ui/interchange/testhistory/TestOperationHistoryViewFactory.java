package org.etlforge.integration.client.ui.interchange.testhistory;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.testhistory.TestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.op.TestOperationHistoryView;

public final class TestOperationHistoryViewFactory extends AbstractTestHistoryViewFactory {
   public TestOperationHistoryViewFactory(CurrentProjectService var1) {
      super(TestOperationHistoryView.CONTENT_ID, var1);
   }

   @Override
   protected TestResultHistoryView createNewView(WindowSection var1) {
      return new TestOperationHistoryView(var1);
   }
}
