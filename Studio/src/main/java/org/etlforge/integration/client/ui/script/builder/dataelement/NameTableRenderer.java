package org.etlforge.integration.client.ui.script.builder.dataelement;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.debug.client.de.DataElement;

final class NameTableRenderer extends DefaultTableCellRenderer {
   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      DataElement var7 = ((DataElementTableModel)var1.getModel()).getRowObjectAt(var5);
      this.setIcon(DataElementIcons.getIcon(var7));
      return this;
   }
}
