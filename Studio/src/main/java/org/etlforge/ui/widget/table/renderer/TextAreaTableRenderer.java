package org.etlforge.ui.widget.table.renderer;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.Enumeration;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class TextAreaTableRenderer extends KongaTextArea implements TableCellRenderer {
   private final DefaultTableCellRenderer delegate = new DefaultTableCellRenderer();
   private final TextAreaTableRenderer.CellHeightCache cellHeightCache = new TextAreaTableRenderer.CellHeightCache();

   public TextAreaTableRenderer() {
      this.setLineWrap(true);
      this.setWrapStyleWord(true);
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.setDefaultProperties(var1, var2, var3, var4, var5, var6);
      this.tweakColumnWidth(var1, var6);
      this.setRowHeight(var1, var5, var6);
      return this;
   }

   private void setDefaultProperties(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.delegate.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      this.setForeground(this.delegate.getForeground());
      this.setBackground(this.delegate.getBackground());
      this.setBorder(this.delegate.getBorder());
      this.setFont(this.delegate.getFont());
      this.setText(this.delegate.getText());
   }

   private void tweakColumnWidth(JTable var1, int var2) {
      TableColumnModel var3 = var1.getColumnModel();
      this.setSize(var3.getColumn(var2).getWidth(), 100000);
   }

   private void setRowHeight(JTable var1, int var2, int var3) {
      int var4 = this.getPreferredSize().height;
      this.cacheHeight(var2, var3, var4);
      int var5 = this.findTotalMaximumRowSize(var1, var2);
      int var6 = Math.max(var4, var5);
      if (var6 > var1.getRowHeight(var2)) {
         var1.setRowHeight(var2, var6);
      }
   }

   private void cacheHeight(int var1, int var2, int var3) {
      this.cellHeightCache.cacheCell(var1, var2, var3);
   }

   private int getMaximumRowHeight(int var1) {
      return this.cellHeightCache.getMaximumRowHeight(var1);
   }

   private int findTotalMaximumRowSize(JTable var1, int var2) {
      int var3 = 1;
      Enumeration var4 = var1.getColumnModel().getColumns();

      while (var4.hasMoreElements()) {
         TableColumn var5 = (TableColumn)var4.nextElement();
         TableCellRenderer var6 = var5.getCellRenderer();
         if (var6 instanceof TextAreaTableRenderer) {
            TextAreaTableRenderer var7 = (TextAreaTableRenderer)var6;
            var3 = Math.max(var3, var7.getMaximumRowHeight(var2));
         }
      }

      return var3;
   }

   private static class CellHeightCache {
      private final Map<Integer, Map<Integer, Integer>> rows = Maps.newHashMap();

      public CellHeightCache() {
      }

      public void cacheCell(int var1, int var2, int var3) {
         Map var4 = this.rows.get(var1);
         if (var4 == null) {
            var4 = Maps.newHashMap();
            this.rows.put(var1, var4);
         }

         var4.put(var2, var3);
      }

      public int getMaximumRowHeight(int var1) {
         Map var2 = this.rows.get(var1);
         if (var2 == null) {
            return 0;
         }

         int var3 = 0;

         for (Object __etlItem103 : var2.values()) {
            Integer var5 = (Integer) __etlItem103;
            var3 = Math.max(var3, var5);
         }

         return var3;
      }
   }
}
