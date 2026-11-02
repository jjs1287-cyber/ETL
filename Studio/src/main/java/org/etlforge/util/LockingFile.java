package org.etlforge.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public final class LockingFile {
   private final File file;
   private FileOutputStream lockFileStream;
   private Logger logger;
   private final Object syncLock;

   public LockingFile(File var1) {
      if (var1.isDirectory()) {
         throw new IllegalArgumentException("file cannot represent a directory");
      }

      this.file = var1;
      this.syncLock = new Object();
   }

   public void setLogger(Logger var1) {
      synchronized (this.syncLock) {
         this.logger = var1;
      }
   }

   public boolean acquire() {
      synchronized (this.syncLock) {
         if (this.lockFileStream != null) {
            return true;
         } else {
            return this.file.exists() ? this.handleExistingFile() : this.createAndOpenLockFile();
         }
      }
   }

   private boolean handleExistingFile() {
      return this.file.delete() && this.createAndOpenLockFile();
   }

   private boolean createAndOpenLockFile() {
      String var1 = "createAndOpenLockFile";

      try {
         this.file.createNewFile();
         this.lockFileStream = new FileOutputStream(this.file);
         this.addShutdownHook();
         return true;
      } catch (FileNotFoundException var3) {
         this.logException(var1, var3);
      } catch (IOException var4) {
         this.logException(var1, var4);
      }

      return false;
   }

   private void addShutdownHook() {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            LockingFile.this.release();
         }
      });
   }

   private void logException(String var1, Exception var2) {
      var2.printStackTrace();
      if (this.logger != null) {
         this.logger.throwing(this.getClass().getName(), var1, var2);
      }
   }

   public void release() {
      synchronized (this.syncLock) {
         if (this.lockFileStream != null) {
            try {
               this.lockFileStream.close();
               this.file.delete();
               this.lockFileStream = null;
            } catch (IOException var4) {
               this.logException("release", var4);
            }
         }
      }
   }

   @Override
   public String toString() {
      return this.file.getAbsolutePath();
   }
}
