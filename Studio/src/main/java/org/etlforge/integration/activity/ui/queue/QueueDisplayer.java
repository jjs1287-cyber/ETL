package org.etlforge.integration.activity.ui.queue;

import javax.swing.JComponent;
import org.etlforge.integration.activity.ui.component.filter.FilterDescriptionPanel;
import org.etlforge.integration.activity.ui.log.OperationLogDisplayer;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.integration.activity.ui.util.DeletableTableActionsFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;

final class QueueDisplayer implements UiProvider {
   private final FilterDescriptionPanel filterDescription;
   private final OperationLogDisplayer displayer;
   private final DeletableTableAction selectAllAction;
   private final DeletableTableAction unselectAllAction;
   private final DeleteQueueAction deleteAction;

   public QueueDisplayer(OperationQueueModel var1, QueueFilters var2, DeleteQueueAction var3) {
      this.deleteAction = var3;
      this.filterDescription = new FilterDescriptionPanel(var2);
      this.displayer = new OperationLogDisplayer(var1, this.filterDescription, getColumnsToInclude());
      this.selectAllAction = DeletableTableActionsFactory.selectAll();
      this.unselectAllAction = DeletableTableActionsFactory.unselectAll();
      this.displayer.setBulkSelectionActions(this.selectAllAction, this.unselectAllAction);
   }

   private static OperationLogTableColumn[] getColumnsToInclude() {
      return new OperationLogTableColumn[]{
         OperationLogTableColumn.SELECT_CHECK,
         OperationLogTableColumn.OPERATION,
         OperationLogTableColumn.STATUS,
         OperationLogTableColumn.TIME_RECEIVED,
         OperationLogTableColumn.FROM
      };
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(this.displayer).south(this.createButtonBox());
      var2.decorate(var1);
   }

   private JComponent createButtonBox() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.add(this.selectAllAction).strut(5).add(this.unselectAllAction).strut(25).add(this.deleteAction).glue();
      return var1.withEmptyBorder(5, 5, 5, 5).container();
   }

   @Override
   public void requestFocus() {
      this.displayer.requestFocus();
   }
}
