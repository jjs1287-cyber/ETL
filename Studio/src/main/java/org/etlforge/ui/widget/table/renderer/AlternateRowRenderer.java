package org.etlforge.ui.widget.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.etlforge.system.ArgCheck;

public class AlternateRowRenderer implements TableRendererPreparer {
   private final Color[] colors;

   public AlternateRowRenderer(Color[] var1) {
      ArgCheck.checkNotEmpty(var1, "colors");
      this.colors = (Color[])var1.clone();
   }

   public AlternateRowRenderer(Color var1, Color var2) {
      this(new Color[]{var1, var2});
   }

   @Override
   public final boolean doDefaultPreparation() {
      return true;
   }

   @Override
   public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
      if (!var1.isCellSelected(var4, var5)) {
         var3.setBackground(this.getBackground(var4));
      }

      return var3;
   }

   private Color getBackground(int var1) {
      int var2 = var1 % this.colors.length;
      return this.colors[var2];
   }
}
