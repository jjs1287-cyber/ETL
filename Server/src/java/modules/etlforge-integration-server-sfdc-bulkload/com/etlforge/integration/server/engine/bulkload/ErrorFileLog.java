package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.etlforge.integration.server.BackendEncoding;
import org.etlforge.io.KongaIoUtils;

public final class ErrorFileLog implements ErrorLog {
   private final File file;
   private final Object lock = new Object();
   private int numberOfEntries;
   private volatile int maxEntries = Integer.MAX_VALUE;
   private BufferedWriter writer;
   private boolean full;
   private Logger logger;

   public ErrorFileLog(BulkLoadParams var1) {
      this(var1.getParentFile());
   }

   public ErrorFileLog(File var1) {
      this.file = new File(var1, UUID.randomUUID() + ".log");
   }

   public void setLogger(Logger var1) {
      synchronized (this.lock) {
         this.logger = var1;
      }
   }

   public void setMaxEntries(int var1) {
      Preconditions.checkArgument(var1 >= 0, "maxEntries must be >= 0 (was " + var1 + ")");
      this.maxEntries = var1;
   }

   @Override
   public void writeError(String var1, Exception var2) {
      synchronized (this.lock) {
         if (!this.full) {
            if (this.maxEntries == 0) {
               this.full = true;
               this.writeToFile("Error logging has been suppressed.", null);
            } else if (this.numberOfEntries == this.maxEntries) {
               this.full = true;
               this.writeToFile("The maximum number of errors to log (" + this.maxEntries + ") has been reached.", null);
            } else {
               this.numberOfEntries++;
               this.writeToFile(var1, var2);
            }
         }
      }
   }

   private void writeToFile(String var1, Exception var2) {
      String var3 = ExceptionUtils.getFullStackTrace(var2);
      if (this.logger != null) {
         this.logger.severe(var1 + "\n" + var3);
      }

      if (this.open()) {
         try {
            this.writer.write(var1);
            this.writer.newLine();
            if (var2 != null) {
               this.writer.write(var3);
               this.writer.newLine();
            }
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }
   }

   private boolean open() {
      if (this.writer == null) {
         try {
            this.writer = new BufferedWriter(BackendEncoding.openFileForWriting(this.file));
         } catch (IOException var2) {
            var2.printStackTrace();
            return false;
         }
      }

      return true;
   }

   public synchronized boolean isEmpty() {
      synchronized (this.lock) {
         return this.writer == null;
      }
   }

   @Override
   public synchronized void close() {
      synchronized (this.lock) {
         KongaIoUtils.close(this.writer);
         this.writer = null;
      }
   }

   public synchronized String getFilePath() {
      return this.file.getAbsolutePath();
   }
}
