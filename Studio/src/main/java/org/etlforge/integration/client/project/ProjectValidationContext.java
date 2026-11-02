package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.validation.ValidationContext;

public class ProjectValidationContext implements ValidationContext {
   private final ManagedProject project;

   public ProjectValidationContext(ManagedProject var1) {
      this.project = Preconditions.checkNotNull(var1);
   }

   @Override
   public IntegrationEntityLookup getDataStore() {
      return this.project.getProject().getItemLookup();
   }

   public DependencyStore getDependencies() {
      return this.project.getDependencyStore();
   }
}
