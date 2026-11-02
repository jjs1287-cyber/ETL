package org.etlforge.integration.client.ui.project.recent;

import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public interface RecentProjectsViewer extends UiProvider, SelectionSource {
   void update();

   ProjectLocation getSelectedProject();

   void setDefaultAction(SelectionAction var1);

   OrderedSelection getSelection();

   RecentProjects getRecentProjects();
}
