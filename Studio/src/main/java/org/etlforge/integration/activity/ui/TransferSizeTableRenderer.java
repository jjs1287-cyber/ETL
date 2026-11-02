package org.etlforge.integration.activity.ui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.util.StorageSizeUnit;
import org.etlforge.util.text.FileSizeFormatter;

public final class TransferSizeTableRenderer extends DefaultTableCellRenderer {
   private final FileSizeFormatter format = new FileSizeFormatter(StorageSizeUnit.Bytes);

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      this.setHorizontalAlignment(11);
      if (var2 instanceof TransferSize) {
         this.setText(this.format.format(((TransferSize)var2).getBytes()));
      }

      return this;
   }
}
