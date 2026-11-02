package org.etlforge.integration.client.project.jitterpack;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.io.Folder;

public final class DefaultPackingFileFilter implements PackingFileFilter {
   private final File persistFolder;
   private final File fileStoreFolder;
   private final File fileStoreRootFolder;
   private final File testDataFolder;
   private final File projectCacheFolder;
   private final File sessionCacheFolder;
   private final boolean includeTestData;
   private final List<String> filteredSettingsPaths;
   private final String transformationResourcesPath;

   public DefaultPackingFileFilter(File var1, boolean var2) {
      this.includeTestData = var2;
      this.fileStoreFolder = ProjectFolders.getFileStoreFolder(var1);
      this.fileStoreRootFolder = ProjectFolders.getFileStoreRootFolder(var1);
      this.projectCacheFolder = ProjectFolders.getCacheFolder(var1).asJavaFile();
      this.sessionCacheFolder = ProjectFolders.getSessionCacheRoot(var1).asJavaFile();
      this.persistFolder = ProjectFolders.getPersistFolder(var1).asJavaFile();
      this.testDataFolder = ProjectFolders.getTestDataFolder(var1).asJavaFile();
      this.filteredSettingsPaths = this.createFilteredSettingsPaths(var1, var2);
      this.transformationResourcesPath = this.filteredSettingsPaths.isEmpty() ? null : this.filteredSettingsPaths.get(this.filteredSettingsPaths.size() - 1);
   }

   private List<String> createFilteredSettingsPaths(File var1, boolean var2) {
      ArrayList var3 = Lists.newArrayList();
      if (!var2) {
         Folder var4 = ProjectFolders.getSettingsFolder(var1);
         var3.add(var4.getAbsolutePath());

         for (String var8 : new String[]{"Entity", "Transformation", "Resources"}) {
            var4 = var4.subFolder(var8);
            var3.add(var4.getAbsolutePath());
         }
      }

      return var3;
   }

   @Override
   public PackingFileFilter.Result evaluateDirectory(File var1) {
      if (var1.equals(this.persistFolder)) {
         return EXCLUDE;
      }

      if (var1.equals(this.fileStoreFolder)) {
         return this.includeTestData ? INCLUDE : INCLUDE_SELECTIVELY;
      }

      if (var1.equals(this.projectCacheFolder)) {
         return INCLUDE_SELECTIVELY;
      }

      if (var1.equals(this.sessionCacheFolder)) {
         return EXCLUDE;
      }

      if (var1.equals(this.fileStoreRootFolder)) {
         return this.includeTestData ? INCLUDE : INCLUDE_SELECTIVELY;
      }

      if (var1.equals(this.testDataFolder)) {
         return this.includeTestData ? INCLUDE : EXCLUDE;
      }

      String var2 = var1.getAbsolutePath();

      for (String var4 : this.filteredSettingsPaths) {
         if (var2.equals(var4)) {
            return this.includeTestData ? INCLUDE : INCLUDE_SELECTIVELY;
         }
      }

      if (!this.includeTestData && var2.startsWith(this.transformationResourcesPath)) {
         return INCLUDE_SELECTIVELY;
      }

      File var5 = var1.getParentFile();
      return this.projectCacheFolder.equals(var5) ? INCLUDE : INCLUDE;
   }

   @Override
   public PackingFileFilter.Result evaluateFile(File var1) {
      return this.transformationResourcesPath != null
            && var1.getAbsolutePath().startsWith(this.transformationResourcesPath)
            && var1.getName().startsWith("DataFile.")
         ? PackingFileFilter.Result.EXCLUDE
         : PackingFileFilter.Result.INCLUDE;
   }
}
