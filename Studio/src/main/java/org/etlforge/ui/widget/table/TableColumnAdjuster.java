package org.etlforge.ui.widget.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public final class TableColumnAdjuster implements PropertyChangeListener, TableModelListener {
   private final JTable table;
   private final int spacing;
   private boolean isColumnHeaderIncluded;
   private boolean isColumnDataIncluded;
   private boolean isOnlyAdjustLarger;
   private boolean isDynamicAdjustment;
   private final Map<TableColumn, Integer> columnSizes = new HashMap<>();

   public TableColumnAdjuster(JTable var1) {
      this(var1, 6);
   }

   public TableColumnAdjuster(JTable var1, int var2) {
      this.table = var1;
      this.spacing = var2;
      this.setColumnHeaderIncluded(true);
      this.setColumnDataIncluded(true);
      this.setOnlyAdjustLarger(true);
      this.setDynamicAdjustment(false);
      this.installActions();
   }

   public void adjustColumns() {
      TableColumnModel var1 = this.table.getColumnModel();

      for (int var2 = 0; var2 < var1.getColumnCount(); var2++) {
         this.adjustColumn(var2);
      }
   }

   public void adjustColumn(int var1) {
      TableColumn var2 = this.table.getColumnModel().getColumn(var1);
      if (var2.getResizable()) {
         int var3 = this.getColumnHeaderWidth(var1);
         int var4 = this.getColumnDataWidth(var1);
         int var5 = Math.max(var3, var4);
         this.updateTableColumn(var1, var5);
      }
   }

   private int getColumnHeaderWidth(int var1) {
      if (!this.isColumnHeaderIncluded) {
         return 0;
      }

      TableColumn var2 = this.table.getColumnModel().getColumn(var1);
      Object var3 = var2.getHeaderValue();
      TableCellRenderer var4 = var2.getHeaderRenderer();
      if (var4 == null) {
         var4 = this.table.getTableHeader().getDefaultRenderer();
      }

      Component var5 = var4.getTableCellRendererComponent(this.table, var3, false, false, -1, var1);
      return var5.getPreferredSize().width;
   }

   private int getColumnDataWidth(int var1) {
      if (!this.isColumnDataIncluded) {
         return 0;
      }

      int var2 = 0;
      int var3 = this.table.getColumnModel().getColumn(var1).getMaxWidth();

      for (int var4 = 0; var4 < this.table.getRowCount(); var4++) {
         var2 = Math.max(var2, this.getCellDataWidth(var4, var1));
         if (var2 >= var3) {
            break;
         }
      }

      return var2;
   }

   private int getCellDataWidth(int var1, int var2) {
      TableCellRenderer var3 = this.table.getCellRenderer(var1, var2);
      Component var4 = this.table.prepareRenderer(var3, var1, var2);
      return var4.getPreferredSize().width + this.table.getIntercellSpacing().width;
   }

   private void updateTableColumn(int var1, int var2) {
      TableColumn var3 = this.table.getColumnModel().getColumn(var1);
      if (var3.getResizable()) {
         var2 += this.spacing;
         if (this.isOnlyAdjustLarger) {
            var2 = Math.max(var2, var3.getPreferredWidth());
         }

         this.columnSizes.put(var3, new Integer(var3.getWidth()));
         this.table.getTableHeader().setResizingColumn(var3);
         var3.setWidth(var2);
      }
   }

   public void restoreColumns() {
      TableColumnModel var1 = this.table.getColumnModel();

      for (int var2 = 0; var2 < var1.getColumnCount(); var2++) {
         this.restoreColumn(var2);
      }
   }

   private void restoreColumn(int var1) {
      TableColumn var2 = this.table.getColumnModel().getColumn(var1);
      Integer var3 = this.columnSizes.get(var2);
      if (var3 != null) {
         this.table.getTableHeader().setResizingColumn(var2);
         var2.setWidth(var3);
      }
   }

   public void setColumnHeaderIncluded(boolean var1) {
      this.isColumnHeaderIncluded = var1;
   }

   public void setColumnDataIncluded(boolean var1) {
      this.isColumnDataIncluded = var1;
   }

   public void setOnlyAdjustLarger(boolean var1) {
      this.isOnlyAdjustLarger = var1;
   }

   public void setDynamicAdjustment(boolean var1) {
      if (this.isDynamicAdjustment != var1) {
         if (var1) {
            this.table.addPropertyChangeListener(this);
            this.table.getModel().addTableModelListener(this);
         } else {
            this.table.removePropertyChangeListener(this);
            this.table.getModel().removeTableModelListener(this);
         }
      }

      this.isDynamicAdjustment = var1;
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      if ("model".equals(var1.getPropertyName())) {
         TableModel var2 = (TableModel)var1.getOldValue();
         var2.removeTableModelListener(this);
         var2 = (TableModel)var1.getNewValue();
         var2.addTableModelListener(this);
         this.adjustColumns();
      }
   }

   @Override
   public void tableChanged(TableModelEvent var1) {
      if (this.isColumnDataIncluded) {
         if (var1.getType() == 0) {
            int var2 = this.table.convertColumnIndexToView(var1.getColumn());
            if (this.isOnlyAdjustLarger) {
               int var3 = var1.getFirstRow();
               TableColumn var4 = this.table.getColumnModel().getColumn(var2);
               if (var4.getResizable()) {
                  int var5 = this.getCellDataWidth(var3, var2);
                  this.updateTableColumn(var2, var5);
               }
            } else {
               this.adjustColumn(var2);
            }
         } else {
            this.adjustColumns();
         }
      }
   }

   private void installActions() {
      this.installColumnAction(true, true, "adjustColumn", "control ADD");
      this.installColumnAction(false, true, "adjustColumns", "control shift ADD");
      this.installColumnAction(true, false, "restoreColumn", "control SUBTRACT");
      this.installColumnAction(false, false, "restoreColumns", "control shift SUBTRACT");
      this.installToggleAction(true, false, "toggleDynamic", "control MULTIPLY");
      this.installToggleAction(false, true, "toggleLarger", "control DIVIDE");
   }

   private void installColumnAction(boolean var1, boolean var2, String var3, String var4) {
      TableColumnAdjuster.ColumnAction var5 = new TableColumnAdjuster.ColumnAction(var1, var2);
      KeyStroke var6 = KeyStroke.getKeyStroke(var4);
      this.table.getInputMap().put(var6, var3);
      this.table.getActionMap().put(var3, var5);
   }

   private void installToggleAction(boolean var1, boolean var2, String var3, String var4) {
      TableColumnAdjuster.ToggleAction var5 = new TableColumnAdjuster.ToggleAction(var1, var2);
      KeyStroke var6 = KeyStroke.getKeyStroke(var4);
      this.table.getInputMap().put(var6, var3);
      this.table.getActionMap().put(var3, var5);
   }

   class ColumnAction extends AbstractAction {
      private final boolean isSelectedColumn;
      private final boolean isAdjust;

      public ColumnAction(boolean var2, boolean var3) {
         this.isSelectedColumn = var2;
         this.isAdjust = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.isSelectedColumn) {
            int[] var2 = TableColumnAdjuster.this.table.getSelectedColumns();

            for (int var3 = 0; var3 < var2.length; var3++) {
               if (this.isAdjust) {
                  TableColumnAdjuster.this.adjustColumn(var2[var3]);
               } else {
                  TableColumnAdjuster.this.restoreColumn(var2[var3]);
               }
            }
         } else if (this.isAdjust) {
            TableColumnAdjuster.this.adjustColumns();
         } else {
            TableColumnAdjuster.this.restoreColumns();
         }
      }
   }

   class ToggleAction extends AbstractAction {
      private final boolean isToggleDynamic;
      private final boolean isToggleLarger;

      public ToggleAction(boolean var2, boolean var3) {
         this.isToggleDynamic = var2;
         this.isToggleLarger = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.isToggleDynamic) {
            TableColumnAdjuster.this.setDynamicAdjustment(!TableColumnAdjuster.this.isDynamicAdjustment);
         } else if (this.isToggleLarger) {
            TableColumnAdjuster.this.setOnlyAdjustLarger(!TableColumnAdjuster.this.isOnlyAdjustLarger);
         }
      }
   }
}
