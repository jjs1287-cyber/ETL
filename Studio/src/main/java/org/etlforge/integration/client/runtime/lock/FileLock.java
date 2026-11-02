package org.etlforge.integration.client.runtime.lock;

import java.io.File;
import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.util.LockingFile;

final class FileLock implements ApplicationInstanceLock {
   private static final String FILE_NAME = "konga_integration.lock";
   private static final Logger logger = ApplicationLog.getMainLogger();
   private final File folder;

   public FileLock(File var1) {
      this.folder = var1;
   }

   @Override
   public boolean lock(String[] var1) {
      File var2 = new File(this.folder, "konga_integration.lock");
      LockingFile var3 = new LockingFile(var2);
      var3.setLogger(logger);
      return var3.acquire();
   }
}
