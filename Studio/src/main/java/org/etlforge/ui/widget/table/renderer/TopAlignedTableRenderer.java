package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TopAlignedTableRenderer implements TableCellRenderer {
   private final TableCellRenderer m_adaptee;

   public TopAlignedTableRenderer() {
      this(new DefaultTableCellRenderer());
   }

   public TopAlignedTableRenderer(TableCellRenderer var1) {
      this.m_adaptee = var1;
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      Component var7 = this.m_adaptee.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      if (var7 instanceof JLabel) {
         ((JLabel)var7).setVerticalAlignment(1);
      }

      return var7;
   }
}
