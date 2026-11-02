package org.etlforge.integration.client.ui.interchange.content;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.data.entity.ProcessFilterProvider;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectContentViewerManager {
   void giveFocusToFilterField();

   void setProcessFilterProvider(ProcessFilterProvider var1);

   ProjectContentViewer getViewer();

   WindowSectionContent getWindowContent();

   void newProjectOpened(IntegrationProject var1);

   void projectClosed();

   void dispose();
}
