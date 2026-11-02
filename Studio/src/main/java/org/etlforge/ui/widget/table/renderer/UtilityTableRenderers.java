package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.etlforge.ui.layout.HorizontalAlignment;

public final class UtilityTableRenderers {
   private UtilityTableRenderers() {
      throw new UnsupportedOperationException();
   }

   public static TableCellRenderer getAlignedRenderer(final HorizontalAlignment var0) {
      return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            this.setHorizontalAlignment(var0.asSwingConstant());
            return this;
         }
      };
   }
}
