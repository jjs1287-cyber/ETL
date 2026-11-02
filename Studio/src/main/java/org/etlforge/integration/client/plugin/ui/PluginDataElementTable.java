package org.etlforge.integration.client.plugin.ui;

import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.dataelement.DataElementType;
import org.etlforge.integration.plugin.PluginDataElementTemplate;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.TextAreaTableRenderer;
import org.etlforge.ui.widget.table.renderer.TopAlignedTableRenderer;

public class PluginDataElementTable implements UiProvider {
   private KongaRowTable table;

   public PluginDataElementTable(PluginDataElementTemplate[] var1) {
      this.table = new KongaRowTable(this.createTableDelegate(var1));
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private KongaTable createTableDelegate(PluginDataElementTemplate[] var1) {
      PluginDataElementTableModel var2 = new PluginDataElementTableModel(var1);
      TableFactory var3 = ComponentFactories.tableFactory();
      KongaTable var4 = var3.newSortedTable(var2, 6);
      this.customizeTableDelegate(var4);
      return var4;
   }

   private void customizeTableDelegate(KongaTable var1) {
      this.setRenderers(var1);
   }

   private void setRenderers(KongaTable var1) {
      var1.setCellRendererFor(PluginDataElementTableModel.DOCUMENTATION, new TextAreaTableRenderer());
      this.topAlignRenderer(var1, String.class);
      this.topAlignRenderer(var1, DataElementType.class);
   }

   private void topAlignRenderer(KongaTable var1, Class<?> var2) {
      TableCellRenderer var3 = var1.getDefaultRenderer(var2);
      var1.setDefaultRenderer(var2, new TopAlignedTableRenderer(var3));
   }
}
