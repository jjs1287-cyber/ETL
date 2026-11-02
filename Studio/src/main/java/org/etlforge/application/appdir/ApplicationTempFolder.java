package org.etlforge.application.appdir;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.Application;
import org.etlforge.application.ExitListener;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.FileFilters;
import org.etlforge.io.Folder;
import org.etlforge.system.ArgCheck;

public final class ApplicationTempFolder {
   private static final Folder TEMP_DIR = new Folder(SystemUtils.JAVA_IO_TMPDIR, getTempFolderName());
   private static final ApplicationTempFolder.TempFolderDeleter tempFolderDeleter = new ApplicationTempFolder.TempFolderDeleter();
   private static final CopyOnWriteArrayList<FileFilter> deleteOnExit = new CopyOnWriteArrayList<>();

   private static String getTempFolderName() {
      String var0 = ApplicationConfiguration.getString("TempFolder.Name", "konga_temp_folder");
      if (SystemUtils.IS_OS_LINUX) {
         var0 = var0 + "-" + SystemUtils.USER_NAME;
      }

      return var0;
   }

   public static void replaceJavaIoTmpDir() {
      System.setProperty("java.io.tmpdir", TEMP_DIR.getAbsolutePath());
   }

   public static void deleteOnExit(FileFilter var0) {
      deleteOnExit.add(Preconditions.checkNotNull(var0));
   }

   public static Folder.Stats stats() {
      return TEMP_DIR.getStats();
   }

   public static boolean clean() {
      ApplicationTempFolder.Cleaner var0 = new ApplicationTempFolder.Cleaner();
      return var0.clean();
   }

   public static String getAbsolutePath() {
      return TEMP_DIR.getAbsolutePath();
   }

   public static String getCanonicalPath() throws IOException {
      return TEMP_DIR.getCanonicalPath();
   }

   public static boolean exists(String var0) {
      File var1 = TEMP_DIR.getFile(var0);
      return var1.exists();
   }

   public static File newFile(String var0) {
      createDirIfNeeded(TEMP_DIR.asJavaFile());
      return TEMP_DIR.getFile(var0);
   }

   public static File newFolder(String var0) {
      return newFolder(var0, false);
   }

   public static File newUniqueFolder(boolean var0) {
      return newFolder(UUID.randomUUID().toString(), var0);
   }

   public static File newFolderStructure(boolean var0, String... var1) {
      ArgCheck.checkNotEmpty(var1, "path");
      Folder var2 = null;
      Folder var3 = TEMP_DIR;

      for (String var7 : var1) {
         Folder var8 = var3.subFolder(var7);
         if (var2 == null) {
            var2 = var8;
            if (var0) {
               tempFolderDeleter.addFolder(var2.asJavaFile());
            }
         }

         var3 = var8;
      }

      var3.createOnDisk();
      return var3.asJavaFile();
   }

   public static File newFolder(String var0, boolean var1) {
      File var2 = TEMP_DIR.getFile(var0);
      createDirIfNeeded(var2);
      if (var1) {
         tempFolderDeleter.addFolder(var2);
      }

      return var2;
   }

   private static synchronized void createDirIfNeeded(File var0) {
      if (!var0.isDirectory()) {
         try {
            FileUtils.forceMkdir(var0);
         } catch (IOException var2) {
            ErrorLog.log(ApplicationTempFolder.class, "Failed to create the temporary directory " + var0.getAbsolutePath(), var2);
         }
      }
   }

   public static synchronized File newLikelyUniqueFolder(String var0, boolean var1) {
      String var2 = getAvailableName(var0);
      return newFolder(var2, var1);
   }

   private static String getAvailableName(String var0) {
      Set var1 = TEMP_DIR.getNamesOfFilesAndFolders();
      String var2 = var0;
      int var3 = 0;

      while (var1.contains(var2)) {
         var2 = var0 + ++var3;
      }

      return var2;
   }

   private ApplicationTempFolder() {
   }

   static {
      Application.addExitListener(tempFolderDeleter);
   }

   private static class Cleaner {
      private boolean success;

      private Cleaner() {
      }

      public boolean clean() {
         this.success = true;
         this.processFolder(ApplicationTempFolder.TEMP_DIR);
         return this.success;
      }

      private void processFolder(Folder var1) {
         for (File var5 : var1.listFiles()) {
            if (var5.isDirectory()) {
               Folder var6 = new Folder(var5);
               this.processFolder(var6);
               if (var6.isEmpty() && !var6.delete()) {
                  this.success = false;
               }
            } else if (!var5.delete()) {
               this.success = false;
            }
         }
      }
   }

   private static final class TempFolderDeleter implements ExitListener {
      private final Set<File> toDelete = Sets.newCopyOnWriteArraySet();

      private TempFolderDeleter() {
      }

      public void addFolder(File var1) {
         this.toDelete.add(var1);
      }

      @Override
      public boolean isExitAllowed() {
         return true;
      }

      @Override
      public void performExitWork() {
         this.deleteFolders();
         this.deleteFiles();
      }

      private synchronized void deleteFolders() {
         for (File var2 : this.toDelete) {
            try {
               FileUtils.deleteDirectory(var2);
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

      private void deleteFiles() {
         FileFilter var1 = FileFilters.or(ApplicationTempFolder.deleteOnExit);
         File[] var2 = ApplicationTempFolder.TEMP_DIR.listFiles(var1);

         for (File var6 : var2) {
            var6.delete();
         }
      }
   }
}
