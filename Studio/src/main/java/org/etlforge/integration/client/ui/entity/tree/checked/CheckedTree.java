package org.etlforge.integration.client.ui.entity.tree.checked;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeEvent;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeListener;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeListeners;

public class CheckedTree extends KongaTree {
   private final CheckBoxTreeListeners checkListeners;
   private final CheckBoxNodeRenderer renderer;

   public CheckedTree(DefaultKongaTreeModel var1, CheckBoxNodeRenderer var2) {
      super(var1);
      if (var2 == null) {
         var2 = new CheckBoxNodeRenderer();
      }

      this.renderer = var2;
      this.checkListeners = new CheckBoxTreeListeners();
      this.setCellRenderer(var2);
      this.addMouseListener(new CheckedTree.MouseClickHandler(this));
      this.registerActions();
   }

   public static CheckedTree createCheckedTree(KongaTreeModel var0) {
      return new CheckedTree(createCheckedModel(var0), null);
   }

   public static CheckedTree createCheckedTree(KongaTreeModel var0, CheckBoxNodeRenderer var1) {
      return new CheckedTree(createCheckedModel(var0), var1);
   }

   public static DefaultKongaTreeModel createCheckedModel(KongaTreeModel var0) {
      KongaTreeNode var1 = var0.getRoot();
      CheckBoxEntityNode var2 = new CheckBoxEntityNode(null, var1.getUserObject(), true);
      addChildren(var1, var2);
      return new DefaultKongaTreeModel(var2);
   }

   public DefaultKongaTreeModel getModel() {
      return (DefaultKongaTreeModel)super.getModel();
   }

   protected CheckBoxNodeRenderer createNodeRenderer() {
      return new CheckBoxNodeRenderer();
   }

   public final void addCheckListener(CheckBoxTreeListener var1) {
      this.checkListeners.add(var1);
   }

   public final void removeCheckListener(CheckBoxTreeListener var1) {
      this.checkListeners.remove(var1);
   }

   private void fireNodeToggled(CheckBoxNode var1) {
      CheckBoxTreeEvent var2 = new CheckBoxTreeEvent(this, var1);
      this.checkListeners.call().nodeToggled(var2);
   }

   private void registerActions() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this);
      var1.addBinding(KeyStroke.getKeyStroke(32, 0), "space", new CheckedTree.SpaceAction(this));
      var1.install();
   }

   private static void addChildren(KongaTreeNode var0, CheckBoxEntityNode var1) {
      for (KongaTreeNode var3 : var0.children()) {
         CheckBoxEntityNode var4 = new CheckBoxEntityNode(var1, var3.getUserObject(), var3.getAllowsChildren());
         addChildren(var3, var4);
      }
   }

   private static class MouseClickHandler extends MouseAdapter {
      private final CheckedTree tree;

      public MouseClickHandler(CheckedTree var1) {
         this.tree = var1;
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         TreePath var2 = this.tree.getPathForLocation(var1.getX(), var1.getY());
         if (var2 != null) {
            Object var3 = var2.getLastPathComponent();
            if (var3 instanceof CheckBoxNode) {
               Rectangle var4 = this.tree.getPathBounds(var2);
               if (this.tree.renderer.isInCheckBox(var1.getX() - var4.x, var1.getY() - var4.y)) {
                  ((CheckBoxNode)var3).toggle();
                  this.tree.fireNodeToggled((CheckBoxNode)var3);
                  this.tree.repaint();
               }
            }
         }
      }
   }

   private static class SpaceAction extends AbstractAction {
      private final CheckedTree tree;

      public SpaceAction(CheckedTree var1) {
         this.tree = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = false;

         for (KongaTreeNode var4 : this.tree.getSelectedNodes()) {
            if (var4 instanceof CheckBoxNode) {
               ((CheckBoxNode)var4).toggle();
               this.tree.fireNodeToggled((CheckBoxNode)var4);
               var2 = true;
            }
         }

         if (var2) {
            this.tree.repaint();
         }
      }
   }
}
