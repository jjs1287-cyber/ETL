package org.etlforge.ui.widget.list;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;

public class PaddingListRenderer implements ListCellRenderer {
   private final ListCellRenderer toPad;
   private final Border padding;

   public PaddingListRenderer(Insets var1) {
      this(new DefaultListDecorRenderer(), var1);
   }

   public PaddingListRenderer(ListCellRenderer var1, Insets var2) {
      if (var1 == null) {
         throw new NullPointerException("toPad");
      }

      this.toPad = var1;
      this.padding = Empty.border(var2);
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      Component var6 = this.toPad.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var6 instanceof JComponent) {
         JComponent var7 = (JComponent)var6;
         if (var7.getBorder() != null) {
            var7.setBorder(new BorderBuilder(var7.getBorder(), this.padding));
         } else {
            var7.setBorder(this.padding);
         }
      }

      return var6;
   }
}
