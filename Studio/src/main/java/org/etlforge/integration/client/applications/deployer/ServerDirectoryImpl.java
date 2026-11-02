package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInformationStore;
import org.etlforge.integration.common.server.ServerGuid;

public final class ServerDirectoryImpl extends ApplicationDirectoryBase implements ServerDirectory {
   public ServerDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "servers");
   }

   @Override
   public File getLocation(ServerGuid var1) {
      return new File(this.getLocation(), var1.toString());
   }

   @Override
   public ServerInformationStore getInformationStore() {
      return ServerInformationStore.EMPTY;
   }
}
