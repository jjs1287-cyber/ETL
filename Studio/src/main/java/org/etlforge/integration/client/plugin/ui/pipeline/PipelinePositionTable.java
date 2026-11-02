package org.etlforge.integration.client.plugin.ui.pipeline;

import java.util.EnumSet;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.RowFilter.Entry;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectorModel;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;

public final class PipelinePositionTable implements UiProvider {
   private final KongaRowTable table;
   private final PluginTableModel tableModel;

   public PipelinePositionTable(PluginPositionSelectorModel var1) {
      this.tableModel = PluginTableModel.newSelectorModel(var1);
      this.table = this.createTable();
   }

   private KongaRowTable createTable() {
      TableFactory var1 = ComponentFactories.tableFactory();
      KongaTable var2 = var1.newSortedTable(this.tableModel, 5);
      this.customizeTable(var2);
      return new KongaRowTable(var2);
   }

   private void customizeTable(KongaTable var1) {
      this.installSorter(var1);
      this.installRenderers(var1);
      TableStyles.normal().makeOver(var1);
   }

   private void installSorter(KongaTable var1) {
      var1.setComparator(this.tableModel.getIndexOfPositionColumn(), new PositionComparator());
      var1.setSortingStatus(this.tableModel.getIndexOfNameColumn(), SortOrder.ASCENDING);
   }

   private void installRenderers(KongaTable var1) {
      var1.setDefaultRenderer(PluginNameTableEntry.class, new PluginNameTableEntryRenderer());
      var1.setDefaultRenderer(PipelinePosition.class, new PositionRenderer());
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void addSelectionAction(SelectionAction var1) {
      var1.setSelectionSource(this.table);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void installRelativePositionFilter(EnumSet<RelativePosition> var1) {
      KongaTable var2 = this.table.getAdaptee();
      if (var1.equals(EnumSet.allOf(RelativePosition.class))) {
         var2.setRowFilter(null);
      } else {
         var2.setRowFilter(new PipelinePositionTable.RelativePositionFilter(var1));
      }
   }

   private static class RelativePositionFilter extends RowFilter<PluginTableModel, Integer> {
      private final EnumSet<RelativePosition> positions;

      public RelativePositionFilter(EnumSet<RelativePosition> var1) {
         this.positions = EnumSet.copyOf(var1);
      }

      @Override
      public boolean include(Entry<? extends PluginTableModel, ? extends Integer> var1) {
         PluginTableItem var2 = ((PluginTableModel)var1.getModel()).getRowObjectAt((Integer)var1.getIdentifier());
         return this.positions.contains(var2.getRelativePosition());
      }
   }
}
