package org.etlforge.integration.data.entity;

import java.io.Serializable;
import java.util.Comparator;

public final class EntityTypeAndNameSorter implements Comparator<IntegrationEntity>, Serializable {
   private boolean useItemTypeForFolders = true;

   public void setUseItemTypeForFolders(boolean var1) {
      this.useItemTypeForFolders = var1;
   }

   public int compare(IntegrationEntity var1, IntegrationEntity var2) {
      EntityType var3 = this.getType(var1);
      EntityType var4 = this.getType(var2);
      int var5 = var3.compareTo(var4);
      if (var5 == 0) {
         if (var1 instanceof RootFolder) {
            if (!(var2 instanceof RootFolder)) {
               var5 = -1;
            }
         } else if (var2 instanceof RootFolder) {
            var5 = 1;
         }

         if (var5 == 0) {
            var5 = var1.getName().compareToIgnoreCase(var2.getName());
         }
      }

      return var5;
   }

   private EntityType getType(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      if (this.useItemTypeForFolders && (var2 == EntityType.RootFolder || var2 == EntityType.Folder)) {
         var2 = ((Folder)var1).getItemType();
      }

      return var2;
   }
}
