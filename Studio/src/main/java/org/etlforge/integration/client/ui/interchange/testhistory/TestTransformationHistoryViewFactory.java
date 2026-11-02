package org.etlforge.integration.client.ui.interchange.testhistory;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.testhistory.TestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.tf.TestTransformationHistoryView;

public final class TestTransformationHistoryViewFactory extends AbstractTestHistoryViewFactory {
   public TestTransformationHistoryViewFactory(CurrentProjectService var1) {
      super(TestTransformationHistoryView.CONTENT_ID, var1);
   }

   @Override
   protected TestResultHistoryView createNewView(WindowSection var1) {
      return new TestTransformationHistoryView(var1);
   }
}
