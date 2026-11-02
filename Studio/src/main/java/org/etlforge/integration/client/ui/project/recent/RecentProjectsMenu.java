package org.etlforge.integration.client.ui.project.recent;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.project.event.RecentProjectsListener;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;

public final class RecentProjectsMenu implements Disposable {
   private final RecentProjects recentProjects;
   private final RecentProjectsMenuBuilder builder;
   private final RecentProjectsListener updater = new RecentProjectsMenu.Updater();

   public RecentProjectsMenu(ApplicationWindow var1, RecentProjects var2, final ProjectLoader var3) {
      this.recentProjects = var2;
      this.builder = new RecentProjectsMenuBuilder(var1, new Receiver<ProjectLocation>() {
         public void handle(ProjectLocation var1) {
            var3.loadAndDisplay(var1);
         }
      });
      var2.addRecentProjectsListener(this.updater);
      this.rebuild();
   }

   @Override
   public void dispose() {
      this.recentProjects.removeRecentProjectsListener(this.updater);
   }

   public void rebuild() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RecentProjectsMenu.this.builder.rebuild(RecentProjectsMenu.this.recentProjects.getExistingLocations());
         }
      });
   }

   private class Updater implements RecentProjectsListener {
      private Updater() {
      }

      @Override
      public void recentProjectsChanged(RecentProjects var1) {
         RecentProjectsMenu.this.rebuild();
      }
   }
}
