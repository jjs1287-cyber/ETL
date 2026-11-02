package org.etlforge.ui.widget.table;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.event.UndoableEditListeners;
import org.etlforge.ui.undo.KongaCompoundEdit;

public abstract class AbstractKongaTableModel implements KongaRowTableModel {
   private final List<TableRow> tableRows = Lists.newArrayList();
   private int numberOfRows;
   private final List<TableModelListener> modelListeners = new CopyOnWriteArrayList<>();
   private final TableColumnMetaData[] columnDescriptors;
   private final BiMap<TableColumnMetaData, Integer> columnIndeces;
   private boolean undoSupported;
   private boolean inUndoOrRedo;
   private UndoableEditListeners undoListeners;
   private Stack<KongaCompoundEdit> compoundEdits;

   protected AbstractKongaTableModel(TableColumnMetaData... var1) {
      this.columnDescriptors = (TableColumnMetaData[])var1.clone();
      this.columnIndeces = this.createColumnIndexMap();
   }

   private BiMap<TableColumnMetaData, Integer> createColumnIndexMap() {
      HashBiMap var1 = HashBiMap.create(this.columnDescriptors.length);
      int var2 = 0;

      for (TableColumnMetaData var6 : this.columnDescriptors) {
         var1.put(var6, var2++);
      }

      return var1;
   }

   @Override
   public final TableColumnMetaData[] getColumnDescriptors() {
      return (TableColumnMetaData[])this.columnDescriptors.clone();
   }

   @Override
   public final TableColumnMetaData getColumnDescriptor(int var1) {
      return this.columnIndeces.inverse().get(var1);
   }

   protected final boolean containsColumn(TableColumnMetaData var1) {
      return this.columnIndeces.containsKey(var1);
   }

   protected abstract Object[] getColumnDataFromObject(Object var1);

   protected boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
      return true;
   }

   @Override
   public Object getRowObjectAt(int var1) {
      TableRow var2 = this.tableRows.get(var1);
      return var2.getRowObject();
   }

   private TableRow createRowFromObject(Object var1) {
      Object[] var2 = this.getColumnDataFromObject(var1);
      if (var2 == null) {
         return null;
      }

      if (var2.length != this.getColumnCount()) {
         throw new RuntimeException(
            "Unexpected number of values returned from getColumnDataFromObject(Object). Returned values: "
               + var2.length
               + ", expected: "
               + this.getColumnCount()
         );
      }

      TableRow var3 = new TableRow(this.columnIndeces, var1);

      for (int var4 = 0; var4 < var2.length; var4++) {
         var3.setColumnValue(this.columnDescriptors[var4], var2[var4]);
      }

      return var3;
   }

   protected Object convertToRowObject(Object var1) {
      return var1;
   }

   @Override
   public void addRow(Object var1) {
      Object var2 = this.convertToRowObject(var1);
      TableRow var3 = this.createRowFromObject(var2);
      if (var3 != null) {
         this.insertTableRowsAt(this.numberOfRows, Collections.singletonList(var3));
      }
   }

   @Override
   public void addRowAt(int var1, Object var2) {
      Object var3 = this.convertToRowObject(var2);
      TableRow var4 = this.createRowFromObject(var3);
      if (var4 != null) {
         this.insertTableRowsAt(var1, Collections.singletonList(var4));
      }
   }

   public void addRows(Collection<?> var1) {
      this.addRows(var1.toArray());
   }

   public void addRows(Object[] var1) {
      if (var1.length != 0) {
         ArrayList var2 = Lists.newArrayList();
         ArrayList var3 = Lists.newArrayList();

         for (Object var7 : var1) {
            Object var8 = this.convertToRowObject(var7);
            TableRow var9 = this.createRowFromObject(var8);
            if (var9 != null) {
               var2.add(var9);
               var3.add(var8);
            }
         }

         this.insertTableRowsAt(this.numberOfRows, var2);
      }
   }

   public void addRowsAt(int var1, Collection<?> var2) {
      if (!var2.isEmpty()) {
         ArrayList var3 = Lists.newArrayList();
         ArrayList var4 = Lists.newArrayList();

         for (Object var6 : var2) {
            TableRow var7 = this.createRowFromObject(var6);
            if (var7 != null) {
               var3.add(var7);
               var4.add(var6);
            }
         }

         this.insertTableRowsAt(var1, var3);
      }
   }

   private void insertTableRowsAt(int var1, List<? extends TableRow> var2) {
      this.tableRows.addAll(var1, var2);
      this.numberOfRows = this.numberOfRows + var2.size();
      this.fireRowsInserted(var1, var1 + var2.size() - 1);
      if (this.isChangeUndoable()) {
         RowsAddedEdit var3 = new RowsAddedEdit(this, var1, var2);
         this.handleEdit(var3);
      }
   }

   private boolean isChangeUndoable() {
      return this.undoSupported && !this.inUndoOrRedo;
   }

   public void addEmptyRow() {
      EmptyTableRow var1 = new EmptyTableRow(this.columnIndeces);
      this.insertTableRowsAt(this.numberOfRows, Collections.singletonList(var1));
   }

   public void addEmptyRowAt(int var1) {
      EmptyTableRow var2 = new EmptyTableRow(this.columnIndeces);
      this.insertTableRowsAt(var1, Collections.singletonList(var2));
   }

   @Override
   public void deleteRow(int var1) {
      TableRow var2 = this.tableRows.remove(var1);
      this.numberOfRows--;
      this.fireRowDeleted(var1);
      if (this.isChangeUndoable()) {
         RowsDeletedEdit var3 = new RowsDeletedEdit(this, var1, Collections.singletonList(var2));
         this.handleEdit(var3);
      }
   }

   public void deleteRows(int var1, int var2) {
      ArrayList var3 = Lists.newArrayList();

      for (int var4 = var2; var4 >= var1; var4--) {
         TableRow var5 = this.tableRows.remove(var4);
         var3.add(var5);
         this.numberOfRows--;
      }

      this.fireRowsDeleted(var1, var2);
      if (this.isChangeUndoable()) {
         RowsDeletedEdit var6 = new RowsDeletedEdit(this, var1, var3);
         this.handleEdit(var6);
      }
   }

   public void deleteAll() {
      if (this.getRowCount() != 0) {
         ArrayList var1 = null;
         if (this.isChangeUndoable()) {
            var1 = Lists.newArrayList(this.tableRows);
         }

         byte var2 = 0;
         int var3 = this.getRowCount() - 1;
         this.tableRows.clear();
         this.numberOfRows = 0;
         this.fireRowsDeleted(var2, var3);
         if (this.isChangeUndoable()) {
            RowsDeletedEdit var4 = new RowsDeletedEdit(this, 0, var1);
            this.handleEdit(var4);
         }
      }
   }

   @Override
   public Class<?> getColumnClass(int var1) {
      if (var1 >= this.getColumnCount()) {
         throw new IndexOutOfBoundsException(var1 + " >= " + this.getColumnCount());
      } else {
         return this.columnDescriptors[var1].getDataClass();
      }
   }

   @Override
   public int getColumnCount() {
      return this.columnDescriptors.length;
   }

   @Override
   public String getColumnName(int var1) {
      if (var1 >= this.getColumnCount()) {
         throw new IndexOutOfBoundsException(var1 + " >= " + this.getColumnCount());
      } else {
         return this.columnDescriptors[var1].getName();
      }
   }

   @Override
   public int getRowCount() {
      return this.numberOfRows;
   }

   @Override
   public Object getValueAt(int var1, int var2) {
      return this.getRowAt(var1).getColumn(var2).getValue();
   }

   @Override
   public Object getValueAt(int var1, TableColumnMetaData var2) {
      return this.getRowAt(var1).getColumn(var2).getValue();
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      return !(this.getRowAt(var1) instanceof EmptyTableRow) && this.columnDescriptors[var2].isEditable();
   }

   @Override
   public void addTableModelListener(TableModelListener var1) {
      this.modelListeners.add(var1);
   }

   @Override
   public void removeTableModelListener(TableModelListener var1) {
      this.modelListeners.remove(var1);
   }

   public final void setValueAt(Object var1, int var2, TableColumnMetaData var3) {
      this.setValueAt(var1, var2, this.columnIndeces.get(var3));
   }

   @Override
   public void setValueAt(Object var1, int var2, int var3) {
      if (this.isCellEditable(var2, var3)) {
         Object var4 = null;
         if (this.isChangeUndoable()) {
            var4 = this.getValueAt(var2, var3);
         }

         this.getRowAt(var2).setColumnValue(var3, var1);
         boolean var5 = this.updateObjectWithColumnData(this.getRowObjectAt(var2), var2, var3, var1);
         if (var5) {
            this.fireTableChanged(new TableModelEvent(this, var2, var2, -1, 0));
            if (this.isCellEdit(var1, var4)) {
               TableCellEdit var6 = new TableCellEdit(this, var2, var3, var4, var1);
               this.handleEdit(var6);
            }
         }
      }
   }

   private boolean isCellEdit(Object var1, Object var2) {
      return this.isChangeUndoable() && !Objects.equal(var2, var1);
   }

   private void handleEdit(UndoableEdit var1) {
      if (this.compoundEdits != null && !this.compoundEdits.isEmpty()) {
         this.compoundEdits.peek().addEdit(var1);
      } else {
         this.undoListeners.call().undoableEditHappened(new UndoableEditEvent(this, var1));
      }
   }

   private TableRow getRowAt(int var1) {
      if (var1 >= 0 && var1 < this.getRowCount()) {
         return this.tableRows.get(var1);
      }

      String var2 = "Number of rows: " + this.getRowCount() + ". Supplied index: " + var1;
      throw new IndexOutOfBoundsException(var2);
   }

   void undoOrRedoCellEdit(int var1, int var2, Object var3) {
      this.inUndoOrRedo = true;
      this.setValueAt(var3, var1, var2);
      this.inUndoOrRedo = false;
   }

   protected final void fireRowInserted(int var1) {
      this.fireRowsInserted(var1, var1);
   }

   protected final void fireRowsInserted(int var1, int var2) {
      this.fireTableChanged(new TableModelEvent(this, var1, var2, -1, 1));
   }

   protected final void fireRowDeleted(int var1) {
      this.fireRowsDeleted(var1, var1);
   }

   protected final void fireRowsDeleted(int var1, int var2) {
      this.fireTableChanged(new TableModelEvent(this, var1, var2, -1, -1));
   }

   protected final void fireTableChanged(TableModelEvent var1) {
      for (TableModelListener var3 : this.modelListeners) {
         var3.tableChanged(var1);
      }
   }

   protected final void throwIllegalRowObject(Object var1, Class<?> var2) throws IllegalArgumentException {
      String var3 = "o must be an instance of " + var2.getName() + " (was " + (var1 == null ? "null" : var1.getClass().getName() + ")");
      throw new IllegalArgumentException(var3);
   }

   public final void setUndoSupported(boolean var1) {
      if (var1 != this.undoSupported) {
         this.undoSupported = var1;
         if (var1) {
            this.undoListeners = new UndoableEditListeners();
         } else {
            this.undoListeners = null;
         }
      }
   }

   public final boolean isUndoSupported() {
      return this.undoSupported;
   }

   public final void startCompoundEdit() {
      if (this.isChangeUndoable()) {
         if (this.compoundEdits == null) {
            this.compoundEdits = new Stack<>();
         }

         this.compoundEdits.push(new KongaCompoundEdit());
      }
   }

   private void ensureUndoRedoSupported() {
      if (!this.undoSupported) {
         throw new UnsupportedOperationException("This table does not support undo-redo.");
      }
   }

   public final void stopCompoundEdit() {
      if (this.isChangeUndoable() && this.isInCompoundEdit()) {
         KongaCompoundEdit var1 = this.compoundEdits.pop();
         var1.end();
         if (!var1.isEmpty()) {
            this.handleEdit(var1);
         }
      }
   }

   protected final boolean isInCompoundEdit() {
      return this.compoundEdits != null && !this.compoundEdits.isEmpty();
   }

   public final void addUndoableEditListener(UndoableEditListener var1) {
      this.ensureUndoRedoSupported();
      this.undoListeners.add(var1);
   }

   public final void removeUndoableEditorListener(UndoableEditListener var1) {
      this.ensureUndoRedoSupported();
      this.undoListeners.remove(var1);
   }

   void undoAddedRows(int var1, int var2) {
      this.inUndoOrRedo = true;
      this.deleteRows(var1, var1 + var2 - 1);
      this.inUndoOrRedo = false;
   }

   void redoAddedRows(int var1, List<? extends TableRow> var2) {
      this.inUndoOrRedo = true;
      this.insertTableRowsAt(var1, var2);
      this.inUndoOrRedo = false;
   }

   void undoDeletedRows(int var1, List<? extends TableRow> var2) {
      this.redoAddedRows(var1, var2);
   }

   void redoDeletedRows(int var1, int var2) {
      this.undoAddedRows(var1, var2);
   }

   @Override
   public final int indexOfColumn(TableColumnMetaData var1) {
      Integer var2 = this.columnIndeces.get(var1);
      if (var2 == null) {
         throw new RuntimeException("Unknown column: " + var1);
      } else {
         return var2;
      }
   }
}
