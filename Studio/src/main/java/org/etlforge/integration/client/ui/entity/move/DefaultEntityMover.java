package org.etlforge.integration.client.ui.entity.move;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.ReferenceList;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DefaultEntityMover implements EntityMover {
   private ConflictResolver conflictResolver = ConflictResolver.DO_NOT_MOVE;
   private ReferenceList<IntegrationEntity> itemsToMove;

   @Override
   public void setItemsToMove(List<? extends IntegrationEntity> var1) {
      this.itemsToMove = ReferenceList.weakReferences();
      if (areMoveable(var1)) {
         this.itemsToMove.addAll(var1);
      }
   }

   private static boolean areMoveable(List<? extends IntegrationEntity> var0) {
      if (var0 == null || var0.isEmpty()) {
         return false;
      } else {
         return containsBothItemsAndFolders(var0) ? false : !containsItemsThatCannotBeMoved(var0);
      }
   }

   private static boolean containsBothItemsAndFolders(List<? extends IntegrationEntity> var0) {
      boolean var1 = false;
      boolean var2 = false;

      for (IntegrationEntity var4 : var0) {
         if (var4 instanceof Folder) {
            var2 = true;
         } else {
            var1 = true;
         }

         if (var2 && var1) {
            return true;
         }
      }

      return false;
   }

   private static boolean containsItemsThatCannotBeMoved(List<? extends IntegrationEntity> var0) {
      for (IntegrationEntity var2 : var0) {
         if (!var2.isMoveable()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public List<IntegrationEntity> getItemsToMove() {
      return this.itemsToMove.getReferents();
   }

   @Override
   public boolean isAllowedTarget(IntegrationEntity var1) {
      this.checkItemsHaveBeenSet();
      if (this.itemsToMove.isEmpty()) {
         return false;
      } else {
         return var1 instanceof Folder ? this.checkFolderTarget((Folder)var1) : false;
      }
   }

   private void checkItemsHaveBeenSet() {
      Preconditions.checkState(this.itemsToMove != null, "setItemsToMove must be called first");
   }

   private boolean checkFolderTarget(Folder var1) {
      for (IntegrationEntity var3 : this.itemsToMove) {
         if (!var1.isAllowedChild(var3)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public List<IntegrationEntity> executeMove(IntegrationEntity var1) {
      this.checkItemsHaveBeenSet();
      Preconditions.checkArgument(this.isAllowedTarget(var1), "The items cannot be moved to the given target");
      ArrayList var2 = Lists.newArrayList();

      for (IntegrationEntity var4 : this.itemsToMove) {
         if (this.move(var4, var1)) {
            var2.add(var4);
         }
      }

      this.itemsToMove = null;
      return var2;
   }

   private boolean move(IntegrationEntity var1, IntegrationEntity var2) {
      if (var2 == var1.getParent()) {
         return true;
      } else {
         IntegrationEntity var3 = this.findDuplicate(var1, var2);
         if (var3 == null) {
            var1.setParent(var2);
            return true;
         } else {
            return this.conflictResolver.resolve(var1, var2, var3);
         }
      }
   }

   private IntegrationEntity findDuplicate(IntegrationEntity var1, IntegrationEntity var2) {
      String var3 = var1.getName();

      for (IntegrationEntity var5 : var2.getAllChildren()) {
         if (var3.equals(var5.getName())) {
            return var5;
         }
      }

      return null;
   }

   public void setConflictResolver(ConflictResolver var1) {
      this.conflictResolver = var1;
   }
}
