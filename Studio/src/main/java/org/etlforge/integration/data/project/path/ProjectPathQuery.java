package org.etlforge.integration.data.project.path;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public class ProjectPathQuery {
   private final IntegrationProject project;

   public ProjectPathQuery(IntegrationProject var1) {
      this.project = var1;
   }

   public Optional<IntegrationEntity> findItem(String var1) {
      ProjectPath var2 = new ProjectPath(var1);
      return var2.search(this.project);
   }

   public Set<IntegrationEntity> findItems(String var1) {
      ProjectPath var2 = new ProjectPath(var1);
      return Sets.newHashSet(var2.searchForItems(this.project));
   }
}
