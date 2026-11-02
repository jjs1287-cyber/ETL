package org.etlforge.integration.client.ui.entity.tree;

import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectNodeRenamer {
   ProjectNodeRenamer READ_ONLY = new ProjectNodeRenamer() {
      @Override
      public void rename(IntegrationProject var1) {
      }
   };

   void rename(IntegrationProject var1);
}
