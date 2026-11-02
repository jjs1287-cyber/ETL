package org.etlforge.integration.client.database.ui.sqlbuilder;

import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.selection.AbstractSelectionAction;

public final class TransferSelectedNodesAction extends AbstractSelectionAction {
   private final SqlInputArea inputArea;

   public TransferSelectedNodesAction(SqlInputArea var1) {
      this.inputArea = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      DbObjectsViewSelection var2 = (DbObjectsViewSelection)this.getSelection();
      List var3 = var2.getTables();
      if (!var3.isEmpty()) {
         this.handleTables(var3);
      } else {
         List var4 = var2.getColumns();
         if (!var4.isEmpty()) {
            this.handleColumns(var4);
         }
      }
   }

   private void handleTables(List<DatabaseObject> var1) {
      if (var1.size() == 1) {
         this.inputArea.insertTable((DatabaseObject)var1.get(0));
      }
   }

   private void handleColumns(List<DatabaseColumn> var1) {
      this.inputArea.insertColumns(var1);
   }
}
