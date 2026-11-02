package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Function;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.PopupMenuDisplayer;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class ProjectTreeContextMenuHandler implements PopupMenuRequestHandler {
   private final IntegrationEntityTree tree;
   private final PopupMenuDisplayer popupMenuDisplayer;
   private final List<Function<KongaTreeNode, JPopupMenu>> specialNodeHandlers;

   public ProjectTreeContextMenuHandler(IntegrationEntityTree var1, PopupMenuDisplayer var2, List<Function<KongaTreeNode, JPopupMenu>> var3) {
      this.tree = var1;
      this.popupMenuDisplayer = var2;
      this.specialNodeHandlers = var3;
   }

   @Override
   public boolean popupMenuRequested(Component var1, MouseEvent var2) {
      List var3 = this.tree.getSelectedEntities();
      if (var3.isEmpty()) {
         return this.checkSpecialNodes();
      }

      this.invokePopupForProjectItems(var3);
      return true;
   }

   private boolean checkSpecialNodes() {
      List var1 = this.tree.getSelectedNodes();
      if (var1.size() == 1) {
         KongaTreeNode var2 = (KongaTreeNode)var1.get(0);

         for (Function var4 : this.specialNodeHandlers) {
            JPopupMenu var5 = (JPopupMenu)var4.apply(var2);
            if (var5 != null) {
               this.tree.showPopupMenu(var5);
               return true;
            }
         }
      }

      return false;
   }

   private void invokePopupForProjectItems(final List<IntegrationEntity> var1) {
      this.tree.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProjectTreeContextMenuHandler.this.popupMenuDisplayer.showPopupMenu(PopupMenuSites.TREE, var1, ProjectTreeContextMenuHandler.this.tree);
         }
      });
   }
}
