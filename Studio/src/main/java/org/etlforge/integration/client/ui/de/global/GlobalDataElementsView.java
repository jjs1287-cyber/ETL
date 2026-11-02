package org.etlforge.integration.client.ui.de.global;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.data.project.IntegrationProject;

public interface GlobalDataElementsView extends WindowSectionContent {
   String CONTENT_ID = GlobalDataElementsView.class.getName();

   void setProject(IntegrationProject var1);
}
