package org.etlforge.integration.project.upgrade.v_3_6_0_14;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.UpgradeStrategy;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ETLForgeConnectNames;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.SalesforceUtils;
import org.etlforge.integration.data.entity.sfdc.SalesforceProjectFolders;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.version.Version;

final class FolderRenamer implements ProjectVersionHandler {
   private final String oldRootName = "Salesforce Wizards";
   private final String oldQueriesName = "Queries";
   private final String oldUpsertsName = "Upserts";
   private IntegrationProject project;
   private boolean changeDetected;

   @Override
   public ProjectVersionHandler.PreLoadingResult preLoading(String var1, ProjectFile var2, UpgradeStrategy var3) {
      return ProjectVersionHandler.PreLoadingResult.AUTOMATIC;
   }

   @Override
   public boolean postLoading(IntegrationProject var1, ProjectFile var2) {
      this.project = var1;
      this.changeDetected = false;
      if (this.isAffectedVersion(var2)) {
         for (RootFolder var4 : var1.getAllRootFolders()) {
            this.processRootFolder(var4);
         }
      }

      return this.changeDetected;
   }

   private boolean isAffectedVersion(ProjectFile var1) {
      Version var2 = var1.getVersionSavedWith();
      return var2.getMajorVersion() == 3 && var2.getMinorVersion() == 6;
   }

   private void processRootFolder(RootFolder var1) {
      for (Folder var3 : var1.getAllSubFolders()) {
         if ("Salesforce Wizards".equals(var3.getName())) {
            this.changeDetected = true;
            this.processWizardRoots(var3);
         }
      }
   }

   private void processWizardRoots(Folder var1) {
      this.setNewFolderName(var1, "ETLForge Connect\u2122");
      this.processWizardTypeRoots(var1);
      this.processSharedItems(var1);
   }

   private void processWizardTypeRoots(Folder var1) {
      for (Folder var3 : var1.getAllSubFolders()) {
         String var4 = var3.getName();
         if ("Queries".equals(var4)) {
            this.setNewFolderName(var3, ETLForgeConnectNames.QUERIES_ROOT_NAME);
         } else if ("Upserts".equals(var4)) {
            this.setNewFolderName(var3, ETLForgeConnectNames.UPSERTS_ROOT_NAME);
         }
      }
   }

   private void processSharedItems(Folder var1) {
      Folder var2 = null;
      EntityType var3 = var1.getItemType();

      for (IntegrationEntity var5 : var1.getAllItems()) {
         if (SalesforceUtils.isSalesforceWizardObject(var5)) {
            if (var2 == null) {
               var2 = SalesforceProjectFolders.createSharedItemsFolder(var3);
               this.project.insertEntity(var2, var1);
            }

            var5.setParent(null);
            this.project.insertEntity(var5, var2);
         }
      }
   }

   private void setNewFolderName(Folder var1, String var2) {
      try {
         var1.setName(var2);
      } catch (IllegalNameException var4) {
         ErrorLog.log(this.getClass(), null, var4);
      }
   }
}
