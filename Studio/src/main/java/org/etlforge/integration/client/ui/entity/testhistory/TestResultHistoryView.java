package org.etlforge.integration.client.ui.entity.testhistory;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.test.TestResultFilter;

public interface TestResultHistoryView extends WindowSectionContent {
   void setFilter(TestResultFilter var1);

   void setProject(ManagedProject var1);
}
