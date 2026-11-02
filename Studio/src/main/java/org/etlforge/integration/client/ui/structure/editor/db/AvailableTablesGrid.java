package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.ListSelectionDispatcher;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.table.TableUtils;

final class AvailableTablesGrid implements UiProvider, Receiver<ListFilter> {
   private final TableSelectionModel selectionModel;
   private final AvailableTablesGrid.SelectionDispatcher selectionDispatcher = new AvailableTablesGrid.SelectionDispatcher();
   private ListFilter filter = ListFilter.ACCEPT_ALL;
   private final int numberOfColumns;
   private final AvailableTablesGrid.TableImpl table;

   public AvailableTablesGrid(TableSelectionModel var1, int var2) {
      this.selectionModel = var1;
      this.numberOfColumns = var2;
      this.table = this.createEmptyTable();
      this.bindToModel();
   }

   private AvailableTablesGrid.TableImpl createEmptyTable() {
      AvailableTablesGrid.TableImpl var1 = new AvailableTablesGrid.TableImpl(new Object[0][this.numberOfColumns], this.getColumnNames());
      var1.setCellSelectionEnabled(true);
      var1.setSelectionMode(0);
      var1.getSelectionModel().addListSelectionListener(this.selectionDispatcher);
      var1.getColumnModel().getSelectionModel().addListSelectionListener(this.selectionDispatcher);
      var1.setTableHeader(null);
      AvailableTablesGrid.Renderer var2 = this.installRenderer(var1);
      var1.setDefaultRenderer(DatabaseObject.class, var2);
      TableStyles.normal().makeOver(var1);
      Font var3 = TextStyles.DefaultTableText.getFont();
      Font var4 = var3.deriveFont(var3.getSize() + 1.0F);
      var1.setFont(var4);
      TableUtils.setVisibleRowCount(var1, 4);
      return var1;
   }

   private Object[] getColumnNames() {
      Object[] var1 = new Object[this.numberOfColumns];
      Arrays.fill(var1, "");
      return var1;
   }

   private AvailableTablesGrid.Renderer installRenderer(AvailableTablesGrid.TableImpl var1) {
      AvailableTablesGrid.Renderer var2 = new AvailableTablesGrid.Renderer();

      for (int var3 = 0; var3 < this.numberOfColumns; var3++) {
         var1.getColumnModel().getColumn(var3).setCellRenderer(var2);
      }

      return var2;
   }

   private void bindToModel() {
      new AvailableTablesGrid.AvailableObjectsBinding().syncUi();
      this.selectionModel.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            AvailableTablesGrid.this.table.repaint();
         }
      });
   }

   public void setDefaultAction(Action var1) {
      DefaultActionHandler var2 = new DefaultActionHandler(this.table, var1);
      var2.install();
   }

   public void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   public void handle(ListFilter var1) {
      this.filter = var1 == null ? ListFilter.ACCEPT_ALL : var1;
      DefaultTableModel var2 = this.createTableModel(this.selectionModel.getAvailableObjects());
      this.table.setModel(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaScrollPane var2 = new KongaScrollPane(this.table);
      var2.getViewport().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   private DefaultTableModel createTableModel(DatabaseObjectContainer[] var1) {
      ArrayList var2 = Lists.newArrayList();

      for (DatabaseObjectContainer var6 : var1) {
         this.createRowsFromContainer(var6, var2);
      }

      Object[][] var7 = new Object[var2.size()][];

      for (int var8 = 0; var8 < var2.size(); var8++) {
         var7[var8] = (Object[])var2.get(var8);
      }

      return new DefaultTableModel(var7, this.getColumnNames());
   }

   private void createRowsFromContainer(DatabaseObjectContainer<?> var1, List<Object[]> var2) {
      String var3 = var1.getObjectType().toString() + "S";
      var2.add(this.headerRow(var3));
      int var4 = 0;
      Object[] var5 = new Object[this.numberOfColumns];

      for (DatabaseObject var7 : var1.getAllObjects()) {
         if (this.filter.test(var7)) {
            var5[var4] = var7;
            if (++var4 == this.numberOfColumns) {
               var2.add(var5);
               var4 = 0;
               var5 = new Object[this.numberOfColumns];
            }
         }
      }

      if (var4 > 0) {
         for (int var8 = var4; var8 < this.numberOfColumns; var8++) {
            var5[var8] = "";
         }

         var2.add(var5);
      }

      var2.add(this.emptyRow());
   }

   private Object[] headerRow(String var1) {
      ArrayList var2 = Lists.newArrayList(var1);

      for (int var3 = 1; var3 < this.numberOfColumns; var3++) {
         var2.add("");
      }

      return var2.toArray();
   }

   private Object[] emptyRow() {
      return new Object[this.numberOfColumns];
   }

   private class AvailableObjectsBinding extends AbstractBinding<DatabaseObjectContainer[]> {
      public AvailableObjectsBinding() {
         super(AvailableTablesGrid.this.selectionModel, "availab...");
      }

      protected void updateUiWithValue(DatabaseObjectContainer[] var1) {
         DefaultTableModel var2 = AvailableTablesGrid.this.createTableModel(var1);
         AvailableTablesGrid.this.table.setModel(var2);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AvailableTablesGrid.this.requestFocus();
            }
         });
      }
   }

   private class Renderer extends DefaultTableCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var2 instanceof DatabaseObject) {
            DatabaseObject var7 = (DatabaseObject)var2;
            String var8 = AvailableTablesGrid.this.selectionModel.isSchemaNameIncluded()
               ? AvailableTablesGrid.this.selectionModel.getQualifiedName(var7)
               : var7.getName();
            this.setText(var8);
            this.setFont(var1.getFont());
         } else {
            Font var9 = Fonts.bold(var1);
            this.setFont(var9);
         }

         return this;
      }
   }

   private class SelectionDispatcher extends ListSelectionDispatcher {
      private SelectionDispatcher() {
      }

      @Override
      protected Selection getSelection(ListSelectionEvent var1) {
         DatabaseObject var2 = AvailableTablesGrid.this.table.getSelectedTable();
         return var2 == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var2);
      }
   }

   private class TableImpl extends JTable {
      private final AvailableTablesGrid.Renderer renderer = AvailableTablesGrid.this.new Renderer();

      public TableImpl(Object[][] var2, Object[] var3) {
         super(var2, var3);
      }

      @Override
      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      @Override
      public TableCellRenderer getCellRenderer(int var1, int var2) {
         return this.renderer;
      }

      public DatabaseObject getSelectedTable() {
         int var1 = this.getSelectedRow();
         if (var1 == -1) {
            return null;
         }

         int var2 = this.getSelectedColumn();
         if (var2 == -1) {
            return null;
         }

         Object var3 = this.getValueAt(var1, var2);
         return var3 instanceof DatabaseObject ? (DatabaseObject)var3 : null;
      }
   }
}

