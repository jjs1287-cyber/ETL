package org.etlforge.integration.data.validation;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.project.ProjectDependencies;

public interface ValidationContext {
   ValidationContext EMPTY = new ValidationContext() {
      @Override
      public ProjectDependencies getDependencies() {
         return ProjectDependencies.EMPTY;
      }

      @Override
      public IntegrationEntityLookup getDataStore() {
         return IntegrationEntityLookup.NULL;
      }
   };

   IntegrationEntityLookup getDataStore();

   ProjectDependencies getDependencies();
}
