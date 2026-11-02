package org.etlforge.integration.client.project.copy;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.permission.PermissionsStorageException;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public final class EntityCopyMachine {
   public IntegrationEntity createAndInsertCopy(IntegrationEntity var1) {
      IntegrationProjectLock.acquire();

      try {
         IntegrationEntity var2 = var1.getParent();
         if (var2 == null) {
            throw new IllegalStateException("The entity to be copied has no parent.");
         }

         IntegrationProject var3 = this.getProject(var2);
         if (var3 == null) {
            throw new IllegalStateException("The entity does not belong to a project.");
         }

         IntegrationEntity var4 = var1.cloneWithNewID();
         var4.setParent(var2);
         this.setNewName(var4);
         this.copyPermissions(var1, var4, var3);
         this.copyPluginSettings(var1, var4, var3);
         this.doSpecialHandling(var1, var4, var3);
         var3.insertEntity(var4, var2);
         return var4;
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private IntegrationProject getProject(IntegrationEntity var1) {
      IntegrationEntity var2 = new EntityPath(var1).root();
      return var2 instanceof IntegrationProject ? (IntegrationProject)var2 : null;
   }

   private void setNewName(IntegrationEntity var1) {
      int var2 = 2;
      String var3 = var1.getName();

      String var4;
      for (var4 = "Copy of " + var3; !EntityUtils.isNameUnique(var1, var4); var2++) {
         var4 = "Copy (" + var2 + ") of " + var3;
      }

      var1.setName(var4);
   }

   private void copyPermissions(IntegrationEntity var1, IntegrationEntity var2, IntegrationProject var3) {
      try {
         ManagedProject var4 = var3.getExtensionObject(ManagedProject.class);
         PermissionsManager var5 = var4.getPermissions();
         var5.copyPermissions(var1, var2);
         var5.save();
      } catch (PermissionsStorageException var6) {
         ErrorLog.attention(this.getClass(), "The permission settings could not be copied.", var6);
      }
   }

   private void copyPluginSettings(IntegrationEntity var1, IntegrationEntity var2, IntegrationProject var3) {
      try {
         ManagedProject var4 = var3.getExtensionObject(ManagedProject.class);
         ProjectPluginInfo var5 = var4.getPipelinePlugins();
         var5.getAppliedPlugins().copy(var1, var2);
         var4.savePluginInfo();
      } catch (InterchangeSavingException var6) {
         ErrorLog.attention(this.getClass(), "The plugin settings could not be copied.", var6);
      }
   }

   private void doSpecialHandling(IntegrationEntity var1, IntegrationEntity var2, IntegrationProject var3) {
      AdditionalCopyInstructions var4 = var1.getExtensionObject(AdditionalCopyInstructions.class);
      if (var4 != null) {
         var4.handle(var2, ProjectUtils.getManagedProject(var3));
      }
   }
}
