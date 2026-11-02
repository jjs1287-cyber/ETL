package org.etlforge.integration.client.ui.entity.testhistory.tf;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.ProjectTestHistory;
import org.etlforge.integration.client.ui.entity.testhistory.AbstractTestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.resources.TestHistoryResources;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.TestResultHistory;
import org.etlforge.integration.data.test.TestTransformationResult;

public final class TestTransformationHistoryView extends AbstractTestResultHistoryView<TestTransformationResult> {
   public static final String CONTENT_ID = TestTransformationHistoryView.class.getName();

   public TestTransformationHistoryView(WindowSection var1) {
      super(CONTENT_ID, TestTransformationResult.class, new TestTransformationHistoryTable(null), var1);
   }

   @Override
   protected TestResultHistory<TestTransformationResult> getHistory(ProjectTestHistory var1) {
      return var1.getTestTransformationHistory();
   }

   public void setTransformation(Transformation var1) {
   }

   @Override
   public String getTitle() {
      return TestHistoryResources.getString("TestTransformation.History.Title");
   }
}
