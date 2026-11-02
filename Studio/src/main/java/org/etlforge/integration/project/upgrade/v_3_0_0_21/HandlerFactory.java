package org.etlforge.integration.project.upgrade.v_3_0_0_21;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.ProjectVersionHandlerFactory;
import org.etlforge.util.version.Version;

public class HandlerFactory implements ProjectVersionHandlerFactory {
   @Override
   public ProjectVersionHandler getHandler() {
      return new Handler();
   }

   @Override
   public Version getLastVersion() {
      return VersionFactory.newVersion(3, 0, 0, 20);
   }

   @Override
   public boolean handlesAllEarlierVersions() {
      return false;
   }
}
