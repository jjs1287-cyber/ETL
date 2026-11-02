package org.etlforge.application.appdir;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.etlforge.application.log.LogFileRepository;

final class LogFileRepositoryImpl implements LogFileRepository {
   private final LogFileRepositoryImpl.RepositoryFolder[] folders;

   public LogFileRepositoryImpl(File var1) {
      List var2 = this.collectFolders(var1);
      this.folders = (LogFileRepositoryImpl.RepositoryFolder[]) var2.toArray(new LogFileRepositoryImpl.RepositoryFolder[var2.size()]);
   }

   private List<LogFileRepositoryImpl.RepositoryFolder> collectFolders(File var1) {
      org.etlforge.io.Folder var2 = new org.etlforge.io.Folder(var1);
      ArrayList var3 = Lists.newArrayList();

      for (org.etlforge.io.Folder var7 : var2.listSubFolders()) {
         try {
            var3.add(new LogFileRepositoryImpl.RepositoryFolder(var7));
         } catch (NumberFormatException var9) {
         }
      }

      Collections.sort(var3, new LogFileRepositoryImpl.FolderOrder());
      return var3;
   }

   @Override
   public LogFileRepository.Folder[] getSessionLogs() {
      return (LogFileRepository.Folder[])this.folders.clone();
   }

   @Override
   public boolean isSystemErr(File var1) {
      return var1.getName().equals("system.err");
   }

   @Override
   public boolean isSystemOut(File var1) {
      return var1.getName().equals("system.out");
   }

   private static class FolderOrder implements Comparator<LogFileRepositoryImpl.RepositoryFolder>, Serializable {
      private FolderOrder() {
      }

      public int compare(LogFileRepositoryImpl.RepositoryFolder var1, LogFileRepositoryImpl.RepositoryFolder var2) {
         return -var1.date.compareTo(var2.date);
      }
   }

   private static class RepositoryFolder implements LogFileRepository.Folder {
      private static final String DISPLAY_NAME_FORMAT = "yyyy-MM-dd HH:mm:ss";
      private final org.etlforge.io.Folder location;
      public final Date date;
      private final String name;

      public RepositoryFolder(org.etlforge.io.Folder var1) throws NumberFormatException {
         this.location = var1;
         this.date = new Date(Long.parseLong(var1.getName()));
         SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         this.name = var2.format(this.date);
      }

      @Override
      public Date getSessionDate() {
         return new Date(this.date.getTime());
      }

      @Override
      public File[] getLogFiles() {
         File[] var1 = this.location.listFiles(LogDirectoryImpl.LOG_FILE_FILTER);
         Arrays.sort(var1);
         return var1;
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public File getSystemErr() {
         return this.location.getFile("system.err");
      }

      @Override
      public File getSystemOut() {
         return this.location.getFile("system.out");
      }
   }
}
