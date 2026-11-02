package org.etlforge.integration.client.connect.wizard;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;

public final class Folders {
   private final String wizardName;
   private final IntegrationProject project;
   private final List<Folder> createdFolders;

   public Folders(IntegrationProject var1, String var2) {
      this.project = var1;
      this.wizardName = var2;
      this.createdFolders = Lists.newArrayList();
   }

   public List<Folder> getCreatedFolders() {
      return this.createdFolders;
   }

   public Folder getOperationParent(boolean var1) {
      return this.getWizardFolder(EntityType.Operation, var1);
   }

   public Folder getTransformationParent(boolean var1) {
      return this.getWizardFolder(EntityType.Transformation, var1);
   }

   public Folder getWizardFolder(EntityType var1, boolean var2) {
      Folder var3 = this.getConnectRoot(var1, var2);
      if (var3 == null) {
         return null;
      }

      Folder var4 = var3.findSubFolder(this.wizardName);
      if (var4 == null && var2) {
         var4 = this.createAndInsertFolder(var1, var3, this.wizardName);
      }

      return var4;
   }

   private Folder createAndInsertFolder(EntityType var1, Folder var2, String var3) {
      Folder var4 = new Folder(var3, var1);
      this.createdFolders.add(var4);
      this.project.insertEntity(var4, var2);
      return var4;
   }

   private Folder getConnectRoot(EntityType var1, boolean var2) {
      RootFolder var3 = this.project.getRootFolder(var1);
      String var4 = "ETLForge Connect\u2122";
      Folder var5 = var3.findSubFolder(var4);
      if (var5 == null && var2) {
         var5 = this.createAndInsertFolder(var1, var3, var4);
      }

      return var5;
   }
}
