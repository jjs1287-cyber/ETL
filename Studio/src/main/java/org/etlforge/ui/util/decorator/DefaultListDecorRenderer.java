package org.etlforge.ui.util.decorator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.border.Border;

public class DefaultListDecorRenderer extends DefaultListCellRenderer implements ListDecor {
   private JList list;
   private Border customBorder;

   public final void setCustomBorder(Border var1) {
      this.customBorder = var1;
   }

   @Override
   public final Font getListFont() {
      return this.list != null ? this.list.getFont() : null;
   }

   @Override
   public Color getTextColor() {
      return this.list != null ? this.list.getForeground() : null;
   }

   @Override
   public void setTextColor(Color var1) {
      this.setForeground(var1);
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      this.list = var1;
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var2 instanceof ListDecorator) {
         ((ListDecorator)var2).decorate(this, var4);
      }

      if (this.customBorder != null) {
         this.setBorder(this.customBorder);
      }

      return this;
   }

   @Override
   public void setToolTip(String var1) {
      super.setToolTipText(var1);
   }
}
