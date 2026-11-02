package org.etlforge.ui.xml;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import org.w3c.dom.Attr;

final class Renderer extends JLabel implements TreeCellRenderer {
   protected static final Color SELECTED_FOREGROUND_FOCUS = Color.WHITE;
   protected static final Color SELECTED_FOREGROUND_NOT_FOCUS = Color.BLACK;
   protected static final Color SELECTED_BACKGROUND_FOCUS = new Color(49, 106, 197);
   protected static final Color SELECTED_BACKGROUND_NOT_FOCUS = new Color(236, 233, 216);
   protected static final Color UNSELECTED_FOREGROUND = Color.BLACK;
   protected static final Color UNSELECTED_BACKGROUND = Color.WHITE;
   protected static final Color ELEMENT_COLOR = new Color(139, 69, 19);
   protected static final Color ATTRIBUTE_COLOR = new Color(255, 50, 35);
   protected static final Color VALUE_COLOR = new Color(38, 43, 226);
   private static Font s_font;

   public Renderer() {
      this.setOpaque(true);
      this.setFont(s_font);
   }

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      if (var2 instanceof XmlTreeNode) {
         this.setText(((XmlTreeNode)var2).getDisplayString());
      } else if (var2 != null) {
         this.setText(var2.toString());
      } else {
         this.setText("");
      }

      if (var3) {
         boolean var8 = var1.hasFocus();
         this.setForeground(var8 ? SELECTED_FOREGROUND_FOCUS : SELECTED_FOREGROUND_NOT_FOCUS);
         this.setBackground(var8 ? SELECTED_BACKGROUND_FOCUS : SELECTED_BACKGROUND_NOT_FOCUS);
      } else {
         if (var2 instanceof ElementTreeNode) {
            this.setForeground(ELEMENT_COLOR);
         } else if (var2 instanceof AttributeTreeNode) {
            AttributeTreeNode var10 = (AttributeTreeNode)var2;
            Attr var9 = var10.getXmlAttribute();
            if (var9.getName().equals("value")) {
               this.setForeground(VALUE_COLOR);
            } else {
               this.setForeground(ATTRIBUTE_COLOR);
            }
         } else if (var2 instanceof ValueTreeNode) {
            this.setForeground(VALUE_COLOR);
         } else {
            this.setForeground(UNSELECTED_FOREGROUND);
         }

         this.setBackground(UNSELECTED_BACKGROUND);
      }

      return this;
   }

   static {
      Font var0 = new JLabel().getFont();
      s_font = var0.deriveFont(0, var0.getSize() + 1.0F);
   }
}
