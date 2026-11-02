package org.etlforge.ui.widget.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import org.etlforge.ui.UiLook;

public class KongaTreeCellRenderer implements TreeCellRenderer {
   protected static final Color SELECTED_FOREGROUND_FOCUS = Color.WHITE;
   protected static final Color SELECTED_FOREGROUND_NOT_FOCUS = Color.BLACK;
   protected static final Color SELECTED_BACKGROUND_FOCUS = new Color(49, 106, 197);
   protected static final Color SELECTED_BACKGROUND_NOT_FOCUS = new Color(236, 233, 216);
   protected static final Color UNSELECTED_FOREGROUND = Color.BLACK;
   protected static final Color UNSELECTED_BACKGROUND = Color.WHITE;
   protected static final Font DEFAULT_FONT = UiLook.getLabelFont();

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      JComponent var8 = null;
      if (var2 instanceof KongaTreeNode) {
         KongaTreeNode var9 = (KongaTreeNode)var2;
         var8 = this.createNodeRendererComponent((KongaTree)var1, var9, var4 ? NodeStatus.OPENED : NodeStatus.CLOSED).getComponent();
      } else {
         var8 = new JLabel(var2 != null ? var2.toString() : "");
      }

      var8.setFont(this.getNodeFont(var2));
      boolean var11 = var1.hasFocus();
      var8.setOpaque(var3);
      if (var3) {
         var8.setForeground(this.getSelectedForeground(var2, var11));
         var8.setBackground(this.getSelectedBackground(var2, var11));
      } else {
         var8.setForeground(this.getUnselectedForeground(var2, var11));
         var8.setBackground(this.getUnselectedBackground(var2, var11));
      }

      return var8;
   }

   protected Color getSelectedForeground(Object var1, boolean var2) {
      return var2 ? SELECTED_FOREGROUND_FOCUS : SELECTED_FOREGROUND_NOT_FOCUS;
   }

   protected Color getSelectedBackground(Object var1, boolean var2) {
      return var2 ? SELECTED_BACKGROUND_FOCUS : SELECTED_BACKGROUND_NOT_FOCUS;
   }

   protected Color getUnselectedForeground(Object var1, boolean var2) {
      return UNSELECTED_FOREGROUND;
   }

   protected Color getUnselectedBackground(Object var1, boolean var2) {
      return UNSELECTED_BACKGROUND;
   }

   protected Font getNodeFont(Object var1) {
      return DEFAULT_FONT;
   }

   protected KongaTreeNodeRendererComponent createNodeRendererComponent(KongaTree var1, KongaTreeNode var2, NodeStatus var3) {
      return new DefaultKongaTreeNodeRendererComponent(var1, var2, var3);
   }
}
