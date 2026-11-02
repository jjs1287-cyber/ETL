package org.etlforge.integration.client.ui.interchange.component.menu;

import javax.swing.JMenuItem;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsMenu;

public final class RecentProjectsMenuItemFactory implements ViewMenuItemFactory {
   private final RecentProjects recentProjects;
   private final ProjectLoader projectLoader;

   public RecentProjectsMenuItemFactory(RecentProjects var1, ProjectLoader var2) {
      this.recentProjects = var1;
      this.projectLoader = var2;
   }

   @Override
   public JMenuItem createMenuItem(ApplicationWindow var1) {
      new RecentProjectsMenu(var1, this.recentProjects, this.projectLoader);
      return null;
   }
}
