package org.etlforge.integration.data.entity;

import java.util.Iterator;
import org.etlforge.integration.data.entity.id.RootFolderId;

public final class RootFolder extends Folder {
   public RootFolder(EntityType var1) {
      this(var1.getPlural(), var1);
   }

   public RootFolder(String var1, EntityType var2) {
      super(var2.getRootFolderId(), var1, var2);
      this.setEntityType(EntityType.RootFolder);
      this.setDeployDirty(false);
      this.setDeployed(true);
      this.protect();
   }

   @Override
   public RootFolder getRootFolder() {
      return this;
   }

   public RootFolderId getID() {
      return (RootFolderId)super.getID();
   }

   @Override
   public void generateNewId() {
      throw new UnsupportedOperationException("RootFolders have fixed IDs in the current version.");
   }

   public Folder getEtlforgeConnectRoot() {
      for (Folder var2 : this.getAllSubFolders()) {
         if (var2.isEtlforgeConnectFolder()) {
            return var2;
         }
      }

      return null;
   }

   public Folder matchPathByName(EntityPath var1) {
      Iterator var2 = var1.iterator();
      IntegrationEntity var3 = (IntegrationEntity)var2.next();
      if (!(var3 instanceof RootFolder)) {
         return null;
      }

      Folder var4 = this;

      while (var2.hasNext()) {
         IntegrationEntity var5 = (IntegrationEntity)var2.next();
         if (!(var5 instanceof Folder)) {
            break;
         }

         var4 = var4.findSubFolder(var5.getName());
         if (var4 == null) {
            break;
         }
      }

      return var4;
   }
}
