package org.etlforge.integration.client.ui.project.info;

import org.etlforge.ui.UiProvider;

interface ProjectConsoleSubViewContainer extends UiProvider {
   int getViewCount();

   ProjectConsoleSubView getSubView(String var1);

   void showSubView(ProjectConsoleSubView var1);

   void removeSubView(String var1);
}
