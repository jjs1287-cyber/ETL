package org.etlforge.integration.client.plugin.ui.pipeline;

import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.AppliedPipelinePluginsModel;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;

public final class AppliedPipelinePluginsTable implements UiProvider {
   private final KongaRowTable table;

   public AppliedPipelinePluginsTable(AppliedPipelinePluginsModel var1) {
      this.table = this.createTable(PluginTableModel.newAppliedPluginsModel(var1));
   }

   private KongaRowTable createTable(PluginTableModel var1) {
      TableFactory var2 = ComponentFactories.tableFactory();
      KongaTable var3 = var2.newSortedTable(var1, 5);
      this.customizeTable(var3);
      return new KongaRowTable(var3);
   }

   private void customizeTable(KongaTable var1) {
      TableStyles.normal().makeOver(var1);
      var1.setDefaultRenderer(PluginNameTableEntry.class, new PluginNameTableEntryRenderer());
      var1.setDefaultRenderer(PipelinePosition.class, new PositionRenderer());
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void bindSelectionActions(SelectionAction... var1) {
      for (SelectionAction var5 : var1) {
         var5.setSelectionSource(this.table);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }
}
