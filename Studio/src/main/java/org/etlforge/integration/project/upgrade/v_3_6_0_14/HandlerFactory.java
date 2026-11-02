package org.etlforge.integration.project.upgrade.v_3_6_0_14;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.ProjectVersionHandlerFactory;
import org.etlforge.util.version.Version;

public class HandlerFactory implements ProjectVersionHandlerFactory {
   @Override
   public Version getLastVersion() {
      return VersionFactory.newVersion(3, 6, 0, 13);
   }

   @Override
   public boolean handlesAllEarlierVersions() {
      return false;
   }

   @Override
   public ProjectVersionHandler getHandler() {
      return new FolderRenamer();
   }
}
