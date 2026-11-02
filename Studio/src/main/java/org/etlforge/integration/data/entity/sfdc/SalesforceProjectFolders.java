package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.ETLForgeConnectNames;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SalesforceProjectFolders {
   private final IntegrationProject project;
   private final String typeName;

   public SalesforceProjectFolders(IntegrationProject var1, EntityType var2) {
      this.project = var1;
      this.typeName = getTypeName(var2);
   }

   private static String getTypeName(EntityType var0) {
      return var0.getPlural();
   }

   public Folder findWizardsRoot(EntityType var1) {
      return this.project.getRootFolder(var1).findSubFolder("ETLForge Connect\u2122");
   }

   public Folder createWizardsRoot(EntityType var1) {
      return new Folder("ETLForge Connect\u2122", var1);
   }

   public Folder findWizardTypeRoot(EntityType var1) {
      Folder var2 = this.findWizardsRoot(var1);
      return var2 == null ? null : var2.findSubFolder(this.typeName);
   }

   public Folder createWizardTypeRoot(EntityType var1) {
      return new Folder(this.typeName, var1);
   }

   public Folder findSharedItemsFolder(EntityType var1) {
      Folder var2 = this.findWizardsRoot(var1);
      if (var2 == null) {
         return null;
      }

      String var3 = ETLForgeConnectNames.getSharedSalesforceItemsFolderName(var1);
      return var2.findSubFolder(var3);
   }

   public static Folder createSharedItemsFolder(EntityType var0) {
      String var1 = ETLForgeConnectNames.getSharedSalesforceItemsFolderName(var0);
      return new Folder(var1, var0);
   }

   public static void correctETLForgeConnectFolderNamesInDownloadedProject(IntegrationProject var0) {
      for (RootFolder var2 : var0.getAllRootFolders()) {
         for (Folder var4 : var2.getAllSubFolders()) {
            if (isProbablyETLForgeConnectFolder(var4)) {
               String var5 = EntityUtils.getAvailableChildName(var2, "ETLForge Connect\u2122");
               var4.setName(var5);
            }
         }
      }
   }

   private static boolean isProbablyETLForgeConnectFolder(Folder var0) {
      String var1 = var0.getName();
      return var1.startsWith("ETLForge Connect") && !var1.equals("ETLForge Connect\u2122");
   }
}
