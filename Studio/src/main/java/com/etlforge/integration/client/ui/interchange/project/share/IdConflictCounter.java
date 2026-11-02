package com.etlforge.integration.client.ui.interchange.project.share;

import com.google.common.collect.Iterables;
import java.util.Set;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsCollector;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;

final class IdConflictCounter {
   private final ManagedProject sourceProject;
   private final ManagedProject targetProject;

   public IdConflictCounter(ManagedProject var1, ManagedProject var2) {
      this.sourceProject = var1;
      this.targetProject = var2;
   }

   public int count(IntegrationEntity var1) {
      Set var2 = new RequiredObjectsCollector(this.sourceProject.getDependencyStore()).collect(var1);
      var2.add(var1);
      return this.count(var2);
   }

   private int count(Set<IntegrationEntity> var1) {
      return Iterables.size(Iterables.filter(var1, new IdConflictCounter.ConflictFilter()));
   }

   private class ConflictFilter implements EntityFilter {
      private ConflictFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return this.isApplicable(var1) && this.isConflict(var1);
      }

      private boolean isApplicable(IntegrationEntity var1) {
         return !(var1 instanceof Folder) && !(var1 instanceof BuiltInIntegrationEntity) && !(var1 instanceof IntegrationProject);
      }

      private boolean isConflict(IntegrationEntity var1) {
         return IdConflictCounter.this.targetProject.getProject().getItemLookup().getEntity(var1.getID()) != null;
      }
   }
}
