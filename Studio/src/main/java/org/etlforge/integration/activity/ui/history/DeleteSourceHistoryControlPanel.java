package org.etlforge.integration.activity.ui.history;

import javax.swing.JComponent;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.integration.activity.ui.util.DeletableTableActionsFactory;
import org.etlforge.integration.activity.ui.util.DeletableTableModel;
import org.etlforge.ui.layout.BoxBuilder;

final class DeleteSourceHistoryControlPanel {
   private final DeletableTableAction selectAllAction;
   private final DeletableTableAction unselectAllAction;
   private final DeleteHistoryAction deleteAction;

   public DeleteSourceHistoryControlPanel(DeleteHistoryAction var1) {
      this.deleteAction = var1;
      this.selectAllAction = DeletableTableActionsFactory.selectAll();
      this.unselectAllAction = DeletableTableActionsFactory.unselectAll();
   }

   public void setTableModel(DeletableTableModel var1) {
      this.selectAllAction.setTableModel(var1);
      this.unselectAllAction.setTableModel(var1);
   }

   public JComponent getUi() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.add(this.selectAllAction).strut(5).add(this.unselectAllAction).strut(25).add(this.deleteAction).glue();
      return var1.withEmptyBorder(5).container();
   }
}
