package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Supplier;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Action;
import javax.swing.JList;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class ListContextMenuHandler implements PopupMenuRequestHandler {
   private Supplier<List<Action>> actionProvider;

   public void setActionProvider(Supplier<List<Action>> var1) {
      this.actionProvider = var1;
   }

   @Override
   public boolean popupMenuRequested(Component var1, MouseEvent var2) {
      if (this.actionProvider != null) {
         int var3 = ((JList)var1).locationToIndex(var2.getPoint());
         if (var3 >= 0) {
            ((JList)var1).setSelectedIndex(var3);
            KongaPopupMenu var4 = new KongaPopupMenu();
            var4.addActions(this.actionProvider.get());
            var4.show(var1, var2.getX(), var2.getY());
         }
      }

      return true;
   }
}
