package org.etlforge.integration.client.ui.interchange.testconnection;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.data.project.IntegrationProject;

public interface TestAllConnectionsView extends WindowSectionContent {
   String ID = TestAllConnectionsView.class.getName();

   void startTest();

   void setProject(IntegrationProject var1);
}
