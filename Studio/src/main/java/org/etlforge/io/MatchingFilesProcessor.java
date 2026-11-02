package org.etlforge.io;

import java.io.File;
import java.io.FileFilter;
import org.etlforge.system.callback.Receiver;

public final class MatchingFilesProcessor {
   private final FileFilter filter;
   private final Receiver<File> worker;

   public MatchingFilesProcessor(Receiver<File> var1) {
      this(FileUtils.ALL_FILES, var1);
   }

   public MatchingFilesProcessor(FileFilter var1, Receiver<File> var2) {
      this.filter = var1;
      this.worker = var2;
   }

   public void process(Folder var1) {
      File[] var2 = var1.listFiles(this.filter);

      for (File var6 : var2) {
         this.worker.handle(var6);
      }
   }

   public void processRecursively(Folder var1) {
      this.process(var1);

      for (Folder var5 : var1.listSubFolders()) {
         this.processRecursively(var5);
      }
   }
}
