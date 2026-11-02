package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

final class TableRenderers {
   private TableRenderers() {
   }

   static class IndexRenderer extends DefaultTableCellRenderer {
      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         JLabel var7 = (JLabel)super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         var7.setHorizontalAlignment(10);
         var7.setText(String.valueOf(var5 + 1));
         return var7;
      }
   }
}
