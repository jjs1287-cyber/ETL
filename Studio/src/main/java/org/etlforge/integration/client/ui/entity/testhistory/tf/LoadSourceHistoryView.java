package org.etlforge.integration.client.ui.entity.testhistory.tf;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.ProjectTestHistory;
import org.etlforge.integration.client.ui.entity.testhistory.AbstractTestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.resources.TestHistoryResources;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.LoadSourceResult;
import org.etlforge.integration.data.test.TestResultHistory;

public final class LoadSourceHistoryView extends AbstractTestResultHistoryView<LoadSourceResult> {
   public static final String CONTENT_ID = LoadSourceHistoryView.class.getName();

   public LoadSourceHistoryView(WindowSection var1) {
      super(CONTENT_ID, LoadSourceResult.class, new LoadSourceHistoryTable(null), var1);
   }

   @Override
   protected TestResultHistory<LoadSourceResult> getHistory(ProjectTestHistory var1) {
      return var1.getLoadSourceHistory();
   }

   public void setTransformation(Transformation var1) {
   }

   @Override
   public String getTitle() {
      return TestHistoryResources.getString("LoadSource.History.Title");
   }
}
