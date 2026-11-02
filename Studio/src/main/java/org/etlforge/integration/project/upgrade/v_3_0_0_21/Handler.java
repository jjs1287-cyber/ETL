package org.etlforge.integration.project.upgrade.v_3_0_0_21;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.UpgradeStrategy;
import org.etlforge.integration.data.project.IntegrationProject;

final class Handler implements ProjectVersionHandler {
   @Override
   public ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3) {
      return ProjectVersionHandler.PreLoadingResult.AUTOMATIC;
   }

   @Override
   public boolean postLoading(IntegrationProject var1, ProjectFile var2) {
      ManagedProject var3 = var1.getExtensionObject(ManagedProject.class);
      MappingStructureCacheCleaner var4 = new MappingStructureCacheCleaner(var3);
      var4.run();
      return false;
   }
}
