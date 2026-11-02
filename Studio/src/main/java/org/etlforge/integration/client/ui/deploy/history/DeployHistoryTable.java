package org.etlforge.integration.client.ui.deploy.history;

import java.awt.EventQueue;
import javax.swing.SortOrder;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.DeployHistoryListener;
import org.etlforge.integration.client.deploy.history.DeployResultFilter;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;

public final class DeployHistoryTable implements UiProvider, Disposable {
   private volatile DeployHistory history;
   private DeployResultFilter filter;
   private volatile DeployHistoryTableModel model;
   private final KongaRowTable table;
   private volatile DeployHistoryListener historyListener;
   private boolean disposed;

   public DeployHistoryTable(DeployHistory var1) {
      this(DeployResultFilter.ACCEPTS_ALL);
   }

   public DeployHistoryTable(DeployResultFilter var1) {
      this.filter = var1;
      this.model = new DeployHistoryTableModel();
      this.table = this.createTable(this.model);
   }

   private KongaRowTable createTable(DeployHistoryTableModel var1) {
      TableFactory var2 = ComponentFactories.tableFactory();
      KongaTable var3 = var2.newSortedTable(var1, 10);
      var3.setSortingStatus(DeployHistoryTableModel.DATE, SortOrder.DESCENDING);
      installRenderers(var3);
      updateStyle(var3);
      return new KongaRowTable(var3);
   }

   private static void updateStyle(KongaTable var0) {
      TableStyle var1 = TableStyles.normal();
      var1.makeOver(var0);
   }

   private static void installRenderers(KongaTable var0) {
      ListDecorTableRenderer var1 = new ListDecorTableRenderer();
      var0.setDefaultRenderer(DateCell.class, var1);
      var0.setDefaultRenderer(OutcomeTableItem.class, var1);
      var0.setDefaultRenderer(ServerTableItem.class, var1);
      var0.setDefaultRenderer(MessageTableItem.class, var1);
      var0.setDefaultRenderer(UserTableItem.class, var1);
   }

   public void setFilter(DeployResultFilter var1) {
      this.filter = var1;
      this.loadHistoryIntoTable();
   }

   private void loadHistoryIntoTable() {
      DeployHistoryTable.HistoryLoader var1 = new DeployHistoryTable.HistoryLoader();
      var1.execute();
   }

   @Override
   public void dispose() {
      if (this.history != null) {
         this.history.removeListener(this.historyListener);
      }

      this.disposed = true;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void installSelectionAction(SelectionAction var1) {
      this.table.installAction(var1);
   }

   public void setHistory(DeployHistory var1, boolean var2) {
      if (var2) {
         this.filter = DeployResultFilter.ACCEPTS_ALL;
      }

      if (var1 != this.history) {
         if (this.history != null) {
            this.uninstallHistoryListener();
         }

         this.history = var1;
         if (var1 != null) {
            this.loadHistoryIntoTable();
         } else {
            this.clearTable();
         }
      }
   }

   private void installHistoryListener() {
      if (this.historyListener == null) {
         this.historyListener = new DeployHistoryTable.HistoryListener();
         this.history.addListener(this.historyListener);
      }
   }

   private void uninstallHistoryListener() {
      if (this.historyListener != null) {
         this.history.removeListener(this.historyListener);
         this.historyListener = null;
      }
   }

   private void clearTable() {
      this.model = new DeployHistoryTableModel();
      this.resetTable();
   }

   private void resetTable() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeployHistoryTable.this.table.setModel(DeployHistoryTable.this.model);
         }
      });
   }

   private void setSelectedIndex(int var1) {
      assert EventQueue.isDispatchThread() : "setSelectedIndex called from outside the EDT";
      IndexedSelection var2 = SelectionFactory.newIndexedSelection(null, var1);
      this.table.setSelection(var2);
   }

   private final class HistoryListener implements DeployHistoryListener {
      private HistoryListener() {
      }

      @Override
      public void deployResultAdded(final DeployResult var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (DeployHistoryTable.this.filter == null || DeployHistoryTable.this.filter.apply(var1)) {
                  DeployHistoryTable.this.model.addRowAt(0, var1);
                  DeployHistoryTable.this.setSelectedIndex(0);
               }
            }
         });
      }

      @Override
      public void deployResultRemoved(final DeployResult var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               int var1x = -1;

               for (int var2 = DeployHistoryTable.this.model.getRowCount() - 1; var2 >= 0; var2--) {
                  if (var1.getId().equals(DeployHistoryTable.this.model.getRowObjectAt(var2).getId())) {
                     DeployHistoryTable.this.model.deleteRow(var2);
                     var1x = var2;
                  }
               }

               if (var1x >= 0 && DeployHistoryTable.this.model.getRowCount() > 0) {
                  int var3 = Math.max(0, var1x - 1);
                  DeployHistoryTable.this.setSelectedIndex(var3);
               }
            }
         });
      }

      @Override
      public void historyCleared() {
         DeployHistoryTable.this.clearTable();
      }
   }

   private final class HistoryLoader extends KongaSwingWorker<DeployHistoryTableModel, Void> {
      public HistoryLoader() {
         super(DeployHistoryTable.class);
         this.setLogLevel(ErrorLevel.LOG);
      }

      protected DeployHistoryTableModel doInBackground() throws Exception {
         return new DeployHistoryTableModel(DeployHistoryTable.this.history, DeployHistoryTable.this.filter);
      }

      protected void onSuccess(DeployHistoryTableModel var1) {
         if (!DeployHistoryTable.this.disposed) {
            DeployHistoryTable.this.model = var1;
            DeployHistoryTable.this.resetTable();
            DeployHistoryTable.this.installHistoryListener();
         }
      }
   }
}
