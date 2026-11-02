package org.etlforge.integration.client.ui.interchange.testconnection;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.table.TableRowSorter;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.ui.entity.testhistory.EntityTestHistoryTableModel;
import org.etlforge.integration.client.ui.entity.testhistory.EntityTestHistoryTableRenderers;
import org.etlforge.integration.client.ui.entity.testhistory.OpenTestedEntityAction;
import org.etlforge.integration.client.ui.entity.testhistory.ViewTestResultDetailsAction;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TypedKongaRowTablePredicate;

public final class TestAllConnectionsResultTable implements UiProvider {
   private final EntityTestHistoryTableModel model;
   private final KongaRowTable table;
   private final ViewTestResultDetailsAction viewDetailsAction;
   private final OpenTestedEntityAction openEntityAction;
   private final TableRowSorter<EntityTestHistoryTableModel> sorter;

   public TestAllConnectionsResultTable(IntegrationEntityLookup var1) {
      this.model = this.createNewModel(var1);
      this.table = this.createTable();
      this.sorter = new TableRowSorter<>(this.model);
      this.table.getAdaptee().setRowSorter(this.sorter);
      this.viewDetailsAction = new ViewTestResultDetailsAction();
      this.viewDetailsAction.setEntityLookup(var1);
      this.table.setDefaultAction(this.viewDetailsAction);
      this.openEntityAction = new OpenTestedEntityAction(var1);
      this.openEntityAction.setSelectionSource(this.table);
      PopupMenuRequestHandlers.install(this.table.getAdaptee(), this.openEntityAction, this.viewDetailsAction);
   }

   private EntityTestHistoryTableModel createNewModel(IntegrationEntityLookup var1) {
      EntityTestHistoryTableModel var2 = new EntityTestHistoryTableModel(false);
      var2.setEntityLookup(var1);
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setNewEntityLookup(final IntegrationEntityLookup var1) {
      Preconditions.checkNotNull(var1, "entityLookup");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TestAllConnectionsResultTable.this.removeAll();
            TestAllConnectionsResultTable.this.model.setEntityLookup(var1);
            TestAllConnectionsResultTable.this.viewDetailsAction.setEntityLookup(var1);
            TestAllConnectionsResultTable.this.openEntityAction.setEntityLookup(var1);
         }
      });
   }

   public void addResult(final TestConnectionResult var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TestAllConnectionsResultTable.this.model.addRow(var1);
            TestAllConnectionsResultTable.this.ensureLastRowVisible();
         }
      });
   }

   public SelectionAction getViewDetailsAction() {
      return this.viewDetailsAction;
   }

   public SelectionAction getOpenEntityAction() {
      return this.openEntityAction;
   }

   private void ensureLastRowVisible() {
      assert EventQueue.isDispatchThread();
      int var1 = this.model.getRowCount();
      if (var1 != 0) {
         Rectangle var2 = this.table.getAdaptee().getCellRect(var1 - 1, 0, true);
         this.table.getAdaptee().scrollRectToVisible(var2);
      }
   }

   public void removeAll() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TestAllConnectionsResultTable.this.model.deleteAll();
         }
      });
   }

   private KongaRowTable createTable() {
      KongaTable var1 = this.createRawTable(this.model);
      return new KongaRowTable(var1);
   }

   private KongaTable createRawTable(EntityTestHistoryTableModel var1) {
      TableFactory var2 = ComponentFactories.tableFactory();
      KongaTable var3 = var2.newSortedTable(var1, 10);
      var3.setComparator(var1.getEntityColumnIndex(), new EntityTypeAndNameSorter());
      var3.setSelectionMode(0);
      EntityTestHistoryTableRenderers.install(var3);
      TableStyles.normal().makeOver(var3);
      return var3;
   }

   public void setFilter(Predicate<EntityTestResult> var1) {
      if (var1 == null) {
         this.sorter.setRowFilter(null);
      } else {
         TypedKongaRowTablePredicate var2 = TypedKongaRowTablePredicate.create(var1);
         this.sorter.setRowFilter(var2);
      }
   }

   public boolean hasFailedConnections() {
      for (int var1 = 0; var1 < this.model.getRowCount(); var1++) {
         if (this.model.getRowObjectAt(var1).getOutcome() == TestResult.Outcome.ERROR) {
            return true;
         }
      }

      return false;
   }

   public Set<IntegrationEntityId> getFailedConnections() {
      HashSet var1 = Sets.newHashSet();

      for (int var2 = 0; var2 < this.model.getRowCount(); var2++) {
         EntityTestResult var3 = this.model.getRowObjectAt(var2);
         if (var3.getOutcome() == TestResult.Outcome.ERROR) {
            var1.add(var3.getSubjectId());
         }
      }

      return var1;
   }
}
