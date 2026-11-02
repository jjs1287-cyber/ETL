package org.etlforge.integration.client.project.backup;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.project.ProjectFolderCopier;
import org.etlforge.util.text.ThreadSafeDateFormat;

class BackupFolder {
   public static final boolean MULTITHREAD = ApplicationConfiguration.getBoolean("ProjectBackup.MultiThread", false);
   private static final ThreadSafeDateFormat DATE_FMT = new ThreadSafeDateFormat("yyyy-MM-dd");
   private static final ThreadSafeDateFormat TIME_FMT = new ThreadSafeDateFormat("HH.mm.ss");
   private final Date timestamp = new Date();
   private final ProjectFolderCopier copier;
   private final File folder;

   public BackupFolder(File var1, String var2, File var3) {
      this(var1, var2, var3, false);
   }

   public BackupFolder(File var1, String var2, File var3, boolean var4) {
      if (var4) {
         this.folder = new File(var1, getProjectFolderName(var2, this.timestamp));
      } else {
         File var5 = new File(var1, DATE_FMT.format(this.timestamp));
         this.folder = new File(var5, getProjectFolderName(var2, this.timestamp));
      }

      this.copier = new ProjectFolderCopier(var3, this.folder);
   }

   public void setMultiThread(boolean var1) {
      this.copier.setMultiThread(var1);
   }

   public File createBackup() throws IOException {
      return this.copier.doCopy();
   }

   public long getTimestamp() {
      return this.timestamp.getTime();
   }

   public File getLocation() {
      return this.folder;
   }

   private static String getProjectFolderName(String var0, Date var1) {
      return var0 + " [" + TIME_FMT.format(var1) + "]";
   }
}
