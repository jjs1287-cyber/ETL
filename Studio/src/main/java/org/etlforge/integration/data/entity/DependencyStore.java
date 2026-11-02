package org.etlforge.integration.data.entity;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.collections.Predicates2;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.project.Dependencies;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.system.Disposable;

public final class DependencyStore implements ProjectDependencies, Disposable {
   private final Dependencies linkStore;

   public static DependencyStore empty() {
      return new DependencyStore(new IntegrationEntityMapLookup());
   }

   public DependencyStore(Dependencies var1) {
      this.linkStore = var1;
   }

   public DependencyStore(IntegrationEntityMapLookup var1) {
      this(createLinks(var1));
   }

   private static Dependencies createLinks(IntegrationEntityMapLookup var0) {
      Dependencies var1 = new Dependencies();

      for (IntegrationEntity var3 : var0.getAllInstances()) {
         for (EntityLink var5 : var3.getLinks(var0)) {
            var1.addLink(var5);
         }
      }

      return var1;
   }

   public Iterable<? extends EntityLink> getLinksFrom(IntegrationEntity var1) {
      return this.linkStore.getAllLinksFrom(var1);
   }

   @Override
   public FluentIterable<IntegrationEntity> getRequiredObjects(IntegrationEntity var1) {
      return this.linkStore.getRequiredObjects(var1).filter(Predicates2.isNot(var1));
   }

   public Iterable<IntegrationEntity> getRequiredObjectsRecursively(IntegrationEntity var1) {
      Set var2 = this.linkStore.getRequiredObjectsRecursively(var1);
      var2.remove(var1);
      return ImmutableSet.copyOf(var2);
   }

   @Override
   public FluentIterable<IntegrationEntity> getDependingObjects(IntegrationEntity var1) {
      return this.linkStore.getAllLinksFrom(var1).transform(EntityLink.TO_DEPENDANT).filter(Predicates2.isNot(var1));
   }

   public Iterable<IntegrationEntity> getDependingObjectsRecursively(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      this.collectDependingObjectsRecursively(var1, var2);
      var2.remove(var1);
      return ImmutableSet.copyOf(var2);
   }

   private void collectDependingObjectsRecursively(IntegrationEntity var1, Set<IntegrationEntity> var2) {
      if (var2.add(var1)) {
         for (IntegrationEntity var4 : this.getDependingObjects(var1)) {
            this.collectDependingObjectsRecursively(var4, var2);
         }
      }
   }

   public boolean isActive(IntegrationEntity var1) {
      return this.linkStore.isActive(var1);
   }

   @Override
   public void dispose() {
      this.linkStore.dispose();
   }
}
