package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.table.TableCellEditor;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableEnumEditor;
import org.etlforge.ui.widget.table.TableStringEditor;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.util.SortOrder;

public final class WhereClauseTable {
   private final DatabaseObject dbObject;
   private WhereClauseTable.TableModelImpl tableModel;
   private final KongaRowTable table;

   public WhereClauseTable(DatabaseObject var1) {
      this.dbObject = var1;
      this.tableModel = this.createTableModel(var1);
      this.table = this.createTable();
   }

   private WhereClauseTable.TableModelImpl createTableModel(DatabaseObject var1) {
      WhereClauseTable.TableModelImpl var2 = new WhereClauseTable.TableModelImpl();

      for (DatabaseColumn var4 : var1.getAllColumns()) {
         var2.addRow(new WhereClauseTable.RowObject(var4));
      }

      return var2;
   }

   private KongaRowTable createTable() {
      TableFactory var1 = ComponentFactories.tableFactory();
      KongaTable var2 = var1.newTable(this.tableModel);
      TableStyles.normal().makeOver(var2);
      var2.setVisibleRowCount(Math.min(4, this.tableModel.getRowCount()));
      var2.setShowVerticalLines(true);
      var2.setTerminateEditOnFocusLost(true);
      this.installEditors(var2);
      return new KongaRowTable(var2);
   }

   private void installEditors(KongaTable var1) {
      TableStringEditor var2 = new TableStringEditor();
      var2.setClickCountToStart(1);
      var1.setCellEditorAt(1, var2);
      TableEnumEditor var3 = new TableEnumEditor(SortOrder.class);
      var3.setClickCountToStart(1);
      var1.setCellEditorAt(2, var3);
      var1.setTerminateEditOnFocusLost(true);
   }

   public DatabaseObject getDbObject() {
      return this.dbObject;
   }

   public UiProvider getUi() {
      return this.table;
   }

   public void clear() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            WhereClauseTable.this.tableModel = WhereClauseTable.this.createTableModel(WhereClauseTable.this.dbObject);
            WhereClauseTable.this.table.setModel(WhereClauseTable.this.tableModel);
         }
      });
   }

   public void terminateEdit() {
      TableCellEditor var1 = this.table.getAdaptee().getCellEditor();
      if (var1 != null) {
         var1.stopCellEditing();
      }
   }

   public void addFilter(final String var1, final String var2, final SortOrder var3) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            int var1x = WhereClauseTable.this.tableModel.getRowIndexFor(var1);
            if (var1x >= 0) {
               WhereClauseTable.this.tableModel.setValueAt(var2, var1x, 1);
               WhereClauseTable.this.tableModel.setValueAt(var3, var1x, 2);
            }
         }
      });
   }

   public void provideDefinedFilters(DbTranFilters var1) {
      for (int var2 = 0; var2 < this.tableModel.getRowCount(); var2++) {
         this.tableModel.getRowObjectAt(var2).provideDefinedFilters(var1);
      }
   }

   private static class RowObject {
      public final DatabaseColumn column;
      public String condition;
      public SortOrder order;

      public RowObject(DatabaseColumn var1) {
         this(var1, "", null);
      }

      public RowObject(DatabaseColumn var1, String var2, SortOrder var3) {
         this.column = var1;
         this.condition = var2;
         this.order = var3;
      }

      public void provideDefinedFilters(DbTranFilters var1) {
         if (this.hasFilter()) {
            var1.addEntry(this.column, this.condition, this.order);
         }
      }

      private boolean hasFilter() {
         return this.condition.length() > 0 || this.order != null && this.order != SortOrder.NONE;
      }
   }

   private static class TableModelImpl extends TypedAbstractKongaTableModel<WhereClauseTable.RowObject> {
      public TableModelImpl() {
         super(WhereClauseTable.RowObject.class, createColumnDescriptors());
      }

      private static TableColumnMetaData[] createColumnDescriptors() {
         return new TableColumnMetaData[]{
            new TableColumnMetaData(Strings.get("Db.WhereClauseTable.Column"), String.class, false, 125),
            new TableColumnMetaData(Strings.get("Db.WhereClauseTable.Condition"), String.class, true, 400),
            new TableColumnMetaData(Strings.get("Db.WhereClauseTable.Sort"), SortOrder.class, true, 100)
         };
      }

      public int getRowIndexFor(String var1) {
         for (int var2 = 0; var2 < this.getRowCount(); var2++) {
            if (var1.equals(this.getValueAt(var2, 0))) {
               return var2;
            }
         }

         return -1;
      }

      protected Object[] getColumnDataForTypedObject(WhereClauseTable.RowObject var1) {
         return new Object[]{var1.column.getName(), var1.condition, var1.order};
      }

      @Override
      protected boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
         switch (var3) {
            case 0:
               assert false : "The column name is not editable";
               break;
            case 1:
               ((WhereClauseTable.RowObject)var1).condition = (String)var4;
               break;
            case 2:
               ((WhereClauseTable.RowObject)var1).order = (SortOrder)var4;
         }

         return super.updateObjectWithColumnData(var1, var2, var3, var4);
      }
   }
}
