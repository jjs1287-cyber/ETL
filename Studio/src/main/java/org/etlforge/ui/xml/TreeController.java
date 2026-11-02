package org.etlforge.ui.xml;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;

public final class TreeController {
   private XmlTree tree;
   private final BoxBuilder controlPanel;
   private final JButton btnExpand;
   private final JButton btnCollapse;
   private final TreeController.ExpandTreeAction expandAction = new TreeController.ExpandTreeAction();
   private final TreeController.CollapseTreeAction collapseAction = new TreeController.CollapseTreeAction();

   public TreeController() {
      this.btnExpand = new JButton(this.expandAction);
      this.btnCollapse = new JButton(this.collapseAction);
      this.controlPanel = Horizontal.box().glue().addAll(this.btnExpand, this.btnCollapse);
   }

   public JPanel getControlPanel() {
      return this.controlPanel.container();
   }

   public void setTree(XmlTree var1) {
      this.tree = var1;
      this.setEnabled(var1 != null);
   }

   public Action getExpandAction() {
      return this.expandAction;
   }

   public Action getCollapseAction() {
      return this.collapseAction;
   }

   private void setEnabled(boolean var1) {
      this.expandAction.setEnabled(var1);
      this.collapseAction.setEnabled(var1);
   }

   private class CollapseTreeAction extends AbstractAction {
      public CollapseTreeAction() {
         super("Collapse");
         this.putValue("ShortDescription", "Collapses all nodes in the selected subtree");
         this.putValue("MnemonicKey", 67);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (TreeController.this.tree != null) {
            TreeController.this.tree.collapseSelectedBranch();
         }
      }
   }

   private class ExpandTreeAction extends AbstractAction {
      public ExpandTreeAction() {
         super("Expand");
         this.putValue("ShortDescription", "Expands all nodes in the selected subtree");
         this.putValue("MnemonicKey", 69);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (TreeController.this.tree != null) {
            TreeController.this.tree.expandSelectedBranch();
         }
      }
   }
}
