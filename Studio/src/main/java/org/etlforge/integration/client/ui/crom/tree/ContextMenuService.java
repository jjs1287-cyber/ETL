package org.etlforge.integration.client.ui.crom.tree;

import com.google.common.collect.Lists;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.KongaTree;

final class ContextMenuService {
   private final SelectionSource selectionSource;
   private final List<SelectionAction> actions = Lists.newArrayList();

   public ContextMenuService(KongaTree var1, SelectionSource var2) {
      var1.addMouseListener(new ContextMenuService.RequestHandler());
      this.selectionSource = var2;
   }

   public void addAction(SelectionAction var1) {
      var1.setSelectionSource(this.selectionSource);
      this.actions.add(var1);
   }

   private class RequestHandler extends MouseActionListener {
      private RequestHandler() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         this.openContextMenu(var1);
      }

      private void openContextMenu(MouseEvent var1) {
         List var2 = this.getActionsToIncludeInMenu();
         if (!var2.isEmpty()) {
            KongaPopupMenu var3 = new KongaPopupMenu();
            var3.addActions(var2);
            var3.show(var1.getComponent(), var1.getX(), var1.getY());
         }
      }

      private List<Action> getActionsToIncludeInMenu() {
         ArrayList var1 = Lists.newArrayList();

         for (SelectionAction var3 : ContextMenuService.this.actions) {
            if (var3.isEnabled()) {
               var1.add(var3);
            }
         }

         return var1;
      }
   }
}
