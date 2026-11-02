package org.etlforge.integration.activity.ui.log.tree;

import com.google.common.base.Function;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.concurrent.Callable;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.tree.TreePath;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.integration.activity.ui.log.LogDisplayMode;
import org.etlforge.integration.activity.ui.log.LogEntryWidget;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.KongaScrollPane;

public final class LogEntryTreeWidget implements LogEntryWidget {
   private final AbstractOperationLogModel model;
   private final OperationLogTableColumn[] columnsToInclude;
   private final OperationLogTree tree = new OperationLogTree();
   private final PropertyChangeListener modelListener;
   private DeletableTableAction selectAllAction;
   private DeletableTableAction unselectAllAction;

   public LogEntryTreeWidget(AbstractOperationLogModel var1, OperationLogTableColumn... var2) {
      this.model = var1;
      this.columnsToInclude = (OperationLogTableColumn[])var2.clone();
      this.tree.setNodeToObjectConversion(new Function<Object, OperationLogEntry>() {
         public OperationLogEntry apply(Object var1) {
            return ((OperationNode)var1).getUserObject();
         }
      });
      this.modelListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            LogEntryTreeWidget.this.syncWithModel();
         }
      };
      var1.addPropertyChangeListener("entries", this.modelListener);
      this.syncWithModel();
   }

   private void syncWithModel() {
      LogEntryTreeWidget.TreeModelProducer var1 = new LogEntryTreeWidget.TreeModelProducer();
      LogEntryTreeWidget.TreeModelConsumer var2 = new LogEntryTreeWidget.TreeModelConsumer();
      Computation var3 = Computation.create(var1, var2);
      var3.swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, this.getClass())
         .withWaitService(this.model.getTalkingToServerWaitService())
         .buildAndExecute();
   }

   @Override
   public LogDisplayMode getMode() {
      return LogDisplayMode.TREE;
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener("entries", this.modelListener);
   }

   @Override
   public OperationLogEntry getSelectedLogEntry() {
      int[] var1 = this.tree.getSelectedRows();
      if (var1.length == 1) {
         TreePath var2 = this.tree.getPathForRow(var1[0]);
         return ((OperationNode)var2.getLastPathComponent()).getUserObject();
      } else {
         return null;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new KongaScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.tree.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.tree.removeSelectionListener(var1);
   }

   @Override
   public void setBulkSelectionActions(DeletableTableAction var1, DeletableTableAction var2) {
      this.selectAllAction = var1;
      this.unselectAllAction = var2;
      this.bindBulkSelectionActionsToModel((OperationLogTreeModel)this.tree.getTreeTableModel());
   }

   private void bindBulkSelectionActionsToModel(OperationLogTreeModel var1) {
      if (this.selectAllAction != null && this.unselectAllAction != null) {
         this.selectAllAction.setTableModel(var1);
         this.unselectAllAction.setTableModel(var1);
      }
   }

   private class TreeModelConsumer implements Receiver<OperationLogTreeModel> {
      private TreeModelConsumer() {
      }

      public void handle(OperationLogTreeModel var1) {
         LogEntryTreeWidget.this.bindBulkSelectionActionsToModel(var1);
         LogEntryTreeWidget.this.tree.setTreeTableModel(var1);
         LogEntryTreeWidget.this.tree.updateColumnWidths(LogEntryTreeWidget.this.columnsToInclude);
         this.installSorter(var1);
         LogEntryTreeWidget.this.tree.expandAll();
         LogEntryTreeWidget.this.tree.requestFocusInWindow();
      }

      private void installSorter(OperationLogTreeModel var1) {
         final LogTreeRowSorter var2 = new LogTreeRowSorter(LogEntryTreeWidget.this.tree);
         var2.setCurrentSortStatus(LogEntryTreeWidget.this.model.getSortStatus());
         var2.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent var1) {
               LogEntryTreeWidget.this.model.setSortStatus(var2.getSortStatus());
            }
         });
         LogEntryTreeWidget.this.tree.setRowSorter(var2);
      }
   }

   private class TreeModelProducer implements Callable<OperationLogTreeModel> {
      private TreeModelProducer() {
      }

      public OperationLogTreeModel call() {
         OperationLogTreeModel var1 = OperationLogTreeModel.build(LogEntryTreeWidget.this.model.getFilteredList(), LogEntryTreeWidget.this.columnsToInclude);
         this.sortTopLevelNodes(var1);
         if (LogEntryTreeWidget.this.model instanceof DeleteSelectionListener) {
            var1.addDeleteSelectionListener((DeleteSelectionListener)LogEntryTreeWidget.this.model);
         }

         return var1;
      }

      private void sortTopLevelNodes(OperationLogTreeModel var1) {
         Comparator var2 = LogEntryTreeWidget.this.model.getTopLevelOrder();
         if (var2 != null) {
            var1.sortTopLevelNodes(var2);
         }
      }
   }
}
