package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.event.UndoableEditListeners;

public final class TableUndoSupport {
   private final KongaRowTableModel model;
   private final UndoableEditListeners listeners = new UndoableEditListeners();
   private boolean isUndoingOrRedoing;
   private boolean isBulkChange;
   private CompoundEdit compoundEdit;

   public TableUndoSupport(KongaRowTableModel var1) {
      this.model = Preconditions.checkNotNull(var1);
   }

   public boolean isUndoingOrRedoing() {
      return this.isUndoingOrRedoing;
   }

   public void onRowAdded(Object var1, int var2) {
      if (!this.isUndoingOrRedoing) {
         TableUndoSupport.RowAddedEdit var3 = new TableUndoSupport.RowAddedEdit(this.model, this, var1, var2);
         this.handleEdit(var3);
      }
   }

   public void onRowDeleted(Object var1, int var2) {
      if (!this.isUndoingOrRedoing) {
         TableUndoSupport.RowDeletedEdit var3 = new TableUndoSupport.RowDeletedEdit(this.model, this, var1, var2);
         this.handleEdit(var3);
      }
   }

   public void onCellValueChanged(int var1, int var2, Object var3, Object var4) {
      if (!this.isUndoingOrRedoing) {
         TableUndoSupport.ValueChangedEdit var5 = new TableUndoSupport.ValueChangedEdit(this.model, this, var1, var2, var3, var4);
         this.handleEdit(var5);
      }
   }

   public void addListener(UndoableEditListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(UndoableEditListener var1) {
      this.listeners.remove(var1);
   }

   public void removeAllListeners() {
      this.listeners.removeAll();
   }

   public void startBulkChange() {
      this.isBulkChange = true;
   }

   public void stopBulkChange() {
      this.isBulkChange = false;
      if (this.compoundEdit != null) {
         this.compoundEdit.end();
         this.fireUndoableEdit(this.compoundEdit);
         this.compoundEdit = null;
      }
   }

   private void handleEdit(UndoableEdit var1) {
      if (this.isBulkChange) {
         if (this.compoundEdit == null) {
            this.compoundEdit = new CompoundEdit();
         }

         this.compoundEdit.addEdit(var1);
      } else {
         this.fireUndoableEdit(var1);
      }
   }

   private void fireUndoableEdit(UndoableEdit var1) {
      this.listeners.call().undoableEditHappened(new UndoableEditEvent(this, var1));
   }

   private abstract static class ModelEdit extends AbstractUndoableEdit {
      private final KongaRowTableModel model;
      private final TableUndoSupport support;

      public ModelEdit(KongaRowTableModel var1, TableUndoSupport var2) {
         this.model = var1;
         this.support = var2;
      }

      protected final KongaRowTableModel getModel() {
         return this.model;
      }

      @Override
      public final void redo() throws CannotRedoException {
         this.support.isUndoingOrRedoing = true;

         try {
            super.redo();
            this.redoImpl();
         } finally {
            this.support.isUndoingOrRedoing = false;
         }
      }

      protected abstract void redoImpl();

      @Override
      public final void undo() throws CannotUndoException {
         this.support.isUndoingOrRedoing = true;

         try {
            super.undo();
            this.undoImpl();
         } finally {
            this.support.isUndoingOrRedoing = false;
         }
      }

      protected abstract void undoImpl();
   }

   private static class RowAddedEdit extends TableUndoSupport.ModelEdit {
      private final Object rowObject;
      private final int rowIndex;

      public RowAddedEdit(KongaRowTableModel var1, TableUndoSupport var2, Object var3, int var4) {
         super(var1, var2);
         this.rowObject = var3;
         this.rowIndex = var4;
      }

      @Override
      protected void redoImpl() {
         this.getModel().addRow(this.rowObject);
      }

      @Override
      protected void undoImpl() {
         this.getModel().deleteRow(this.rowIndex);
      }
   }

   private static class RowDeletedEdit extends TableUndoSupport.ModelEdit {
      private final Object rowObject;
      private final int rowIndex;

      public RowDeletedEdit(KongaRowTableModel var1, TableUndoSupport var2, Object var3, int var4) {
         super(var1, var2);
         this.rowObject = var3;
         this.rowIndex = var4;
      }

      @Override
      protected void redoImpl() {
         this.getModel().deleteRow(this.rowIndex);
      }

      @Override
      protected void undoImpl() {
         if (this.rowIndex >= this.getModel().getRowCount()) {
            this.getModel().addRow(this.rowObject);
         } else {
            this.getModel().addRowAt(this.rowIndex, this.rowObject);
         }
      }
   }

   private static class ValueChangedEdit extends TableUndoSupport.ModelEdit {
      private final int rowIndex;
      private final int columnIndex;
      private final Object oldValue;
      private final Object newValue;

      public ValueChangedEdit(KongaRowTableModel var1, TableUndoSupport var2, int var3, int var4, Object var5, Object var6) {
         super(var1, var2);
         this.rowIndex = var3;
         this.columnIndex = var4;
         this.oldValue = var5;
         this.newValue = var6;
      }

      @Override
      protected void redoImpl() {
         this.apply(this.newValue);
      }

      @Override
      protected void undoImpl() {
         this.apply(this.oldValue);
      }

      private void apply(Object var1) {
         this.getModel().setValueAt(var1, this.rowIndex, this.columnIndex);
      }
   }
}
