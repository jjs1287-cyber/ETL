package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.system.Disposable;

public class TableRowOrderControlActions implements Disposable {
   private final KongaTable table;
   private final TableRowOrderControl control;
   private final TableRowOrderControlActions.MoveUpAction moveUpAction;
   private final TableRowOrderControlActions.MoveDownAction moveDownAction;
   private final ListSelectionListener selectionListener;

   public TableRowOrderControlActions(KongaTable var1, TableRowOrderControl var2) {
      Preconditions.checkNotNull(var1, "table");
      Preconditions.checkNotNull(var2, "ctrl");
      this.table = var1;
      this.control = var2;
      this.moveUpAction = new TableRowOrderControlActions.MoveUpAction();
      this.moveDownAction = new TableRowOrderControlActions.MoveDownAction();
      this.selectionListener = this.createSelectionListener();
      var1.getSelectionModel().addListSelectionListener(this.selectionListener);
   }

   @Override
   public void dispose() {
      this.table.getSelectionModel().removeListSelectionListener(this.selectionListener);
   }

   public Action getMoveUpAction() {
      return this.moveUpAction;
   }

   public Action getMoveDownAction() {
      return this.moveDownAction;
   }

   protected boolean isEnabled() {
      return true;
   }

   private ListSelectionListener createSelectionListener() {
      return new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent var1) {
            if (!var1.getValueIsAdjusting()) {
               int[] var2 = TableRowOrderControlActions.this.table.getSelectedRows();
               TableRowOrderControlActions.this.moveUpAction.updateEnabledState(var2);
               TableRowOrderControlActions.this.moveDownAction.updateEnabledState(var2);
            }
         }
      };
   }

   private class MoveDownAction extends AbstractAction {
      public MoveDownAction() {
         super(Strings.get("MoveDown"));
         this.putValue("ShortDescription", Strings.get("TableRowOrderControlActions.MoveDown.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            int[] var2 = TableRowOrderControlActions.this.table.getSelectedRows();
            TableRowOrderControlActions.this.table.clearSelection();

            for (int var3 = var2.length - 1; var3 >= 0; var3--) {
               int var4 = var2[var3];
               this.moveRowDown(var4);
            }
         } finally {
            this.updateEnabledState();
         }
      }

      private void moveRowDown(int var1) {
         if (TableRowOrderControlActions.this.control.canRowMoveDown(var1)) {
            TableRowOrderControlActions.this.control.moveRowDown(var1);
            TableRowOrderControlActions.this.table.addRowSelectionInterval(var1 + 1, var1 + 1);
         }
      }

      private void updateEnabledState() {
         this.updateEnabledState(TableRowOrderControlActions.this.table.getSelectedRows());
      }

      public void updateEnabledState(int[] var1) {
         boolean var2 = TableRowOrderControlActions.this.isEnabled();
         if (var2) {
            var2 = var1.length > 0;

            for (int var6 : var1) {
               if (!TableRowOrderControlActions.this.control.canRowMoveDown(var6)) {
                  var2 = false;
                  break;
               }
            }
         }

         this.setEnabled(var2);
      }
   }

   private class MoveUpAction extends AbstractAction {
      public MoveUpAction() {
         super(Strings.get("MoveUp"));
         this.putValue("ShortDescription", Strings.get("TableRowOrderControlActions.MoveUp.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            int[] var2 = TableRowOrderControlActions.this.table.getSelectedRows();
            TableRowOrderControlActions.this.table.clearSelection();

            for (int var6 : var2) {
               this.moveRowUp(var6);
            }
         } finally {
            this.updateEnabledState();
         }
      }

      private void moveRowUp(int var1) {
         if (TableRowOrderControlActions.this.control.canRowMoveUp(var1)) {
            TableRowOrderControlActions.this.control.moveRowUp(var1);
            TableRowOrderControlActions.this.table.addRowSelectionInterval(var1 - 1, var1 - 1);
         }
      }

      private void updateEnabledState() {
         this.updateEnabledState(TableRowOrderControlActions.this.table.getSelectedRows());
      }

      public void updateEnabledState(int[] var1) {
         boolean var2 = TableRowOrderControlActions.this.isEnabled();
         if (var2) {
            var2 = var1.length > 0;

            for (int var6 : var1) {
               if (!TableRowOrderControlActions.this.control.canRowMoveUp(var6)) {
                  var2 = false;
                  break;
               }
            }
         }

         this.setEnabled(var2);
      }
   }
}
