package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public abstract class TableRendererWithToolTip implements TableCellRenderer {
   private TableCellRenderer m_originalRenderer;

   public TableRendererWithToolTip(TableCellRenderer var1) {
      this.m_originalRenderer = var1;
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      Component var7 = this.m_originalRenderer.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      if (var7 instanceof JComponent) {
         ((JComponent)var7).setToolTipText(this.getToolTipText(var1, var2, var3, var4, var5, var6));
      }

      return var7;
   }

   public abstract String getToolTipText(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6);
}
