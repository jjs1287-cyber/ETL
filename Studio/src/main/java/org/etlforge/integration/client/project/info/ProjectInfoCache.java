package org.etlforge.integration.client.project.info;

import java.util.List;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectInfoCache {
   ProjectInfoCache EMPTY = new ProjectInfoCache() {
      @Override
      public void storeProjects(List<IntegrationProject> var1) {
      }

      @Override
      public List<IntegrationProject> getProjects() {
         return null;
      }

      @Override
      public void clear() {
      }
   };

   List<IntegrationProject> getProjects();

   void storeProjects(List<IntegrationProject> var1);

   void clear();
}
