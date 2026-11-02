package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.EntityDependenciesModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.NoDependencies;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.ProjectEntityDependenciesModel;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;

final class DependenciesModelBuilder {
   private final EntityFilter filter;

   public DependenciesModelBuilder(EntityFilter var1) {
      this.filter = var1;
   }

   public EntityDependenciesModel buildModel(IntegrationEntity var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      return var2 != null ? new ProjectEntityDependenciesModel(var1, var2, this.filter) : new NoDependencies();
   }
}
