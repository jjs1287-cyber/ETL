package org.etlforge.integration.client.ui.interchange.testhistory;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.testhistory.TestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.tf.LoadSourceHistoryView;

public final class LoadSourceHistoryViewFactory extends AbstractTestHistoryViewFactory {
   public LoadSourceHistoryViewFactory(CurrentProjectService var1) {
      super(LoadSourceHistoryView.CONTENT_ID, var1);
   }

   @Override
   protected TestResultHistoryView createNewView(WindowSection var1) {
      return new LoadSourceHistoryView(var1);
   }
}
