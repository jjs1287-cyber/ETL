package org.etlforge.integration.data.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;

final class FolderChildFilter implements EntityFilter {
   private final EntityType folderType;
   private final EntityFilter itemFilter;
   private final FolderChildFilter.SubFolderFilter subFolderFilter;

   public FolderChildFilter(Folder var1) {
      this(var1.getItemType(), getItemFilterFromFolder(var1));
   }

   private static EntityFilter getItemFilterFromFolder(Folder var0) {
      Optional var1 = var0.getItemFilter();
      return var1.isPresent() ? (EntityFilter)var1.get() : EntityFilters.ACCEPTS_ALL;
   }

   @VisibleForTesting
   FolderChildFilter(EntityType var1, EntityFilter var2) {
      this.folderType = var1;
      this.itemFilter = var2;
      this.subFolderFilter = new FolderChildFilter.SubFolderFilter(var1, var2);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Folder ? this.subFolderFilter.apply(var1) : this.checkItem(var1);
   }

   private boolean checkItem(IntegrationEntity var1) {
      return var1.getEntityType() == this.folderType && this.itemFilter.apply(var1);
   }

   private static class SubFolderFilter implements EntityFilter {
      private final EntityType folderType;
      private final EntityFilter itemFilter;

      public SubFolderFilter(EntityType var1, EntityFilter var2) {
         this.folderType = var1;
         this.itemFilter = var2;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         Folder var2 = (Folder)var1;
         if (var2.getItemType() != this.folderType) {
            return false;
         }

         Optional var3 = var2.getItemFilter();
         return var3.isPresent() ? this.itemFilter.equals(var3.get()) : true;
      }
   }
}
