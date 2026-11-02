package org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import java.util.HashSet;
import java.util.List;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.event.UpdatedDependenciesEvent;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.PropertyService;

public final class ProjectEntityDependenciesModel implements EntityDependenciesModel {
   public static final String DEPENDANTS = "dependants";
   private final ManagedProject project;
   private final ProjectEntityDependenciesModel.Listener projectListener;
   private final ProjectEntityDependenciesModel.Collector collector;
   private final ListProperty<IntegrationEntity> dependants;
   private boolean disposed;

   public ProjectEntityDependenciesModel(IntegrationEntity var1, ManagedProject var2, EntityFilter var3) {
      this.project = var2;
      this.collector = new ProjectEntityDependenciesModel.Collector(var1, var2.getDependencyStore(), var3);
      this.projectListener = new ProjectEntityDependenciesModel.Listener();
      var2.getEventBus().register(this.projectListener);
      PropertyService var4 = new PropertyService(this);
      this.dependants = var4.newListProperty("dependants");
      this.update();
   }

   private void update() {
      this.dependants.set(this.collector.collect());
   }

   @Override
   public Property<List<IntegrationEntity>> getDependants() {
      return this.dependants;
   }

   @Override
   public void dispose() {
      if (!this.disposed) {
         this.project.getEventBus().unregister(this.projectListener);
         this.disposed = true;
      }
   }

   @Override
   public boolean isEmpty() {
      return this.dependants.get().isEmpty();
   }

   private static class Collector {
      private final IntegrationEntity entity;
      private final DependencyStore dependencies;
      private final EntityFilter filter;

      public Collector(IntegrationEntity var1, DependencyStore var2, EntityFilter var3) {
         this.entity = var1;
         this.dependencies = var2;
         this.filter = var3;
      }

      public List<IntegrationEntity> collect() {
         HashSet var1 = Sets.newHashSet();

         for (IntegrationEntity var3 : this.dependencies.getDependingObjects(this.entity)) {
            if (this.filter.apply(var3)) {
               var1.add(var3);
            } else {
               Iterables.addAll(var1, this.collectWizards(var3));
            }
         }

         var1.remove(this.entity);
         return Lists.newArrayList(var1);
      }

      private Iterable<IntegrationEntity> collectWizards(IntegrationEntity var1) {
         Iterable var2 = this.dependencies.getDependingObjectsRecursively(var1);
         return Iterables.filter(var2, EntityFilters.WIZARDS);
      }
   }

   private class Listener {
      private Listener() {
      }

      @Subscribe
      public void handle(UpdatedDependenciesEvent var1) {
         ProjectEntityDependenciesModel.this.update();
      }
   }
}
