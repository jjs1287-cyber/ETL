package org.etlforge.integration.activity.ui.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.ListSelectionDispatcher;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.table.TableUtils;

final class LogEntryTableWidget implements LogEntryWidget {
   private final AbstractOperationLogModel model;
   private final OperationLogTableColumn[] columnsToInclude;
   private final OperationLogTable table;
   private final PropertyChangeListener modelListener;
   private final LogEntryTableWidget.SelectionDispatcher selectionDispatcher = new LogEntryTableWidget.SelectionDispatcher();
   private DeletableTableAction selectAllAction;
   private DeletableTableAction unselectAllAction;

   public LogEntryTableWidget(AbstractOperationLogModel var1, OperationLogTableColumn... var2) {
      this.model = var1;
      this.columnsToInclude = (OperationLogTableColumn[])var2.clone();
      this.table = this.createTable();
      this.modelListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            LogEntryTableWidget.this.syncWithModel();
         }
      };
      var1.addPropertyChangeListener("entries", this.modelListener);
      this.syncWithModel();
   }

   private OperationLogTable createTable() {
      OperationLogTable var1 = new OperationLogTable(this.emptyTableModel());
      new OperationLogTableController(var1).install();
      var1.getSelectionModel().addListSelectionListener(this.selectionDispatcher);
      var1.setVisibleRowCount(10);
      return var1;
   }

   private OperationLogTableModel emptyTableModel() {
      return new OperationLogTableModel(this.columnsToInclude);
   }

   private void syncWithModel() {
      LogEntryTableWidget.TableModelProducer var1 = new LogEntryTableWidget.TableModelProducer();
      LogEntryTableWidget.TableModelConsumer var2 = new LogEntryTableWidget.TableModelConsumer();
      Computation var3 = Computation.create(var1, var2);
      var3.swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, this.getClass())
         .withWaitService(this.model.getTalkingToServerWaitService())
         .buildAndExecute();
   }

   @Override
   public LogDisplayMode getMode() {
      return LogDisplayMode.FLAT;
   }

   @Override
   public OperationLogEntry getSelectedLogEntry() {
      return this.table.getSelectedLogEntry();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener("entries", this.modelListener);
   }

   @Override
   public void setBulkSelectionActions(DeletableTableAction var1, DeletableTableAction var2) {
      this.selectAllAction = var1;
      this.unselectAllAction = var2;
      this.bindBulkSelectionActionsToModel((OperationLogTableModel)this.table.getModel());
   }

   private void bindBulkSelectionActionsToModel(OperationLogTableModel var1) {
      if (this.selectAllAction != null && this.unselectAllAction != null) {
         this.selectAllAction.setTableModel(var1);
         this.unselectAllAction.setTableModel(var1);
      }
   }

   private class SelectionDispatcher extends ListSelectionDispatcher {
      private SelectionDispatcher() {
      }

      protected OrderedSelection getSelection(ListSelectionEvent var1) {
         OperationLogTableModel var2 = (OperationLogTableModel)LogEntryTableWidget.this.table.getModel();
         return TableUtils.toSelection(var2, LogEntryTableWidget.this.table.getSelectedRows());
      }
   }

   private class TableModelConsumer implements Receiver<OperationLogTableModel> {
      private TableModelConsumer() {
      }

      public void handle(OperationLogTableModel var1) {
         LogEntryTableWidget.this.bindBulkSelectionActionsToModel(var1);
         LogEntryTableWidget.this.table.setModel(var1);
         this.updateSorting();
         LogEntryTableWidget.this.table.requestFocusInWindow();
      }

      private void updateSorting() {
         SortStatus var1 = LogEntryTableWidget.this.model.getSortStatus();
         if (var1 != null) {
            LogEntryTableWidget.this.table.setSortingStatus(var1.column, var1.sortOrder);
         }

         LogEntryTableWidget.this.table.getRowSorter().addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent var1) {
               LogEntryTableWidget.this.model.setSortStatus(LogEntryTableWidget.this.table.getSortStatus());
            }
         });
      }
   }

   private class TableModelProducer implements Callable<OperationLogTableModel> {
      private TableModelProducer() {
      }

      public OperationLogTableModel call() throws Exception {
         OperationLogTableModel var1 = new OperationLogTableModel(LogEntryTableWidget.this.model.getFilteredList(), LogEntryTableWidget.this.columnsToInclude);
         if (LogEntryTableWidget.this.model instanceof DeleteSelectionListener) {
            var1.addDeleteSelectionListener((DeleteSelectionListener)LogEntryTableWidget.this.model);
         }

         return var1;
      }
   }
}
