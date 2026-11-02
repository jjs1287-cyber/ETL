package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentPage;
import org.etlforge.ui.action.ActionFactory;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableRowOrderControl;
import org.etlforge.ui.widget.table.TableRowOrderControlActions;

final class TableActions {
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final DocDefTable table;
   private final TableRowOrderControlActions rowOrderActions;

   TableActions(DocDefTable var1) {
      this.table = var1;
      TableRowOrderControl var2 = var1.getModel().getRowOrderControl();
      this.rowOrderActions = new TableRowOrderControlActions(var1.getTable(), var2);
   }

   Action getMoveUp() {
      Action var1 = this.rowOrderActions.getMoveUpAction();
      customize(var1, "Action.MoveUp", null);
      return var1;
   }

   Action getMoveDown() {
      Action var1 = this.rowOrderActions.getMoveDownAction();
      customize(var1, "Action.MoveDown", null);
      return var1;
   }

   Action getDelete() {
      AbstractAction var1 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TableActions.this.table.onDelete();
         }
      };
      TableActions.DeleteActionEnabler.install(this.table, var1);
      customize(var1, "Action.Delete", null);
      return var1;
   }

   Action getAddNew() {
      Action var1 = ActionFactory.forKey(KeyUtils.getCtrlDown(78), new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TableActions.this.table.onAddNew();
         }
      });
      customize(var1, "Action.AddNew", null);
      return var1;
   }

   private static void customize(Action var0, String var1, String var2) {
      String var3 = RESOURCES.getString(var1);
      Icon var4 = var2 == null ? null : (Icon)RESOURCES.getObject(var2);
      var0.putValue("Name", var3);
      var0.putValue("SmallIcon", var4);
   }

   private static final class DeleteActionEnabler implements ListSelectionListener {
      private final DocDefTable table;
      private final Action deleteAction;

      private DeleteActionEnabler(DocDefTable var1, Action var2) {
         this.table = var1;
         this.deleteAction = var2;
      }

      public static void install(DocDefTable var0, Action var1) {
         TableActions.DeleteActionEnabler var2 = new TableActions.DeleteActionEnabler(var0, var1);
         KongaTable var3 = var0.getTable();
         var3.getSelectionModel().addListSelectionListener(var2);
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            int[] var2 = this.table.getTable().getSelectedRows();
            if (var2.length == 0) {
               this.deleteAction.setEnabled(false);
            } else {
               this.checkDeleteActionEnabled(var2);
            }
         }
      }

      private void checkDeleteActionEnabled(int[] var1) {
         boolean var2 = false;
         DocDefTableModel var3 = this.table.getModel();

         for (int var7 : var1) {
            if (var3.canRowBeDeleted(var7)) {
               var2 = true;
               break;
            }
         }

         this.deleteAction.setEnabled(var2);
      }
   }
}
