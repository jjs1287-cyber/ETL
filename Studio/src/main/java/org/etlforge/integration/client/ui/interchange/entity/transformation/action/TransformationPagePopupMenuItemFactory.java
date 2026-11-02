package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.core.action.popup.SinglePagePopupMenuItemFactory;
import org.etlforge.integration.data.entity.Transformation;

public class TransformationPagePopupMenuItemFactory extends SinglePagePopupMenuItemFactory {
   public TransformationPagePopupMenuItemFactory(Action var1) {
      super(var1);
   }

   @Override
   protected final PopupMenuItem createItem() {
      return new TransformationPagePopupMenuItemFactory.TransformationItem(this.getAction());
   }

   private static class TransformationItem extends SinglePagePopupMenuItemFactory.Item {
      public TransformationItem(Action var1) {
         super(var1);
      }

      @Override
      public boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         return super.isVisibleFor(var1, var2) && var2.get(0) instanceof Transformation;
      }
   }
}
