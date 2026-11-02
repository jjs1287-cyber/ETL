package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.DefaultPopupMenuManager;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.tree.ActionNode;
import org.etlforge.ui.widget.tree.ExpandEverythingUnderSelectedNodeAction;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class ProjectTreeController {
   private final EntityPageDisplayer pageDisplayer;
   private final Action deleteObjectAction;
   private final ImmutableList<Function<KongaTreeNode, JPopupMenu>> specialNodeHandlers;
   private PopupMenuManager popupMenuManager = new DefaultPopupMenuManager();

   public ProjectTreeController(EntityConfigLookup var1, Iterable<ContentViewerCategory> var2, EntityPageDisplayer var3, Action var4) {
      this.pageDisplayer = var3;
      this.deleteObjectAction = var4;
      this.specialNodeHandlers = this.buildSpecialNodeHandlers(var1, var2);
   }

   private ImmutableList<Function<KongaTreeNode, JPopupMenu>> buildSpecialNodeHandlers(EntityConfigLookup var1, Iterable<ContentViewerCategory> var2) {
      Builder var3 = ImmutableList.builder();
      var3.add(new CategoryNodeContextMenuProvider(var1));

      for (ContentViewerCategory var5 : var2) {
         var3.add(new CategoryRootContextMenuProvider(var5, var1));
      }

      return var3.build();
   }

   public void setPopupMenuManager(PopupMenuManager var1) {
      this.popupMenuManager = var1;
   }

   public void install(IntegrationEntityTree var1) {
      this.registerActions(var1);
      this.installContextMenu(var1);
   }

   private void registerActions(final IntegrationEntityTree var1) {
      ProjectTreeController.EnterAction var2 = new ProjectTreeController.EnterAction(var1, this.pageDisplayer);
      DefaultActionHandler var3 = new DefaultActionHandler(var1, var2);
      var3.install();
      ActionKeyBinding.install(this.deleteObjectAction, var1, KeyUtils.DELETE);
      AbstractAction var4 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.renameSelectedNode();
         }
      };
      ActionKeyBinding.install(var4, var1, KeyUtils.F2);
      AbstractAction var5 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            IntegrationEntityTreeModel var2 = var1.getModel();

            for (KongaTreeNode var4 : var1.getSelectedNodes()) {
               var2.sortChildren(var4);
            }
         }
      };
      ActionKeyBinding.install(var5, var1, KeyUtils.REFRESH);
      ActionKeyBinding.install(new ExpandEverythingUnderSelectedNodeAction(var1), var1, KeyUtils.EXPAND_EVERYTHING);
   }

   private void installContextMenu(IntegrationEntityTree var1) {
      ProjectTreeContextMenuHandler var2 = new ProjectTreeContextMenuHandler(var1, this.popupMenuManager, this.specialNodeHandlers);
      PopupMenuRequestHandlers.install(var1, var2);
   }

   private static class EnterAction extends AbstractAction {
      private final IntegrationEntityTree tree;
      private final EntityPageDisplayer pageDisplayer;

      public EnterAction(IntegrationEntityTree var1, EntityPageDisplayer var2) {
         this.tree = var1;
         this.pageDisplayer = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.tree.getSelectionCount() == 1) {
            KongaTreeNode var2 = this.tree.getSelectedNodes().get(0);
            if (var2 instanceof ActionNode) {
               ((ActionNode)var2).triggerActionIfEnabled(var1);
               return;
            }
         }

         for (IntegrationEntity var3 : this.tree.getSelectedEntities()) {
            this.pageDisplayer.openPage(var3);
         }
      }
   }
}
