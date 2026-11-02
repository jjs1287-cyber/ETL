package org.etlforge.integration.activity.ui.files;

import java.text.NumberFormat;
import javax.swing.table.DefaultTableCellRenderer;

final class RecordCountRenderer extends DefaultTableCellRenderer {
   private final NumberFormat fmt = NumberFormat.getIntegerInstance();

   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof Long) {
         long var2 = (Long)var1;
         String var4 = this.getStringRepresentation(var2);
         this.setHorizontalAlignment(4);
         super.setValue(var4);
      } else {
         super.setValue(var1);
      }
   }

   private String getStringRepresentation(long var1) {
      return var1 < 0L ? "" : this.fmt.format(var1);
   }
}
