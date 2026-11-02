package org.etlforge.ui.xml;

import java.util.Enumeration;
import java.util.LinkedList;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.w3c.dom.Element;

public final class XmlTree extends KongaTree {
   public XmlTree(KongaTreeModel var1) {
      super(var1);
      this.setCellRenderer(new Renderer());
      this.getSelectionModel().setSelectionMode(1);
   }

   public static XmlTree createTree(Element var0) {
      KongaTreeModel var1 = XmlTreeModelBuilder.createModel(var0);
      return new XmlTree(var1);
   }

   public TreePath getPath(TreeNode var1) {
      LinkedList var2 = new LinkedList();
      KongaTreeNode var3 = this.getModel().getRoot();

      while (var1 != null) {
         var2.addFirst(var1);
         if (var1 == var3) {
            return new TreePath(var2.toArray());
         }

         var1 = var1.getParent();
      }

      return null;
   }

   @Override
   public void expandAll() {
      KongaTreeNode var1 = this.getModel().getRoot();
      this.expandAll(new TreePath(var1), true);
   }

   @Override
   public void collapseAll() {
      KongaTreeNode var1 = this.getModel().getRoot();
      this.expandAll(new TreePath(var1), false);
   }

   public void expandSelectedBranch() {
      TreePath var1 = this.getSelectionPath();
      if (var1 == null) {
         this.expandAll();
      } else {
         this.expandAll(var1, true);
      }
   }

   public void collapseSelectedBranch() {
      TreePath var1 = this.getSelectionPath();
      if (var1 == null) {
         this.collapseAll();
      } else {
         this.expandAll(var1, false);
      }
   }

   private void expandAll(TreePath var1, boolean var2) {
      TreeNode var3 = (TreeNode)var1.getLastPathComponent();
      if (var3.getChildCount() >= 0) {
         Enumeration var4 = var3.children();

         while (var4.hasMoreElements()) {
            TreeNode var5 = (TreeNode)var4.nextElement();
            TreePath var6 = var1.pathByAddingChild(var5);
            this.expandAll(var6, var2);
         }
      }

      if (var2) {
         this.expandPath(var1);
      } else {
         this.collapsePath(var1);
      }
   }
}
