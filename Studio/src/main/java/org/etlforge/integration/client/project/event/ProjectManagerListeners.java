package org.etlforge.integration.client.project.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public final class ProjectManagerListeners extends AbstractListenerManager<ProjectManagerListener> implements ListenerManager<ProjectManagerListener> {
   protected ProjectManagerListener createNotifier() {
      return new ProjectManagerListener() {
         @Override
         public void projectClosed(ProjectManagerEvent var1) {
            for (ProjectManagerListener var3 : ProjectManagerListeners.this.getListeners()) {
               var3.projectClosed(var1);
            }
         }

         @Override
         public void projectOpened(ProjectManagerEvent var1) {
            for (ProjectManagerListener var3 : ProjectManagerListeners.this.getListeners()) {
               var3.projectOpened(var1);
            }
         }
      };
   }
}
