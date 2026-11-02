package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.util.StorageSizeUnit;
import org.etlforge.util.text.FileSizeFormatter;

public class FileSizeRenderer extends DefaultTableCellRenderer {
   private final FileSizeFormatter format;

   public FileSizeRenderer() {
      this(null);
   }

   public FileSizeRenderer(StorageSizeUnit var1) {
      this.format = new FileSizeFormatter(var1);
      this.setHorizontalAlignment(4);
   }

   public void setSmallestUnit(StorageSizeUnit var1) {
      this.format.setSmallestUnit(var1);
   }

   public void setFixedUnit(StorageSizeUnit var1) {
      this.format.setSmallestUnit(var1);
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      String var7 = this.format.format(var2);
      if (var7 != null) {
         this.setText(var7);
      }

      return this;
   }
}
