package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.integration.data.test.TestResultComparators;
import org.etlforge.integration.data.test.TestResultFilter;
import org.etlforge.integration.data.test.TestResultHistory;
import org.etlforge.integration.data.test.TestResultHistoryListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;

public class EntityTestHistoryTable<T extends EntityTestResult> implements UiProvider, Disposable {
   private TestResultHistory<T> history;
   private IntegrationEntityLookup entityLookup;
   private final KongaRowTable table;
   private EntityTestHistoryTableModel model;
   private TestResultFilter filter;
   private TestResultHistoryListener historyListener;

   public EntityTestHistoryTable() {
      this(null);
   }

   public EntityTestHistoryTable(TestResultFilter var1) {
      this.filter = var1 != null ? var1 : TestResultFilter.ACCEPTS_ALL;
      this.model = this.createEmptyTableModel();
      this.table = this.createTable(this.model);
   }

   @Override
   public void dispose() {
      this.uninstallResultStoreListener();
   }

   public void installActions(SelectionAction... var1) {
      this.table.installActions(var1);
      if (var1.length > 0) {
         this.table.setDefaultAction(var1[0]);
      }
   }

   public void setHistory(TestResultHistory<T> var1, IntegrationEntityLookup var2) {
      this.uninstallResultStoreListener();
      this.history = var1;
      this.entityLookup = var2;
      this.recreateAndApplyModel(true);
      this.installResultStoreListener();
   }

   protected EntityTestHistoryTableModel createEmptyTableModel() {
      return new EntityTestHistoryTableModel();
   }

   private void populateModel(EntityTestHistoryTableModel var1) {
      if (this.history != null) {
         var1.setEntityLookup(this.entityLookup);
         List var2 = this.history.getStoredResults(this.filter);
         Comparator var3 = Collections.reverseOrder(TestResultComparators.BY_TIMESTAMP);
         Collections.sort(var2, var3);

         for (Object __etlItem73 : var2) {
            TestResult var5 = (TestResult) __etlItem73;
            var1.addRow(var5);
         }
      }
   }

   private KongaRowTable createTable(EntityTestHistoryTableModel var1) {
      KongaTable var2 = this.createRawTable(var1);
      return new KongaRowTable(var2);
   }

   private KongaTable createRawTable(EntityTestHistoryTableModel var1) {
      TableFactory var2 = ComponentFactories.tableFactory();
      KongaTable var3 = var2.newSortedTable(var1, 10);
      var3.setComparator(var1.getEntityColumnIndex(), new EntityTypeAndNameSorter());
      EntityTestHistoryTableRenderers.install(var3);
      return var3;
   }

   private void installResultStoreListener() {
      if (this.history != null && this.historyListener == null) {
         this.historyListener = new EntityTestHistoryTable.ResultStoreListener();
         this.history.addTestResultStoreListener(this.historyListener);
      }
   }

   private void uninstallResultStoreListener() {
      if (this.history != null && this.historyListener != null) {
         this.history.removeTestResultStoreListener(this.historyListener);
         this.historyListener = null;
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

   public void setFilter(TestResultFilter var1) {
      if (var1 == null) {
         var1 = TestResultFilter.ACCEPTS_ALL;
      }

      if (var1 != this.filter) {
         this.filter = var1;
         this.recreateAndApplyModel(false);
      }
   }

   private void recreateAndApplyModel(final boolean var1) {
      this.model = this.createEmptyTableModel();
      this.populateModel(this.model);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EntityTestHistoryTable.this.table.setModel(EntityTestHistoryTable.this.model);
            if (var1) {
               EntityTestHistoryTable.this.table.selectFirstRow();
            }
         }
      });
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public SelectionAction getDefaultAction() {
      return this.table.getDefaultAction();
   }

   private final class ResultStoreListener implements TestResultHistoryListener {
      private ResultStoreListener() {
      }

      @Override
      public void onResultAdded(TestResult var1) {
         if (EntityTestHistoryTable.this.filter.accepts(var1)) {
            this.insertNewRow(var1);
         }
      }

      private void insertNewRow(final TestResult var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EntityTestHistoryTable.this.model.addRowAt(0, var1);
            }
         });
      }

      @Override
      public void onResultRemoved(final TestResult var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (int var1x = 0; var1x < EntityTestHistoryTable.this.model.getRowCount(); var1x++) {
                  if (EntityTestHistoryTable.this.model.getRowObjectAt(var1x) == var1) {
                     EntityTestHistoryTable.this.model.deleteRow(var1x);
                     break;
                  }
               }
            }
         });
      }

      @Override
      public void onHistoryRebuilt() {
         EntityTestHistoryTable.this.recreateAndApplyModel(true);
      }
   }
}
