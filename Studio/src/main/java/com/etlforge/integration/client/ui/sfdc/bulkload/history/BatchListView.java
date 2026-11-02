package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.SortOrder;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.DateRenderer;
import org.etlforge.ui.widget.table.renderer.NumberFormatRenderer;
import org.etlforge.util.clipboard.ClipboardUtils;

final class BatchListView implements UiProvider {
   private final KongaRowTable table;
   private final BatchListView.CopyBatchIdAction copyBatchIdAction = new BatchListView.CopyBatchIdAction();
   private final DateRenderer dateRenderer = Utils.createDateTableRenderer();
   private final JLabel caption;
   private final CompletionPercentageUi completionUi;

   public BatchListView() {
      this.table = this.createTable();
      this.copyBatchIdAction.setSelectionSource(this.table);
      this.caption = new JLabel("Batch list:");
      this.completionUi = new CompletionPercentageUi();
   }

   private KongaRowTable createTable() {
      KongaTable var1 = new KongaTable(new BatchTableModel());
      var1.setDefaultRenderer(Date.class, this.dateRenderer);
      var1.setDefaultRenderer(Integer.class, NumberFormatRenderer.forInteger());
      var1.setAutoCreateRowSorter(true);
      var1.setSortingStatus(BatchTableModel.TIME_SENT, SortOrder.ASCENDING);
      var1.setVisibleRowCount(10);
      TableStyles.normal().makeOver(var1);
      this.installController(var1);
      return new KongaRowTable(var1);
   }

   private void installController(KongaTable var1) {
      KongaPopupMenu var2 = KongaPopupMenu.fromItems(this.copyBatchIdAction);
      var1.setComponentPopupMenu(var2);
   }

   public void display(BulkLoadInstance var1) {
      BatchTableModel var2 = this.updateTable(var1);
      this.table.setModel(var2);
      this.updateCaption(var1);
      this.completionUi.setPercentageSent(var1.getPercentageSent());
   }

   private BatchTableModel updateTable(BulkLoadInstance var1) {
      BatchTableModel var2 = new BatchTableModel();
      var2.addRows(var1.getBatches());
      return var2;
   }

   private void updateCaption(BulkLoadInstance var1) {
      String var2 = var1.getJobId();
      if (var2 != null) {
         this.caption.setText("Batches sent in job " + var2 + ":");
      } else {
         this.caption.setText("Batch list:");
      }
   }

   public void setTimeZone(TimeZone var1) {
      this.dateRenderer.setTimeZone(var1);
      this.table.getAdaptee().repaint();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.northToSouth(this.caption, this.table, this.completionUi);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void selectFirstRow() {
      this.table.selectFirstRow();
   }

   public void installKeyBinding(Action var1) {
      this.table.installKeyBinding(var1);
   }

   private class CopyBatchIdAction extends AbstractSingleObjectSelectionAction<RecordOfSentBatch> {
      public CopyBatchIdAction() {
         super(RecordOfSentBatch.class, "Copy Batch ID");
      }

      protected void run(RecordOfSentBatch var1, ActionEvent var2) {
         String var3 = var1.getBatch().salesforceId;
         ClipboardUtils.copyToSystemClipboard(var3);
      }
   }
}
