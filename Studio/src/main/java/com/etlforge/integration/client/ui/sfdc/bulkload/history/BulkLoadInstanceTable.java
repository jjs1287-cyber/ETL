package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.swing.Action;
import javax.swing.SortOrder;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.DateRenderer;
import org.etlforge.ui.widget.table.renderer.NumberFormatRenderer;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class BulkLoadInstanceTable implements UiProvider, SelectionSource {
   private final KongaRowTable table;
   private final BulkLoadInstanceTable.CopyJobIdAction copyJobIdAction = new BulkLoadInstanceTable.CopyJobIdAction();
   private final DateRenderer dateRenderer = Utils.createDateTableRenderer();

   public BulkLoadInstanceTable() {
      this.table = this.createTable();
      this.copyJobIdAction.setSelectionSource(this.table);
   }

   private KongaRowTable createTable() {
      KongaTable var1 = new KongaTable(new BulkLoadInstanceTableModel());
      var1.setAutoCreateRowSorter(true);
      var1.setDefaultRenderer(Date.class, this.dateRenderer);
      NumberFormatRenderer var2 = NumberFormatRenderer.forInteger();
      var1.setCellRendererFor(BulkLoadInstanceTableModel.SUCCESS_RECORDS, var2);
      var1.setCellRendererFor(BulkLoadInstanceTableModel.FAILURE_RECORDS, var2);
      var1.setSortingStatus(BulkLoadInstanceTableModel.START_TIME, SortOrder.DESCENDING);
      var1.setVisibleRowCount(10);
      TableStyles.normal().makeOver(var1);
      this.installController(var1);
      return new KongaRowTable(var1);
   }

   private void installController(KongaTable var1) {
      KongaPopupMenu var2 = KongaPopupMenu.fromItems(this.copyJobIdAction);
      var1.setComponentPopupMenu(var2);
   }

   public BulkLoadInstance getSelectedInstance() {
      IndexedSelection var1 = this.getSelection();
      return var1.size() == 1 ? (BulkLoadInstance)var1.head() : null;
   }

   public void setInstances(List<BulkLoadInstance> var1) {
      BulkLoadInstanceTableModel var2 = new BulkLoadInstanceTableModel();
      var2.addRows(var1);
      this.table.setModel(var2);
   }

   public void setTimeZone(TimeZone var1) {
      this.dateRenderer.setTimeZone(var1);
      this.table.getAdaptee().repaint();
   }

   public IndexedSelection getSelection() {
      return this.table.getSelection();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
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

   public void selectFirstRow() {
      this.table.selectFirstRow();
   }

   public void installKeyBinding(Action var1) {
      this.table.installAction(var1);
   }

   private class CopyJobIdAction extends AbstractSingleObjectSelectionAction<BulkLoadInstance> {
      public CopyJobIdAction() {
         super(BulkLoadInstance.class, "Copy Job ID");
      }

      protected void run(BulkLoadInstance var1, ActionEvent var2) {
         String var3 = var1.getJobId();
         ClipboardUtils.copyToSystemClipboard(var3);
      }
   }
}
