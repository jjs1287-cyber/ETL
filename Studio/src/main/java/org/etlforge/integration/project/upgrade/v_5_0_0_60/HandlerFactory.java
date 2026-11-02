package org.etlforge.integration.project.upgrade.v_5_0_0_60;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.ProjectVersionHandlerFactory;
import org.etlforge.util.version.Version;

public final class HandlerFactory implements ProjectVersionHandlerFactory {
   private final boolean automatic = ClientInfo.isDataLoader();

   @Override
   public Version getLastVersion() {
      return VersionFactory.newVersion(5, 0, 0, 59);
   }

   @Override
   public boolean handlesAllEarlierVersions() {
      return false;
   }

   @Override
   public ProjectVersionHandler getHandler() {
      return new Handler(this.automatic);
   }
}
