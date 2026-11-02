package org.etlforge.integration.activity.ui.log.tree;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.RowSorter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.ui.TransferSize;
import org.etlforge.integration.activity.ui.log.LogStatusIcons;
import org.etlforge.ui.widget.KongaTreeTable;
import org.etlforge.util.time.Duration;

final class OperationLogTree extends KongaTreeTable {
   private final TableCellRenderer statusRenderer = createStatusRenderer();
   private final TableCellRenderer dateRenderer = createDateRenderer();
   private final TableCellRenderer durationRenderer = createDurationRenderer();
   private final TableCellRenderer transferSizeRenderer = createTransferSizeRenderer();
   private final TableCellRenderer integerRenderer = createIntegerRenderer();
   private RowSorter<? extends TableModel> sorter;

   public OperationLogTree() {
      super(new OperationLogTreeModel());
      this.setVisibleRowCount(8);
      this.setIcons();
      this.setStyle(TableStyles.normal());
      this.setAutoCreateRowSorter(true);
   }

   private void setIcons() {
      this.setOpenIcon(null);
      this.setClosedIcon(null);
      this.setLeafIcon(null);
   }

   @Override
   public TableCellRenderer getCellRenderer(int var1, int var2) {
      Object var3 = this.getValueAt(var1, var2);
      if (var3 instanceof LogStatus) {
         return this.statusRenderer;
      }

      if (var3 instanceof Date) {
         return this.dateRenderer;
      }

      if (var3 instanceof Duration) {
         return this.durationRenderer;
      }

      if (var3 instanceof TransferSize) {
         return this.transferSizeRenderer;
      }

      if (var3 instanceof Boolean) {
         return super.getCellRenderer(var1, var2);
      }

      if (!(var3 instanceof Integer) && !(var3 instanceof Long)) {
         TableCellRenderer var4 = super.getCellRenderer(var1, var2);
         if (var4 instanceof Component) {
            ((Component)var4).setFont(this.getFont());
         }

         return var4;
      } else {
         return this.integerRenderer;
      }
   }

   private static TableCellRenderer createStatusRenderer() {
      StringValue var0 = new StringValue() {
         @Override
         public String getString(Object var1) {
            Icon var2 = LogStatusIcons.getIcon((LogStatus)var1);
            return var2 == null ? var1.toString() : null;
         }
      };
      IconValue var1 = new IconValue() {
         @Override
         public Icon getIcon(Object var1) {
            return LogStatusIcons.getIcon((LogStatus)var1);
         }
      };
      return new DefaultTableRenderer(var0, var1, 0);
   }

   private static TableCellRenderer createDateRenderer() {
      final DateFormat var0 = DateFormat.getDateTimeInstance(3, 2);
      StringValue var1 = new StringValue() {
         @Override
         public String getString(Object var1) {
            return var0.format((Date)var1);
         }
      };
      return new DefaultTableRenderer(var1);
   }

   private static TableCellRenderer createDurationRenderer() {
      StringValue var0 = new StringValue() {
         @Override
         public String getString(Object var1) {
            return ((Duration)var1).toString(false);
         }
      };
      return new DefaultTableRenderer(var0);
   }

   private static TableCellRenderer createTransferSizeRenderer() {
      return new DefaultTableRenderer(new LabelProvider(StringValues.TO_STRING, 11));
   }

   private static TableCellRenderer createIntegerRenderer() {
      return new DefaultTableRenderer(new LabelProvider(StringValues.NUMBER_TO_STRING, 4));
   }

   @Override
   public TableCellEditor getCellEditor(int var1, int var2) {
      TableCellEditor var3 = super.getCellEditor(var1, var2);
      if (var3 instanceof DefaultCellEditor) {
         ((DefaultCellEditor)var3).setClickCountToStart(1);
      }

      return var3;
   }

   @Override
   public void setRowSorter(RowSorter<? extends TableModel> var1) {
      RowSorter var2 = this.sorter;
      if (var2 != null) {
         var2.removeRowSorterListener(this);
      }

      if (var1 != null) {
         var1.addRowSorterListener(this);
      }

      this.sorter = var1;
      this.firePropertyChange("rowSorter", var2, var1);
      this.firePropertyChange("sorter", var2, var1);
   }

   @Override
   public RowSorter<? extends TableModel> getRowSorter() {
      return this.sorter;
   }

   @Override
   public void sorterChanged(RowSorterEvent var1) {
      if (var1.getType() == Type.SORT_ORDER_CHANGED) {
         JTableHeader var2 = this.getTableHeader();
         if (var2 != null) {
            var2.repaint();
         }
      }
   }

   @Override
   protected void postprocessModelChange(TableModelEvent var1) {
      super.postprocessModelChange(var1);

      try {
         this.updateColumnWidths(((OperationLogTreeModel)this.getTreeTableModel()).getColumnDescriptors());
      } catch (NullPointerException var3) {
      }
   }
}
