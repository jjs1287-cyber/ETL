package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.EnumSet;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.location.DataLocationType;

public abstract class AbstractNewLocatableCallback extends UiEntityFactoryCallback.Adapter {
   private final Function<Collection<DataLocationType>, DataLocationType> typeSelector;

   public AbstractNewLocatableCallback(Function<Collection<DataLocationType>, DataLocationType> var1) {
      this.typeSelector = Preconditions.checkNotNull(var1);
   }

   @Override
   public final Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      LocatableFolders var3 = this.getTypeFolders(var2);
      if (var3 == null) {
         return Result.continueProcess(var2);
      }

      DataLocationType var4 = this.configureLocationType(var1, var2, var3);
      if (var4 == null) {
         return Result.cancel();
      }

      Folder var5 = var3.findFolderForType(var4, var2);
      return Result.continueProcess(var5);
   }

   private LocatableFolders getTypeFolders(Folder var1) {
      RootFolder var2 = var1.getRootFolder();
      return var2 == null ? null : this.createTypeFolders(var2);
   }

   protected abstract LocatableFolders createTypeFolders(RootFolder var1);

   private DataLocationType configureLocationType(IntegrationEntity var1, Folder var2, LocatableFolders var3) {
      DataLocationType var4 = this.selectType(var1, var2, var3);
      if (var4 == null) {
         return null;
      }

      Locatable var5 = (Locatable)var1;
      var5.setLocation(var4.createLocation());
      var5.setAllowedLocationTypes(EnumSet.of(var4));
      return var4;
   }

   private DataLocationType selectType(IntegrationEntity var1, Folder var2, LocatableFolders var3) {
      EnumSet var4 = var3.getAllowedTypesFor(var2);
      switch (var4.size()) {
         case 0:
            throw new RuntimeException("Configuration error: No matching location type for folder " + var2);
         case 1:
            return (DataLocationType)var4.iterator().next();
         default:
            return this.typeSelector.apply(var4);
      }
   }
}
