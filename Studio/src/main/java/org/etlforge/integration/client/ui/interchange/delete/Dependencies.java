package org.etlforge.integration.client.ui.interchange.delete;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.ProjectDependencies;

public final class Dependencies implements ProjectDependencies {
   private final List<Dependencies.Dependency> dependencies;
   private final DependencyStore dependencyStore;
   private Set<IntegrationEntity> collectionOfItemsToDelete;

   public static Dependencies empty() {
      return new Dependencies(DependencyStore.empty());
   }

   public Dependencies(DependencyStore var1) {
      this.dependencyStore = var1;
      this.dependencies = Lists.newArrayList();
   }

   public void dispose() {
      this.dependencyStore.dispose();
   }

   @Override
   public FluentIterable<IntegrationEntity> getRequiredObjects(IntegrationEntity var1) {
      return this.dependencyStore.getRequiredObjects(var1);
   }

   @Override
   public FluentIterable<IntegrationEntity> getDependingObjects(IntegrationEntity var1) {
      return this.dependencyStore.getDependingObjects(var1);
   }

   public boolean isEmpty() {
      return this.dependencies.isEmpty();
   }

   public boolean isEmpty(EntityFilter var1) {
      for (Dependencies.Dependency var3 : this.dependencies) {
         if (var3.apply(var1)) {
            return false;
         }
      }

      return true;
   }

   public void addDependencies(Collection<IntegrationEntity> var1) {
      this.collectionOfItemsToDelete = Sets.newHashSet(var1);

      for (IntegrationEntity var3 : var1) {
         this.addDependencies(var3);
      }
   }

   private void addDependencies(IntegrationEntity var1) {
      for (EntityLink var3 : this.dependencyStore.getLinksFrom(var1)) {
         if (this.isImportantDependency(var1, var3)) {
            this.addDependency(var3, new HashSet<>());
         }
      }
   }

   private boolean isImportantDependency(IntegrationEntity var1, EntityLink var2) {
      IntegrationEntity var3 = var2.getOtherObject(var1);
      return !this.collectionOfItemsToDelete.contains(var3);
   }

   public List<IntegrationEntity> getDependantObjects() {
      ArrayList var1 = Lists.newArrayList();

      for (Dependencies.Dependency var3 : this.dependencies) {
         var1.add(var3.getObject());
      }

      return var1;
   }

   public Set<IntegrationEntity> getDependenciesThatWillBeDeleted() {
      HashSet var1 = Sets.newHashSet();

      for (Dependencies.Dependency var3 : this.dependencies) {
         if (var3.willBeDeleted) {
            var1.add(var3.getObject());
         }
      }

      return var1;
   }

   public boolean willBeDeleted(IntegrationEntity var1) {
      for (Dependencies.Dependency var3 : this.dependencies) {
         if (var3.getObject() == var1 && var3.willBeDeleted()) {
            return true;
         }
      }

      return false;
   }

   private void addDependency(EntityLink var1, Set<EntityLink> var2) {
      if (var2.add(var1)) {
         IntegrationEntity var3 = var1.getDependantObject();
         if (!this.collectionOfItemsToDelete.contains(var3)) {
            this.add(new Dependencies.Dependency(var3, var1.isStrong()));
            if (var1.isStrong()) {
               for (EntityLink var5 : this.dependencyStore.getLinksFrom(var3)) {
                  this.addDependency(var5, var2);
               }
            }
         }
      }
   }

   private void add(Dependencies.Dependency var1) {
      int var2 = this.dependencies.indexOf(var1);
      if (var2 != -1 && var1.willBeDeleted()) {
         Dependencies.Dependency var3 = this.dependencies.get(var2);
         if (!var3.willBeDeleted()) {
            this.dependencies.add(var2, var1);
         }
      } else {
         this.dependencies.add(var1);
      }
   }

   private static class Dependency {
      private final IntegrationEntity object;
      private final boolean willBeDeleted;

      public Dependency(IntegrationEntity var1, boolean var2) {
         this.object = var1;
         this.willBeDeleted = var2;
      }

      public boolean apply(EntityFilter var1) {
         return var1.apply(this.object);
      }

      public IntegrationEntity getObject() {
         return this.object;
      }

      public boolean willBeDeleted() {
         return this.willBeDeleted;
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof Dependencies.Dependency && ((Dependencies.Dependency)var1).object == this.object;
      }

      @Override
      public int hashCode() {
         return System.identityHashCode(this.object);
      }
   }
}
