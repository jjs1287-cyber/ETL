package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.application.appdir.ReadMeGenerator;
import org.etlforge.integration.client.server.DefaultServerDirectory;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInformationStore;
import org.etlforge.integration.common.server.ServerGuid;

public final class ServerDirectoryImpl extends ApplicationDirectoryBase implements ServerDirectory {
   private final DefaultServerDirectory delegate;

   public ServerDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "servers");
      this.delegate = new DefaultServerDirectory(var1) {
         @Override
         protected File getFolderForServer(ServerGuid var1) throws Exception {
            File var2 = super.getFolderForServer(var1);
            ReadMeGenerator.generate(var2);
            return var2;
         }
      };
   }

   @Override
   public synchronized ServerInformationStore getInformationStore() {
      return this.delegate.getInformationStore();
   }

   @Override
   public File getLocation(ServerGuid var1) {
      return this.delegate.getLocation(var1);
   }
}
