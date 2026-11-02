package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.UpdateValue;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableStringEditor;
import org.etlforge.ui.widget.table.TableUndoRedoService;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class UpdateSourceDatabasePageUi implements UiProvider {
   private Set<DatabaseColumn> updateKeys;
   private final KongaRowTable table;
   private boolean showDisclaimer;

   public UpdateSourceDatabasePageUi(DatabaseObject var1, Set<DatabaseColumn> var2) {
      if (var2 != null && !var2.isEmpty()) {
         this.updateKeys = var2;
      } else {
         this.updateKeys = new HashSet<>(var1.getPrimaryKeys());
      }

      this.table = this.createTable(var1);
      this.showDisclaimer = true;
   }

   private KongaRowTable createTable(DatabaseObject var1) {
      UpdateSourceDatabasePageUi.TableModel var2 = new UpdateSourceDatabasePageUi.TableModel(this.updateKeys, var1);
      TableFactory var3 = ComponentFactories.tableFactory();
      KongaTable var4 = var3.newTable(var2, 4);
      TableStyles.wide().makeOver(var4);
      UpdateSourceDatabasePageUi.ValueRenderer var5 = new UpdateSourceDatabasePageUi.ValueRenderer(this.updateKeys);
      var4.setCellRendererAt(2, var5);
      TableStringEditor var6 = new TableStringEditor();
      var6.setClickCountToStart(1);
      var4.setCellEditorAt(2, var6);
      var4.setTerminateEditOnFocusLost(true);
      TableUndoRedoService var7 = new TableUndoRedoService();
      var7.install(var4, true);
      return new KongaRowTable(var4);
   }

   public void populateColumnValues(List<DatabaseColumn> var1, List<String> var2) {
      UpdateSourceDatabasePageUi.TableModel var3 = (UpdateSourceDatabasePageUi.TableModel)this.table.getModel();
      var3.populateColumnValues(var1, var2);
   }

   public void populateUi(DatabaseStructure var1, DatabaseObject var2) {
      UpdateSourceDatabasePageUi.TableModel var3 = (UpdateSourceDatabasePageUi.TableModel)this.table.getModel();
      var3.setValues(var1, var2);
   }

   public void setShowDisclaimer(boolean var1) {
      this.showDisclaimer = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JLabel var2 = this.showDisclaimer ? createDisclaimer() : null;
      CaptionLayout.of(var2, this.table).nonOpaque().decorate(var1);
   }

   private static JLabel createDisclaimer() {
      TextStyle var0 = TextStyle.newStyle(TextStyles.ErrorInfoText, 1);
      return var0.makeLabel(PackageResources.UpdateSourcePage.DISCLAIMER);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private static class TableModel extends TypedAbstractKongaTableModel<DatabaseColumn> {
      private final Set<DatabaseColumn> updateKeys;

      public TableModel(Set<DatabaseColumn> var1, DatabaseObject var2) {
         super(DatabaseColumn.class, createColumnDescriptors());
         this.updateKeys = var1;
         this.addRows(var2.getAllColumns());
      }

      private static TableColumnMetaData[] createColumnDescriptors() {
         TableColumnMetaData var0 = new TableColumnMetaData("Column", String.class, false, 300);
         TableColumnMetaData var1 = new TableColumnMetaData("Type", String.class, false, 100);
         TableColumnMetaData var2 = new TableColumnMetaData("Update Value", String.class, true, 200);
         return new TableColumnMetaData[]{var0, var1, var2};
      }

      protected Object[] getColumnDataForTypedObject(DatabaseColumn var1) {
         boolean var2 = this.updateKeys.contains(var1);
         return new Object[]{var1.getName(), var1.getDataTypeString(), var2 ? "[update key]" : ""};
      }

      @Override
      public boolean isCellEditable(int var1, int var2) {
         if (var2 == 2) {
            DatabaseColumn var3 = this.getRowObjectAt(var1);
            return !this.updateKeys.contains(var3);
         } else {
            return super.isCellEditable(var1, var2);
         }
      }

      public void populateColumnValues(List<DatabaseColumn> var1, List<String> var2) {
         for (int var3 = 0; var3 < this.getRowCount(); var3++) {
            DatabaseColumn var4 = this.getRowObjectAt(var3);
            if (!this.updateKeys.contains(var4)) {
               String var5 = (String)this.getValueAt(var3, 2);
               if (StringUtils.isNotBlank(var5)) {
                  var1.add(var4);
                  var2.add(var5.trim());
               }
            }
         }
      }

      public void setValues(DatabaseStructure var1, DatabaseObject var2) {
         DbFields var3 = var1.getDbFields();
         HashMap var4 = Maps.newHashMap();

         for (UpdateValue var8 : var3.getUpdateValues(var2)) {
            var4.put(var8.getColumn(), var8.getValue());
         }

         for (int var9 = 0; var9 < this.getRowCount(); var9++) {
            DatabaseColumn var10 = this.getRowObjectAt(var9);
            String var11 = (String)var4.get(var10);
            this.setValueAt(var11, var9, 2);
         }
      }
   }

   private static class ValueRenderer extends DefaultTableCellRenderer {
      private final Set<DatabaseColumn> updateKeys;

      public ValueRenderer(Set<DatabaseColumn> var1) {
         this.updateKeys = var1;
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         UpdateSourceDatabasePageUi.TableModel var7 = (UpdateSourceDatabasePageUi.TableModel)var1.getModel();
         DatabaseColumn var8 = var7.getRowObjectAt(var5);
         boolean var9 = !this.updateKeys.contains(var8);
         this.setEnabled(var9);
         return this;
      }
   }
}
