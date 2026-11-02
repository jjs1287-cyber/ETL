package org.etlforge.integration.data.entity;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.project.ProjectDependencies;

public final class RequiredObjectsCollector {
   private final ProjectDependencies dependencies;

   public RequiredObjectsCollector(ProjectDependencies var1) {
      this.dependencies = var1;
   }

   public Set<IntegrationEntity> collect(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      this.collectImpl(var1, var2, new HashSet<>());
      return var2;
   }

   private void collectImpl(IntegrationEntity var1, Set<IntegrationEntity> var2, Set<IntegrationEntity> var3) {
      if (var3.add(var1)) {
         for (IntegrationEntity var5 : this.dependencies.getRequiredObjects(var1)) {
            this.collectImpl(var5, var2, var3);
            var2.add(var5);
         }
      }
   }
}
