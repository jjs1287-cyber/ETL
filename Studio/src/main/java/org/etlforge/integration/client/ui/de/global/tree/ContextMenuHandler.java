package org.etlforge.integration.client.ui.de.global.tree;

import com.google.common.base.Supplier;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class ContextMenuHandler implements PopupMenuRequestHandler {
   private final Supplier<List<Action>> actionProvider;

   public ContextMenuHandler(Supplier<List<Action>> var1) {
      this.actionProvider = var1;
   }

   @Override
   public boolean popupMenuRequested(Component var1, MouseEvent var2) {
      this.showMenu(var1, var2.getX(), var2.getY());
      return true;
   }

   private void showMenu(Component var1, int var2, int var3) {
      TreePath var4 = ((JTree)var1).getClosestPathForLocation(var2, var3);
      if (var4 != null) {
         ((JTree)var1).setSelectionPath(var4);
         KongaPopupMenu var5 = new KongaPopupMenu();
         var5.addActions(this.actionProvider.get());
         var5.show(var1, var2, var3);
      }
   }
}
