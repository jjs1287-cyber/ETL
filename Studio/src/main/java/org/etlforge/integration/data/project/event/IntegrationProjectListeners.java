package org.etlforge.integration.data.project.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public final class IntegrationProjectListeners extends AbstractListenerManager<ProjectListener> implements ListenerManager<ProjectListener> {
   protected ProjectListener createNotifier() {
      return new ProjectListener() {
         @Override
         public void entitiesAdded(IntegrationProjectEvent var1) {
            for (ProjectListener var3 : IntegrationProjectListeners.this.getListeners()) {
               var3.entitiesAdded(var1);
            }
         }

         @Override
         public void entitiesDeleted(IntegrationProjectEvent var1) {
            for (ProjectListener var3 : IntegrationProjectListeners.this.getListeners()) {
               var3.entitiesDeleted(var1);
            }
         }

         @Override
         public void entitiesMoved(IntegrationProjectEvent var1) {
            for (ProjectListener var3 : IntegrationProjectListeners.this.getListeners()) {
               var3.entitiesMoved(var1);
            }
         }

         @Override
         public void entitiesRevived(IntegrationProjectEvent var1) {
            for (ProjectListener var3 : IntegrationProjectListeners.this.getListeners()) {
               var3.entitiesRevived(var1);
            }
         }
      };
   }
}
