package org.etlforge.ui.util.decorator;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public class TreeNodeDecorRenderer extends DefaultTreeCellRenderer implements TreeNodeDecor {
   private JTree tree;
   private boolean selected;
   private boolean expanded;
   private boolean useDefaultToolTip;

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      this.tree = var1;
      this.selected = var3;
      this.expanded = var4;
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      if (var2 instanceof TreeNodeDecorator) {
         ((TreeNodeDecorator)var2).decorate(this);
      } else {
         this.setFont(var1.getFont());
         if (var2 instanceof KongaTreeNode) {
            this.setText(((KongaTreeNode)var2).getDisplayName());
            this.setIcon(((KongaTreeNode)var2).getNodeIcon(var4 ? NodeStatus.OPENED : NodeStatus.CLOSED));
         }

         if (this.useDefaultToolTip) {
            this.setToolTipText(this.getText());
         }
      }

      return this;
   }

   @Override
   public JTree getTree() {
      return this.tree;
   }

   @Override
   public boolean isExpanded() {
      return this.expanded;
   }

   @Override
   public boolean isSelected() {
      return this.selected;
   }

   @Override
   public void setTextStyle(TextDecorator var1) {
      Font var2 = var1.getFont();
      if (var2 == null) {
         var2 = this.tree.getFont();
      }

      this.setFont(var2);
      if (!this.selected) {
         this.setForeground(var1.getTextColor());
      }
   }

   @Override
   public void setToolTip(String var1) {
      this.setToolTipText(var1);
   }

   public void setUseDefaultToolTip(boolean var1) {
      this.useDefaultToolTip = var1;
   }

   @Override
   public void setEnabled(boolean var1) {
      boolean var2 = this.tree != null ? this.tree.isEnabled() && var1 : var1;
      super.setEnabled(var2);
   }
}
