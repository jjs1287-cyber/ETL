package org.jitterbit.integration.project.upgrade.v_5_0_0_1;

import java.io.File;
import java.util.ArrayList;
import org.jitterbit.integration.client.entity.settings.sqlite.EntitySettingsUpgrade;
import org.jitterbit.integration.client.entity.settings.sqlite.FileResourcesUpgrade;
import org.jitterbit.integration.client.entity.settings.sqlite.SQLiteEntityResources;
import org.jitterbit.integration.client.project.ProjectFile;
import org.jitterbit.integration.client.project.ProjectFolders;
import org.jitterbit.integration.client.project.ProjectVersionHandler;
import org.jitterbit.integration.client.project.UpgradeStrategy;
import org.jitterbit.integration.data.project.IntegrationProject;
import org.jitterbit.integration.project.upgrade.DefaultPreLoader;
import org.jitterbit.io.Folder;

final class ResourceMigrator implements ProjectVersionHandler {
   private volatile Folder settingsFolder;

   @Override
   public ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3) {
      DefaultPreLoader var4 = new DefaultPreLoader(var1, var2, var3);
      var4.run();
      ProjectVersionHandler.PreLoadingResult var5 = var4.getResult();
      if (var5 == ProjectVersionHandler.PreLoadingResult.ALLOWED) {
         this.doMigration(var2.getParentFolder());
      }

      return var5;
   }

   private void doMigration(File var1) {
      this.settingsFolder = ProjectFolders.getSettingsFolder(var1).subFolder("Entity");
      SQLiteEntityResources var2 = SQLiteEntityResources.inFolder(this.settingsFolder);

      try {
         EntitySettingsUpgrade var3 = new EntitySettingsUpgrade(this.settingsFolder, var2);
         var3.runUpgrade();
         FileResourcesUpgrade var4 = new FileResourcesUpgrade(this.settingsFolder, var2);
         var4.runUpgrade();
      } finally {
         var2.close(new ArrayList<>());
      }
   }

   @Override
   public boolean postLoading(IntegrationProject var1, ProjectFile var2) {
      this.deleteOldFiles();
      return true;
   }

   private void deleteOldFiles() {
      if (this.settingsFolder != null) {
         for (Folder var4 : this.settingsFolder.listSubFolders()) {
            var4.delete();
         }
      }
   }
}
