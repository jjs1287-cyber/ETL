package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class PluginNameTableEntryRenderer extends DefaultTableCellRenderer {
   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      if (var2 instanceof PluginNameTableEntry) {
         ((PluginNameTableEntry)var2).decorate(this, var3);
      }

      return this;
   }
}
