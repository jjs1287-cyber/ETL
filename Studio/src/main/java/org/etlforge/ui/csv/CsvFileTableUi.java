package org.etlforge.ui.csv;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.table.TableColumnAdjuster;
import org.etlforge.ui.widget.table.TableRowSelectionSource;
import org.etlforge.ui.widget.table.TableUtils;
import org.etlforge.util.csv.CsvFile;
import org.etlforge.util.csv.Record;

public final class CsvFileTableUi implements UiProvider, SelectionSource {
   private final JTable table;
   private final CsvFileTableUi.SelectionSourceImpl selectionSource;
   private String caption;

   public CsvFileTableUi(CsvFile var1) {
      this.table = this.createTable(var1);
      this.selectionSource = new CsvFileTableUi.SelectionSourceImpl(var1, this.table);
   }

   private JTable createTable(CsvFile var1) {
      Object[] var2 = this.getHeader(var1);
      Object[][] var3 = this.getRowData(var1);
      DefaultTableModel var4 = new DefaultTableModel(var3, var2);
      JTable var5 = new JTable(var4) {
         @Override
         public boolean isCellEditable(int var1, int var2x) {
            return false;
         }
      };
      this.customizeTable(var5);
      this.adjustColumnWidths(var5);
      return var5;
   }

   private Object[] getHeader(CsvFile var1) {
      return var1.isEmpty() ? emptyRow(8) : var1.getHeader().toArray();
   }

   private static Object[] emptyRow(int var0) {
      String[] var1 = new String[var0];
      Arrays.fill(var1, " ");
      return var1;
   }

   private Object[][] getRowData(CsvFile var1) {
      int var2 = var1.getNumberOfRecords();
      Object[][] var3 = new Object[Math.max(10, var2)][];
      int var4 = 0;

      for (Record var6 : var1.getRecords()) {
         var3[var4] = var6.toArray();
         var4++;
      }

      int var7 = var1.getNumberOfColumns();

      while (var4 < 10) {
         var3[var4] = emptyRow(var1.isEmpty() ? 8 : var7);
         var4++;
      }

      return var3;
   }

   private void customizeTable(JTable var1) {
      var1.getTableHeader().setReorderingAllowed(false);
      var1.setShowGrid(true);
      TableUtils.setVisibleRowCount(var1, 10);
   }

   private void adjustColumnWidths(JTable var1) {
      var1.setAutoResizeMode(0);
      TableColumnModel var2 = var1.getColumnModel();

      for (int var3 = var2.getColumnCount() - 2; var3 >= 0; var3--) {
         TableColumn var4 = var2.getColumn(var3);
         var4.setMaxWidth(200);
      }

      TableColumnAdjuster var5 = new TableColumnAdjuster(var1);
      var5.adjustColumns();
   }

   public CsvFileTableUi withTableStyle(TableStyle var1) {
      var1.makeOver(this.table);
      return this;
   }

   public CsvFileTableUi withTextStyle(TextStyle var1) {
      var1.makeOver(this.table);
      return this;
   }

   public CsvFileTableUi withCaption(String var1) {
      this.caption = var1;
      return this;
   }

   public IndexedSelection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaScrollPane var2 = new KongaScrollPane(this.table);
      var2.getViewport().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var2.addComponentListener(new CsvFileTableUi.LastColumnStretcher());
      if (StringUtils.isEmpty(this.caption)) {
         var1.display(var2);
      } else {
         CaptionLayout.of(this.caption, var2).decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public void installContextMenu(Collection<? extends Action> var1) {
      PopupMenuRequestHandlers.install(this.table, var1);
   }

   private class LastColumnStretcher extends ComponentAdapter {
      private LastColumnStretcher() {
      }

      @Override
      public void componentResized(ComponentEvent var1) {
         if (var1.getComponent().isVisible()) {
            JScrollPane var2 = (JScrollPane)var1.getComponent();
            int var3 = this.calculateWidthDifference(var2);
            if (var3 > 0) {
               this.makeLastColumnWider(var3);
            }
         }
      }

      private int calculateWidthDifference(JScrollPane var1) {
         JViewport var2 = var1.getViewport();
         int var3 = var2.getWidth();
         int var4 = CsvFileTableUi.this.table.getWidth();
         return var3 - var4;
      }

      private void makeLastColumnWider(int var1) {
         TableColumnModel var2 = CsvFileTableUi.this.table.getColumnModel();
         TableColumn var3 = var2.getColumn(var2.getColumnCount() - 1);
         int var4 = var3.getWidth() + var1;
         var3.setMinWidth(var4);
         var3.setPreferredWidth(var4);
      }
   }

   private static class SelectionSourceImpl extends TableRowSelectionSource {
      private final CsvFile file;

      public SelectionSourceImpl(CsvFile var1, JTable var2) {
         super(var2);
         this.file = var1;
      }

      @Override
      protected Object getRowObjectAt(int var1) {
         return var1 < this.file.getNumberOfRecords() ? this.file.getRecordAt(var1) : null;
      }
   }
}

