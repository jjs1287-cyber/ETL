package org.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.location.DataLocationType;

public final class LocatableBuilder {
   private final DataLocationType type;

   public LocatableBuilder(DataLocationType var1) {
      this.type = Preconditions.checkNotNull(var1);
   }

   public Folder configure(Locatable var1, Folder var2) {
      this.configureItem(var1);
      return this.findFolder(var1, var2);
   }

   private void configureItem(Locatable var1) {
      var1.setLocation(this.type.createLocation());
      var1.setAllowedLocationTypes(EnumSet.of(this.type));
   }

   private Folder findFolder(Locatable var1, Folder var2) {
      RootFolder var3 = var2.getRootFolder();
      if (var3 == null) {
         return var2;
      }

      LocatableFolders var4 = new LocatableFolders(var3);
      return var4.findFolderForNewItem(var1, var2);
   }
}
