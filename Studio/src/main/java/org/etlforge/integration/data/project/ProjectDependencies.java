package org.etlforge.integration.data.project;

import com.google.common.collect.FluentIterable;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface ProjectDependencies {
   ProjectDependencies EMPTY = new ProjectDependencies() {
      @Override
      public FluentIterable<IntegrationEntity> getRequiredObjects(IntegrationEntity var1) {
         return Iterables2.empty();
      }

      @Override
      public FluentIterable<IntegrationEntity> getDependingObjects(IntegrationEntity var1) {
         return Iterables2.empty();
      }
   };

   FluentIterable<IntegrationEntity> getRequiredObjects(IntegrationEntity var1);

   FluentIterable<IntegrationEntity> getDependingObjects(IntegrationEntity var1);
}
