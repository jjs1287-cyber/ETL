package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.location.DataLocationType;

public final class FileBasedLocatableBuilder {
   public Folder configure(Locatable var1, Folder var2) {
      this.configureItem(var1);
      return this.findFolder(var2);
   }

   private void configureItem(Locatable var1) {
      var1.setAllowedLocationTypes(DataLocationType.getFileBasedTypes());
   }

   private Folder findFolder(Folder var1) {
      RootFolder var2 = var1.getRootFolder();
      if (var2 == null) {
         return var1;
      }

      LocatableFolders var3 = new LocatableFolders(var2);
      return var3.findFolderForFileBasedLocation(var1);
   }
}
