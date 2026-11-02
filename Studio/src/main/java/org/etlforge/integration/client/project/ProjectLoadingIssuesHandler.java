package org.etlforge.integration.client.project;

import java.util.Collection;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectLoadingIssuesHandler {
   ProjectLoadingIssuesHandler IGNORE = new ProjectLoadingIssuesHandler() {
      @Override
      public void handle(IntegrationProject var1, Collection<ProjectLoadingIssue> var2) {
      }
   };

   void handle(IntegrationProject var1, Collection<ProjectLoadingIssue> var2);
}
