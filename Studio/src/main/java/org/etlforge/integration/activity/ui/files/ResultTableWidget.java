package org.etlforge.integration.activity.ui.files;

import java.util.List;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.activity.client.files.SingleRunResult;
import org.etlforge.integration.activity.ui.log.LogStatusTableRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;

final class ResultTableWidget implements UiProvider, SelectionSource {
   private final KongaRowTable table;

   public ResultTableWidget(StatusHistoryModel var1) {
      this.table = this.createTable(var1);
      new ResultTableWidget.ResultListBinding(var1);
   }

   private KongaRowTable createTable(StatusHistoryModel var1) {
      ResultTableModel var2 = new ResultTableModel(var1.getResultList());
      int var3 = var1.isSingleRunMode() ? 3 : 8;
      KongaTable var4 = ComponentFactories.tableFactory().newSortedTable(var2, var3);
      var4.withSingleSelection();
      var4.setAutoCreateRowSorter(true);
      this.installRenderers(var4);
      return new KongaRowTable(var4);
   }

   protected void installRenderers(KongaTable var1) {
      var1.setCellRendererFor(ResultTableModel.STATUS, new LogStatusTableRenderer());
      RecordCountRenderer var2 = new RecordCountRenderer();
      var1.setCellRendererFor(ResultTableModel.SUCCESS_RECS, var2);
      var1.setCellRendererFor(ResultTableModel.FAILURE_RECS, var2);
   }

   public OrderedSelection getSelection() {
      return this.table.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.table.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.table.removeSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private class ResultListBinding extends AbstractBinding<List<SingleRunResult>> {
      private final StatusHistoryModel model;

      public ResultListBinding(StatusHistoryModel var2) {
         super(var2, "resultList");
         this.model = var2;
      }

      protected void updateUiWithValue(List<SingleRunResult> var1) {
         ResultTableModel var2 = new ResultTableModel(var1);
         ResultTableWidget.this.table.setModel(var2);
         if (this.model.isSingleRunMode()) {
            ResultTableWidget.this.table.selectFirstRow();
         }
      }
   }
}
