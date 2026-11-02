package org.etlforge.application.errorlog;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

public class ErrorLogFilePersistor implements ErrorLogPersistor {
   private final File parent;
   private final File file;
   private final Object writeLock;

   public ErrorLogFilePersistor(File var1) {
      this.parent = var1;
      this.file = new File(var1, "ErrorLog.log");
      this.writeLock = new Object();
   }

   public File getFile() {
      return this.file;
   }

   @Override
   public void persist(ErrorLogEntry var1) {
      synchronized (this.writeLock) {
         if (this.parentExists()) {
            this.writeToFile(var1);
         }
      }
   }

   private void writeToFile(ErrorLogEntry var1) {
      try {
         var1.report(Files.newWriterSupplier(this.file, Charsets.UTF_8, true));
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   private boolean parentExists() {
      return this.parent.exists() || this.parent.mkdirs();
   }
}
