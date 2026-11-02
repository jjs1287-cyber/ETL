package com.etlforge.integration.client.project.share;

import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;

class FileStoreResourceHandler {
   private final FileStore from;
   private final FileStore to;

   public FileStoreResourceHandler(FileStore var1, FileStore var2) {
      this.from = var1;
      this.to = var2;
   }

   public void transfer(String var1) throws FileStoreException {
      FileStoreFile var2 = this.from.getFile(var1);
      this.transfer(var2, new FileStoreResourceHandler.RecursionGuard());
   }

   private void transfer(FileStoreFile var1, FileStoreResourceHandler.RecursionGuard var2) throws FileStoreException {
      if (var1 != null) {
         if (var2.isCyclicDependency(var1)) {
            return;
         }

         this.to.addFile(var1.getPhysicalRepresentation(), var1.getPath(), null);

         for (FileStoreFile var4 : this.from.getRequiredFiles(var1.getPath())) {
            this.to.addDependency(var1.getPath(), var4.getPath());
            this.transfer(var4, var2);
         }
      }
   }

   private static class RecursionGuard {
      private final Set<String> paths = new HashSet<>();

      private RecursionGuard() {
      }

      public boolean isCyclicDependency(FileStoreFile var1) {
         String var2 = var1.getPath();
         if (this.paths.contains(var2)) {
            return true;
         }

         this.paths.add(var2);
         return false;
      }
   }
}
