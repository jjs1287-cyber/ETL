package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.ui.widget.button.KongaButton;

public class FixedWidthFieldTable extends FieldTable {
   public static final boolean RECALCULATE_ON_DELETE = false;
   private Action recalculateAction;

   public FixedWidthFieldTable(FixedWidthFieldTableModel var1) {
      super(var1);
   }

   @Override
   protected final void createActions(DocDefTableUi var1) {
      super.createActions(var1);
      DocDefTableModel var2 = this.getModel();
      if (var2 instanceof FixedWidthFieldTableModel) {
         this.recalculateAction = new FixedWidthFieldTable.RecalculateAction();
         this.recalculateAction.setEnabled(var2.getRowCount() > 0);
      }
   }

   @Override
   protected final List<JButton> getButtons() {
      List var1 = super.getButtons();
      if (this.recalculateAction != null) {
         var1.add(new KongaButton(this.recalculateAction));
      }

      return var1;
   }

   @Override
   protected final void onAddNew() {
      super.onAddNew();
      if (this.recalculateAction != null) {
         this.recalculateAction.setEnabled(this.getModel().getRowCount() > 0);
      }
   }

   @Override
   protected final void onDelete() {
      super.onDelete();
      if (this.recalculateAction != null) {
         if (this.getModel().getRowCount() > 0) {
            this.recalculateAction.setEnabled(true);
         } else {
            this.recalculateAction.setEnabled(false);
         }
      }
   }

   private class RecalculateAction extends AbstractAction {
      public RecalculateAction() {
         super(DocDefTable.RESOURCES.getString("Action.Calculate"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ((FixedWidthFieldTableModel)FixedWidthFieldTable.this.getModel()).recalculate();
      }
   }
}
