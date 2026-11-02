package com.etlforge.integration.client.sfdc.project.upgrade.v29;

import org.etlforge.application.VersionFactory;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.ProjectVersionHandlerFactory;
import org.etlforge.util.version.Version;

public final class HandlerFactory implements ProjectVersionHandlerFactory {
   @Override
   public Version getLastVersion() {
      return VersionFactory.newVersion(5, 3, 9999, 999999);
   }

   @Override
   public boolean handlesAllEarlierVersions() {
      return false;
   }

   @Override
   public ProjectVersionHandler getHandler() {
      V29Upgrade var1 = new V29Upgrade();
      var1.setLogger(ApplicationLog.getMainLogger());
      return var1;
   }
}
