package org.etlforge.integration.client.database.ui;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.lang.Intervals;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.label.LabelUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;
import org.etlforge.util.string.StringLimiter;

public final class DatabaseColumnsTable implements UiProvider {
   private final JLabel caption;
   private final KongaRowTable table;
   private final boolean selectable;
   private boolean opaque;

   public DatabaseColumnsTable(DatabaseObject var1) {
      this(var1, false);
   }

   public DatabaseColumnsTable(DatabaseObject var1, boolean var2) {
      this.selectable = var2;
      this.table = this.createTable(var1);
      this.caption = new JLabel(new StringLimiter(var1.getName()).getLimitedString(100) + " columns");
      this.caption.setLabelFor(this.table.getAdaptee());
      this.opaque = true;
   }

   private KongaRowTable createTable(DatabaseObject var1) {
      DatabaseColumnsTable.Model var2 = new DatabaseColumnsTable.Model(var1, this.selectable);
      KongaTable var3 = new KongaTable(var2);
      TableStyles.normal().makeOver(var3);
      int var4 = Intervals.valueWithin(var2.getRowCount(), 8, 20);
      var3.setVisibleRowCount(var4);
      var3.setRendererPreparer(new TableRendererPreparer() {
         @Override
         public Component prepare(JTable var1, TableCellRenderer var2x, Component var3x, int var4x, int var5) {
            TableColumnMetaData var6 = ((DatabaseColumnsTable.Model)var1.getModel()).getColumnDescriptor(var5);
            boolean var7 = var6 != DatabaseColumnsTable.Model.NULLABLE && var6 != DatabaseColumnsTable.Model.PRIMARY_KEY;
            var3x.setEnabled(var7);
            return var3x;
         }

         @Override
         public boolean doDefaultPreparation() {
            return true;
         }
      });
      return new KongaRowTable(var3);
   }

   public void setVisibleRowCount(int var1) {
      this.table.getAdaptee().setVisibleRowCount(var1);
   }

   public void setCheckedColumns(Collection<DatabaseColumn> var1) {
      this.checkSelectable();
      KongaRowTableModel var2 = this.table.getModel();

      for (int var3 = 0; var3 < var2.getRowCount(); var3++) {
         DatabaseColumn var4 = (DatabaseColumn)var2.getRowObjectAt(var3);
         var2.setValueAt(var1.contains(var4), var3, 0);
      }
   }

   private void checkSelectable() {
      Preconditions.checkState(this.selectable, "This table does not support check-to-select columns");
   }

   public List<DatabaseColumn> getCheckedColumns() {
      this.checkSelectable();
      ArrayList var1 = Lists.newArrayList();
      KongaRowTableModel var2 = this.table.getModel();

      for (int var3 = 0; var3 < var2.getRowCount(); var3++) {
         if (this.isRowChecked(var2, var3)) {
            var1.add((DatabaseColumn)var2.getRowObjectAt(var3));
         }
      }

      return var1;
   }

   private boolean isRowChecked(KongaRowTableModel var1, int var2) {
      return Boolean.TRUE.equals(var1.getValueAt(var2, 0));
   }

   public void setCaption(String var1) {
      if (var1 == null) {
         this.caption.setText("");
      } else {
         MnemonicString var2 = UiResourceBundle.MNEMONIC_FACTORY.fromString(var1);
         LabelUtils.applyMnemonics(this.caption, var2);
      }
   }

   public void setOpaque(boolean var1) {
      this.opaque = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3).setOpaque(this.opaque);
      var2.north(this.caption).center(this.table);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.getAdaptee().asUiProvider().requestFocus();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void installToggleCheckAction() {
      this.checkSelectable();
      this.setDefaultAction(new AbstractSingleObjectSelectionAction<DatabaseColumn>(DatabaseColumn.class) {
         protected void run(DatabaseColumn var1, ActionEvent var2) {
            int var3 = DatabaseColumnsTable.this.table.getSelection().headIndex();
            boolean var4 = (Boolean)DatabaseColumnsTable.this.table.getModel().getValueAt(var3, 0);
            DatabaseColumnsTable.this.table.getModel().setValueAt(!var4, var3, 0);
         }
      });
   }

   private static final class Model extends TypedAbstractKongaTableModel<DatabaseColumn> {
      public static final TableColumnMetaData CHECK = new TableColumnMetaData("Select", Boolean.class, true, 100);
      public static final TableColumnMetaData NAME = new TableColumnMetaData("Name", String.class, false, 200);
      public static final TableColumnMetaData TYPE_NAME = new TableColumnMetaData("Type", String.class, false, 100);
      public static final TableColumnMetaData PRIMARY_KEY = new TableColumnMetaData("Primary Key", Boolean.class, false, 75);
      public static final TableColumnMetaData NULLABLE = new TableColumnMetaData("Nullable", Boolean.class, false, 60);
      private final boolean selectable;

      public Model(DatabaseObject var1, boolean var2) {
         super(DatabaseColumn.class, createColumnMetaData(var2));
         this.selectable = var2;
         this.addRows(var1.getAllColumns());
      }

      private static TableColumnMetaData[] createColumnMetaData(boolean var0) {
         return var0
            ? new TableColumnMetaData[]{CHECK, NAME, TYPE_NAME, PRIMARY_KEY, NULLABLE}
            : new TableColumnMetaData[]{NAME, TYPE_NAME, PRIMARY_KEY, NULLABLE};
      }

      protected Object[] getColumnDataForTypedObject(DatabaseColumn var1) {
         ArrayList var2 = Lists.newArrayList();
         if (this.selectable) {
            var2.add(Boolean.FALSE);
         }

         Collections.addAll(var2, var1.getName(), var1.getDataTypeString(), var1.isPrimaryKey(), var1.isNullable());
         return var2.toArray();
      }
   }
}
