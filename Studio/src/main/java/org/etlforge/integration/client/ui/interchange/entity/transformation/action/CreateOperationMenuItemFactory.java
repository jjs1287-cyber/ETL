package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.entity.TransformationToOperationFactory;
import org.etlforge.integration.data.entity.Transformation;

public final class CreateOperationMenuItemFactory extends AbstractPopupMenuItemFactory {
   private final TransformationToOperationFactory transform;

   public CreateOperationMenuItemFactory(TransformationToOperationFactory var1) {
      this.transform = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new CreateOperationMenuItemFactory.ItemImpl(this.transform);
   }

   private static class ItemImpl implements PopupMenuItem {
      private final TransformationToOperationFactory transform;
      private boolean visible;
      private boolean multipleObjectsSelected;
      private Transformation tx;

      public ItemImpl(TransformationToOperationFactory var1) {
         this.transform = var1;
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.tx = null;
         this.visible = false;
         if (!var2.isEmpty()) {
            this.visible = var1.isTree() && this.isOnlyTransformations(var2);
            if (this.visible) {
               this.tx = (Transformation)var2.get(0);
               this.multipleObjectsSelected = var2.size() > 1;
            }
         }
      }

      private boolean isOnlyTransformations(List<?> var1) {
         for (Object var3 : var1) {
            if (!(var3 instanceof Transformation)) {
               return false;
            }
         }

         return true;
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         CreateOperationAction var2 = new CreateOperationAction(this.tx, this.transform);
         if (this.multipleObjectsSelected) {
            var2.setEnabled(false);
         }

         var1.add(var2);
         this.tx = null;
      }

      @Override
      public boolean isVisible() {
         return this.visible;
      }
   }
}
