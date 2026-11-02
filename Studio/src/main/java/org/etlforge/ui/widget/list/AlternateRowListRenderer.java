package org.etlforge.ui.widget.list;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.border.UnderlineBorder;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;

public class AlternateRowListRenderer implements ListCellRenderer {
   private final ListCellRenderer impl;
   private final Color[] colors;
   private Border border;

   public AlternateRowListRenderer(Color... var1) {
      this(new DefaultListDecorRenderer(), var1);
   }

   public AlternateRowListRenderer(ListCellRenderer var1, Color... var2) {
      this.colors = var2;
      this.impl = var1;
   }

   public void setSeparatorColor(Color var1) {
      this.border = var1 != null ? new UnderlineBorder(var1) : null;
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      Component var6 = this.impl.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (!var4) {
         this.applyBackground(var3, var6);
      }

      if (this.border != null && var6 instanceof JComponent) {
         this.applyBorder(var6);
      }

      return var6;
   }

   private void applyBackground(int var1, Component var2) {
      var2.setBackground(this.colors[var1 % this.colors.length]);
   }

   private void applyBorder(Component var1) {
      JComponent var2 = (JComponent)var1;
      if (var2.getBorder() != null) {
         var2.setBorder(new BorderBuilder(this.border, var2.getBorder()));
      } else {
         var2.setBorder(this.border);
      }
   }
}
