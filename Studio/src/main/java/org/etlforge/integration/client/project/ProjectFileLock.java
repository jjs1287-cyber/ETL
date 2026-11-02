package org.etlforge.integration.client.project;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.etlforge.util.LockingFile;

public final class ProjectFileLock {
   private static final String FILE_NAME = ".project.lock";
   private final Map<File, LockingFile> lockFiles = Maps.newIdentityHashMap();

   public synchronized boolean acquire(File var1) {
      LockingFile var2 = new LockingFile(new File(var1, ".project.lock"));
      if (var2.acquire()) {
         this.lockFiles.put(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public synchronized boolean isLocked(File var1) {
      LockingFile var2 = new LockingFile(new File(var1, ".project.lock"));

      try {
         return !var2.acquire();
      } finally {
         var2.release();
      }
   }

   public synchronized void release(ManagedProject var1) {
      this.release(var1.getLocation());
   }

   public synchronized void release(File var1) {
      LockingFile var2 = this.lockFiles.remove(var1);
      if (var2 != null) {
         var2.release();
      }
   }
}
