package org.etlforge.integration.client.ui.entity;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.base.ListenerReport;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class ListenerReportTree implements UiProvider {
   private final KongaTree tree;

   public ListenerReportTree(IntegrationEntity var1) {
      this(var1.reportListeners());
   }

   public ListenerReportTree(ListenerReport var1) {
      this.tree = this.createTree(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   private KongaTree createTree(ListenerReport var1) {
      KongaTree var2 = new KongaTree(this.createTreeModel(var1));
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.setCellRenderer(new ListenerReportTree.Renderer());
      var2.expandAll();
      return var2;
   }

   private KongaTreeModel createTreeModel(ListenerReport var1) {
      DefaultKongaTreeNode var2 = new DefaultKongaTreeNode("Root", true);

      for (String var4 : var1.getCategoriesAlphabetically()) {
         DefaultKongaTreeNode var5 = new DefaultKongaTreeNode(var2, var4, true);

         for (ListenerReport.Item var7 : var1.getItems(var4)) {
            new DefaultKongaTreeNode(var5, var7, false);
         }
      }

      return new DefaultKongaTreeModel(var2);
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private Renderer() {
      }

      @Override
      public Icon getLeafIcon() {
         return null;
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         KongaTreeNode var8 = (KongaTreeNode)var2;
         if (var8.getUserObject() instanceof String) {
            this.setText(var8.getUserObject() + " [" + var8.getChildCount() + "]");
         } else if (var8.getUserObject() instanceof ListenerReport.Item) {
            this.setText(((ListenerReport.Item)var8.getUserObject()).getListener().getClass().getName());
         }

         return this;
      }
   }
}
