package org.etlforge.integration.data.project;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;

public abstract class ProjectItemPreparer implements Disposable {
   private final IntegrationProject project;
   private final ImmutableSet<EntityType> types;
   private final ProjectItemPreparer.Listener listener = new ProjectItemPreparer.Listener();

   public ProjectItemPreparer(IntegrationProject var1) {
      this(var1, allTypes(var1));
   }

   private static Set<EntityType> allTypes(IntegrationProject var0) {
      return new Builder<EntityType>().addAll(var0.getSupportedEntityTypes()).add(EntityType.Folder).build();
   }

   public ProjectItemPreparer(IntegrationProject var1, Iterable<EntityType> var2) {
      this.project = var1;
      this.types = ImmutableSet.copyOf(var2);
      var1.addProjectListener(this.listener);
   }

   public final void insertAll() {
      this.project.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            ProjectItemPreparer.this.entityInserted(var1);
         }
      });
   }

   public final void removeAll() {
      this.project.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            ProjectItemPreparer.this.entityRemoved(var1);
         }
      });
   }

   @Override
   public final void dispose() {
      this.project.removeProjectListener(this.listener);
   }

   protected abstract void inserted(IntegrationEntity var1);

   protected abstract void removed(IntegrationEntity var1);

   private void entityInserted(IntegrationEntity var1) {
      if (this.types.contains(var1.getEntityType())) {
         this.inserted(var1);
      }
   }

   private void entityRemoved(IntegrationEntity var1) {
      if (this.types.contains(var1.getEntityType())) {
         this.removed(var1);
      }
   }

   private class Listener implements ProjectListener {
      private Listener() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.handleInsert(var1);
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.handleInsert(var1);
      }

      private void handleInsert(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            ProjectItemPreparer.this.entityInserted(var3);
         }
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            ProjectItemPreparer.this.entityRemoved(var3);
         }
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
      }
   }
}
