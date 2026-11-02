package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

final class KeyStrokeDeleteAction extends AbstractAction {
   private final DocDefTableUi ui;
   private final KeyStrokeDeleteAction.TableProvider tableProvider;

   KeyStrokeDeleteAction(DocDefTableUi var1, KeyStrokeDeleteAction.TableProvider var2) {
      this.ui = var1;
      this.tableProvider = var2;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (!this.couldEdit()) {
         Action var2 = this.tableProvider.getTable().getDeleteAction();
         var2.actionPerformed(var1);
      }
   }

   private boolean couldEdit() {
      if (this.ui.getSelectedRowCount() > 1) {
         return false;
      }

      int var1 = this.ui.getSelectedRow();
      return var1 == -1 ? false : this.processEditOnSelectedColumn(var1);
   }

   private boolean processEditOnSelectedColumn(int var1) {
      int var2 = this.ui.getSelectedColumn();
      if (var2 != -1 && this.ui.editCellAt(var1, var2)) {
         this.processEdit();
         return true;
      } else {
         return false;
      }
   }

   private void processEdit() {
      TableCellEditor var1 = this.ui.getCellEditor();
      if (var1 instanceof DefaultCellEditor) {
         Component var2 = ((DefaultCellEditor)var1).getComponent();
         if (var2 instanceof JTextComponent) {
            ((JTextComponent)var2).setText("");
         }
      }
   }

   interface TableProvider {
      DocDefTable getTable();
   }
}
