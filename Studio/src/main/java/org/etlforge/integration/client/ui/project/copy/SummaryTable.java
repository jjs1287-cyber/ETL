package org.etlforge.integration.client.ui.project.copy;

import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.table.KongaTable;

final class SummaryTable implements UiProvider {
   private final KongaTable table;

   public SummaryTable(DeepCopyResult var1) {
      SummaryTableModel var2 = new SummaryTableModel(var1);
      this.table = ComponentFactories.tableFactory().newTable(var2, 8);
      this.customizeTable();
   }

   public void setCaption(String var1) {
      this.table.setLabel(var1);
   }

   private void customizeTable() {
      IntegrationEntityTableCellRenderer var1 = new IntegrationEntityTableCellRenderer();
      this.table.setCellRendererFor(SummaryTableModel.ORIGINAL, var1);
      this.table.setCellRendererFor(SummaryTableModel.COPY, var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }
}
