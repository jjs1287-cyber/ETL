package org.etlforge.integration.data.project;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.util.event.ListenerManager;

final class DeleteManager {
   private final IntegrationProject project;
   private final PropertyChangeListener entityListener;
   private List<IntegrationEntity> collectedEntities;

   public DeleteManager(IntegrationProject var1) {
      this.project = var1;
      this.entityListener = new DeleteManager.EntityListener();
   }

   public void listenTo(IntegrationEntity var1) {
      var1.addPropertyChangeListener("Deleted", this.entityListener);
   }

   private void stopListeningTo(IntegrationEntity var1) {
      var1.removePropertyChangeListener("Deleted", this.entityListener);
   }

   public void startCollecting() {
      this.collectedEntities = Lists.newArrayList();
   }

   public void processDeletedItems(ListenerManager<ProjectListener> var1) {
      if (this.collectedEntities != null && !this.collectedEntities.isEmpty()) {
         this.stopListeningToDeletedEntities();
         IntegrationProjectEvent var2 = new IntegrationProjectEvent(this.project, this.collectedEntities);
         this.notifyListeners(var1, var2);
      }
   }

   private void stopListeningToDeletedEntities() {
      for (IntegrationEntity var2 : this.collectedEntities) {
         this.stopListeningTo(var2);
      }
   }

   private void notifyListeners(ListenerManager<ProjectListener> var1, IntegrationProjectEvent var2) {
      ((ProjectListener)var1.call()).entitiesDeleted(var2);
      this.collectedEntities = null;
   }

   private class EntityListener implements PropertyChangeListener {
      private EntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (DeleteManager.this.collectedEntities != null) {
            DeleteManager.this.collectedEntities.add((IntegrationEntity)var1.getSource());
         }
      }
   }
}
