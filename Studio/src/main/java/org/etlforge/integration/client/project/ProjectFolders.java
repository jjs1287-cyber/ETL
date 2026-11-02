package org.etlforge.integration.client.project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.io.Folder;

public final class ProjectFolders {
   private static final String PROJECT_DATA = "Data";
   private static final String FILESTORE_ROOT = "root";
   private static final String FILESTORE = "filestore";
   public static final String TEST_DATA_ROOT = "test";
   static final String CACHE = "cache";
   public static final String SESSION_CACHE = "session";
   private static final String PERMISSIONS = "Permissions";
   private static final String SETTINGS = "settings";
   private static final String PERSIST = "persist";
   private static final Set<String> FOLDER_NAMES = new HashSet<>();

   public static Folder getProjectDataFolder(File var0) {
      return new Folder(var0, "Data");
   }

   public static File getFileStoreFolder(File var0) {
      return new File(var0, "filestore");
   }

   public static File getFileStoreRootFolder(File var0) {
      return new File(getFileStoreFolder(var0), "root");
   }

   public static Folder getCacheFolder(File var0) {
      return new Folder(var0, "cache");
   }

   public static Folder getPermissionsFolder(File var0) {
      return new Folder(var0, "Permissions");
   }

   public static Folder getSettingsFolder(File var0) {
      return new Folder(var0, "settings");
   }

   public static Folder getPersistFolder(File var0) {
      return new Folder(var0, "persist");
   }

   public static Folder getTestDataFolder(File var0) {
      File var1 = getFileStoreFolder(var0);
      return new Folder(var1, "test");
   }

   static Folder getTestHistoryFolder(File var0) {
      return getPersistFolder(var0);
   }

   static boolean isProjectFolder(File var0) {
      return var0.isDirectory() && FOLDER_NAMES.contains(var0.getName());
   }

   public static Folder getSessionCacheRoot(File var0) {
      return new Folder(var0, "cache", "session");
   }

   private ProjectFolders() {
   }

   static {
      FOLDER_NAMES.add("Data");
      FOLDER_NAMES.add("filestore");
      FOLDER_NAMES.add("cache");
      FOLDER_NAMES.add("Permissions");
      FOLDER_NAMES.add("settings");
      FOLDER_NAMES.add("persist");
   }
}
