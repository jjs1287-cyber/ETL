package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.WebServiceCall;

public class WebServiceCallItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public WebServiceCallItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected final PopupMenuItem createItem() {
      return new WebServiceCallItemFactory.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         boolean var3 = Iterables.any(var2, Predicates.instanceOf(WebServiceCall.class));
         boolean var4 = var3 && var2.size() == 1;
         this.setVisible(var3);
         this.setEnabled(var4);
      }
   }
}
