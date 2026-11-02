package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.Pair;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableIntegerEditor;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class TableUseCountTable implements UiProvider {
   private final TableSelectionModel selectionModel;
   private final TableUseCountTable.Model tableModel;
   private final KongaRowTable table;

   public TableUseCountTable(TableSelectionModel var1) {
      this.selectionModel = var1;
      this.tableModel = new TableUseCountTable.Model();
      this.table = this.createTable();
      this.bindProperties();
   }

   private KongaRowTable createTable() {
      byte var1 = 4;
      KongaTable var2 = ComponentFactories.tableFactory().newTable(this.tableModel, var1);
      TableStyles.normal().makeOver(var2);
      var2.setTerminateEditOnFocusLost(true);
      var2.setCellRendererFor(TableUseCountTable.Model.OBJECT, new TableUseCountTable.ObjectRenderer());
      TableIntegerEditor var3 = new TableIntegerEditor();
      var3.setClickCountToStart(1);
      var2.setCellEditorFor(TableUseCountTable.Model.USE_COUNT, var3);
      return new KongaRowTable(var2);
   }

   private void bindProperties() {
      TableUseCountTable.UsageCountBinding var1 = new TableUseCountTable.UsageCountBinding();
      var1.syncUi();
      this.selectionModel.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            TableUseCountTable.this.table.getAdaptee().repaint();
         }
      });
   }

   public void installAction(SelectionAction var1) {
      var1.setSelectionSource(this.table);
      ActionKeyBinding.install(var1, this.table.getAdaptee());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
      this.table.getAdaptee().selectFirstRow();
   }

   private static class Model extends TypedAbstractKongaTableModel<DatabaseObject> {
      public static final TableColumnMetaData OBJECT = new TableColumnMetaData("Table/View", DatabaseObject.class, false, 200);
      public static final TableColumnMetaData USE_COUNT = new TableColumnMetaData("Use Count", Integer.class, true, 75);

      public Model() {
         super(DatabaseObject.class, OBJECT, USE_COUNT);
      }

      protected Object[] getColumnDataForTypedObject(DatabaseObject var1) {
         return new Object[]{var1, 1};
      }

      public int getUseCount(int var1) {
         return (Integer)this.getValueAt(var1, USE_COUNT);
      }

      @Override
      public void setValueAt(Object var1, int var2, int var3) {
         TableColumnMetaData var4 = this.getColumnDescriptor(var3);
         if (var4 == USE_COUNT && var1 instanceof Integer) {
            int var5 = (Integer)var1;
            if (var5 <= 0) {
               if (this.askAboutRemoval()) {
                  this.deleteRow(var2);
               }

               return;
            }

            if (var5 > 100) {
               NonBlockingAlert.error("A table cannot be used more than 100 times.", "Value Too Large");
               return;
            }
         }

         super.setValueAt(var1, var2, var3);
      }

      private boolean askAboutRemoval() {
         Answer var1 = Ask.yesNoOrCancel("You have entered a value <= 0. Do you want to remove this table?", "Remove Table?");
         return var1.isYes();
      }
   }

   private class ObjectRenderer extends DefaultTableCellRenderer {
      private ObjectRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         DatabaseObject var7 = (DatabaseObject)var2;
         String var8 = TableUseCountTable.this.selectionModel.isSchemaNameIncluded() ? this.getQualifiedName(var7) : var7.getName();
         this.setText(var8);
         return this;
      }

      private String getQualifiedName(DatabaseObject var1) {
         BeginEndQuote var2 = TableUseCountTable.this.selectionModel.getBeginEndQuotes();
         return var2.escapeName(var1.getSchema()) + "." + var2.escapeName(var1.getName());
      }
   }

   private class UsageCountBinding extends AbstractBinding<SelectedTables> {
      private final TableModelListener uiListener = new TableModelListener() {
         @Override
         public void tableChanged(TableModelEvent var1) {
            if (this.isEventOfInterest(var1) && UsageCountBinding.this.isUiToModelEnabled()) {
               UsageCountBinding.this.syncModel();
            }
         }

         private boolean isEventOfInterest(TableModelEvent var1) {
            int var2 = var1.getType();
            return var2 == 0 || var2 == -1;
         }
      };

      public UsageCountBinding() {
         super(TableUseCountTable.this.selectionModel, "ho ho ho");
         TableUseCountTable.this.tableModel.addTableModelListener(this.uiListener);
      }

      protected SelectedTables getValueFromUi() {
         SelectedTables var1 = new SelectedTables();

         for (int var2 = 0; var2 < TableUseCountTable.this.tableModel.getRowCount(); var2++) {
            DatabaseObject var3 = TableUseCountTable.this.tableModel.getRowObjectAt(var2);
            int var4 = TableUseCountTable.this.tableModel.getUseCount(var2);
            var1.setTableUsage(var3, var4);
         }

         return var1;
      }

      protected void updateUiWithValue(SelectedTables var1) {
         TableUseCountTable.this.tableModel.deleteAll();
         int var2 = 0;

         for (Pair var4 : var1.getTableUsage()) {
            TableUseCountTable.this.tableModel.addRow(var4.first);
            TableUseCountTable.this.tableModel.setValueAt(var4.second, var2, TableUseCountTable.Model.USE_COUNT);
            var2++;
         }
      }

      @Override
      protected void disposeUiListener() {
         TableUseCountTable.this.tableModel.removeTableModelListener(this.uiListener);
      }
   }
}
