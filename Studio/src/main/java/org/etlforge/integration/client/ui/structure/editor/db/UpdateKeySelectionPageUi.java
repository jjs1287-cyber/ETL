package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class UpdateKeySelectionPageUi implements UiProvider {
   private final KongaRowTable table;

   public UpdateKeySelectionPageUi(DatabaseObject var1) {
      this.table = this.createTable(var1);
   }

   private KongaRowTable createTable(DatabaseObject var1) {
      UpdateKeySelectionPageUi.TableModel var2 = new UpdateKeySelectionPageUi.TableModel(var1);
      TableFactory var3 = ComponentFactories.tableFactory();
      KongaTable var4 = var3.newTable(var2, 4);
      TableStyles.normal().makeOver(var4);
      return new KongaRowTable(var4);
   }

   public Set<DatabaseColumn> getSelectedColumns() {
      UpdateKeySelectionPageUi.TableModel var1 = (UpdateKeySelectionPageUi.TableModel)this.table.getModel();
      return var1.getSelectedColumns();
   }

   public void setSelectedColumns(DatabaseStructure var1, DatabaseObject var2) {
      UpdateKeySelectionPageUi.TableModel var3 = (UpdateKeySelectionPageUi.TableModel)this.table.getModel();
      var3.setSelectedColumns(var1, var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(PackageResources.PrimaryKeySelectionPage.EXPLANATION, this.table).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private static class TableModel extends TypedAbstractKongaTableModel<DatabaseColumn> {
      public TableModel(DatabaseObject var1) {
         super(DatabaseColumn.class, createColumnDescriptors());
         this.addRows(var1.getAllColumns());
      }

      private static TableColumnMetaData[] createColumnDescriptors() {
         TableColumnMetaData var0 = new TableColumnMetaData("Column", String.class, false, 300);
         TableColumnMetaData var1 = new TableColumnMetaData("Type", String.class, false, 100);
         TableColumnMetaData var2 = new TableColumnMetaData("Update Key", Boolean.class, true, 100);
         return new TableColumnMetaData[]{var0, var1, var2};
      }

      protected Object[] getColumnDataForTypedObject(DatabaseColumn var1) {
         return new Object[]{var1.getName(), var1.getDataTypeString(), false};
      }

      public Set<DatabaseColumn> getSelectedColumns() {
         HashSet var1 = Sets.newHashSet();

         for (int var2 = 0; var2 < this.getRowCount(); var2++) {
            if (this.isSelected(var2)) {
               var1.add(this.getRowObjectAt(var2));
            }
         }

         return var1;
      }

      private boolean isSelected(int var1) {
         Object var2 = this.getValueAt(var1, 2);
         return Boolean.TRUE.equals(var2);
      }

      public void setSelectedColumns(DatabaseStructure var1, DatabaseObject var2) {
         DbFields var3 = var1.getDbFields();
         HashSet var4 = Sets.newHashSet(var3.getUpdateKeys(var2));

         for (int var5 = 0; var5 < this.getRowCount(); var5++) {
            DatabaseColumn var6 = this.getRowObjectAt(var5);
            boolean var7 = var4.contains(var6);
            this.setValueAt(var7, var5, 2);
         }
      }
   }
}
