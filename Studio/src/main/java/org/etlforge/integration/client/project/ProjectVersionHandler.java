package org.etlforge.integration.client.project;

import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectVersionHandler {
   ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3);

   boolean postLoading(IntegrationProject var1, ProjectFile var2);

   enum PreLoadingResult {
      NOT_APPLICABLE,
      AUTOMATIC,
      ALLOWED,
      DENIED;
   }
}
