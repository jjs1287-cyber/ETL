package org.etlforge.integration.client.ui.entity.testhistory.conn;

import javax.swing.Icon;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.project.ProjectTestHistory;
import org.etlforge.integration.client.ui.entity.testhistory.AbstractTestResultHistoryView;
import org.etlforge.integration.client.ui.entity.testhistory.resources.TestHistoryResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResultHistory;

public final class TestConnectionHistoryView extends AbstractTestResultHistoryView<TestConnectionResult> {
   public static final String CONTENT_ID = TestConnectionHistoryView.class.getName();

   public TestConnectionHistoryView(WindowSection var1) {
      super(CONTENT_ID, TestConnectionResult.class, new TestConnectionHistoryTable(null), var1);
   }

   @Override
   protected TestResultHistory<TestConnectionResult> getHistory(ProjectTestHistory var1) {
      return var1.getTestConnectionHistory();
   }

   public void setLocatable(Locatable var1) {
   }

   @Override
   public String getTitle() {
      return TestHistoryResources.getString("Connection.Result.View.Title");
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.TEST_CONNECTION_16;
   }
}
