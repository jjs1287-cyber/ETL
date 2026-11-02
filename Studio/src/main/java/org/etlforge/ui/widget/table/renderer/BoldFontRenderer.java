package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.etlforge.ui.util.Fonts;

public abstract class BoldFontRenderer implements TableRendererPreparer {
   @Override
   public boolean doDefaultPreparation() {
      return true;
   }

   @Override
   public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
      if (var3 instanceof JLabel) {
         Font var6 = this.getFont(var1, var4, var5);
         ((JLabel)var3).setFont(var6);
      }

      return var3;
   }

   private Font getFont(JTable var1, int var2, int var3) {
      Font var4 = var1.getFont();
      if (this.isBold(var1, var2, var3)) {
         var4 = Fonts.bold(var4);
      }

      return var4;
   }

   protected abstract boolean isBold(JTable var1, int var2, int var3);
}
