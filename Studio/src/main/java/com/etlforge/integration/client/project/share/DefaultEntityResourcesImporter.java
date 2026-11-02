package com.etlforge.integration.client.project.share;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.permission.PermissionsStorageException;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.ArgCheck;

public final class DefaultEntityResourcesImporter implements EntityResourcesImporter {
   private final IntegrationEntity entity;
   private ManagedProject sourceProject;
   private ManagedProject targetProject;

   @ExtensionObjectConstructor
   public DefaultEntityResourcesImporter(IntegrationEntity var1) {
      this.entity = var1;
   }

   @Override
   public void transferResources(ManagedProject var1, ManagedProject var2) {
      this.sourceProject = var1;
      this.targetProject = var2;
      this.transferPermissions();
      this.transferPluginInfo();
      this.transferSettings();
   }

   private void transferPermissions() {
      try {
         PermissionsManager var1 = this.sourceProject.getPermissions();
         PermissionsManager var2 = this.targetProject.getPermissions();
         var1.exportPermissions(this.entity, var2);
      } catch (PermissionsStorageException var3) {
         var3.printStackTrace();
      }
   }

   private void transferSettings() {
      EntityResources var1 = this.sourceProject.getEntityResources();
      EntityResources var2 = this.targetProject.getEntityResources();
      var1.export(this.entity, var2);
   }

   private void transferPluginInfo() {
      PipelinePluginImporter var1 = new PipelinePluginImporter(this.sourceProject, this.targetProject);
      var1.transfer(this.entity);
   }

   void transferFileStoreFile(String var1) {
      ArgCheck.checkNotEmpty(var1);

      try {
         FileStoreResourceHandler var2 = new FileStoreResourceHandler(this.sourceProject.getFileStore(), this.targetProject.getFileStore());
         var2.transfer(var1);
      } catch (FileStoreException var3) {
         var3.printStackTrace();
      }
   }
}
