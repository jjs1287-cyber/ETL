package org.etlforge.integration.client.ui.interchange.testhistory;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.testhistory.TestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.conn.TestConnectionHistoryView;

public final class TestConnectionHistoryViewFactory extends AbstractTestHistoryViewFactory {
   public TestConnectionHistoryViewFactory(CurrentProjectService var1) {
      super(TestConnectionHistoryView.CONTENT_ID, var1);
   }

   @Override
   protected TestResultHistoryView createNewView(WindowSection var1) {
      return new TestConnectionHistoryView(var1);
   }
}
