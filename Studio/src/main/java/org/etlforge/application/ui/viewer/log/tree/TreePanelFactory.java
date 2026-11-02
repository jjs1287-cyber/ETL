package org.etlforge.application.ui.viewer.log.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.ui.widget.tree.KongaTree;

final class TreePanelFactory {
   private final List<? extends Action> leftActions;
   private final List<? extends Action> rightActions;

   public TreePanelFactory(List<? extends Action> var1, List<? extends Action> var2) {
      this.leftActions = var1;
      this.rightActions = var2;
   }

   public TreePanel getPanel(KongaTree var1) {
      return new TreePanelFactory.PanelImpl(var1);
   }

   private class PanelImpl extends TreePanel {
      public PanelImpl(KongaTree var2) {
         super(PackageResources.TREE_TITLE, var2);
      }

      @Override
      protected JButton[] getToolBarButtons() {
         ArrayList var1 = new ArrayList();
         this.addButtons(var1, TreePanelFactory.this.leftActions, false, true);

         for (JButton var5 : super.getToolBarButtons()) {
            var1.add(var5);
         }

         this.addButtons(var1, TreePanelFactory.this.rightActions, true, false);
         return (JButton[]) var1.toArray(new JButton[var1.size()]);
      }

      private void addButtons(List<JButton> var1, List<? extends Action> var2, boolean var3, boolean var4) {
         if (var2 != null && !var2.isEmpty()) {
            if (var3) {
               var1.add(null);
            }

            for (Action var6 : var2) {
               var1.add(createToolBarButton(var6));
            }

            if (var4) {
               var1.add(null);
            }
         }
      }
   }
}
