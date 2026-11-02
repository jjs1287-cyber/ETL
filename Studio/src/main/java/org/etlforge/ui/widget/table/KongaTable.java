package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultRowSorter;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.ChangeEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.PopupMenuInvoker;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.ListContainerKeyBindingsInstaller;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public class KongaTable extends JTable implements KongaWidget, PopupMenuInvoker {
   public static final String EDITING = "editing";
   private KongaTable.MouseClickListener mouseClickListener;
   private TableRendererPreparer rendererPreparer;
   private ComponentPainter painter;
   private SortStatusMemento sortStatusMemento;
   private RowSorterListener rowSorterListener;
   private JLabel label;
   private final TableSortSupport sortSupport = new TableSortSupport();
   private RowFilter<? extends TableModel, Integer> rowFilter;

   public KongaTable(KongaTableModel var1) {
      super.setAutoCreateColumnsFromModel(false);
      super.setModel(var1);
      TableColumnMetaData[] var2 = var1.getColumnDescriptors();
      int var3 = 0;

      for (int var4 = var2.length; var3 < var4; var3++) {
         TableColumnMetaData var5 = var2[var3];
         TableColumn var6 = new TableColumn(var3, var5.getWidth(), this.createCellRenderer(var5), null);
         boolean var7 = var5.isResizable();
         var6.setResizable(var7);
         if (!var7) {
            var6.setMinWidth(var5.getWidth());
            var6.setMaxWidth(var5.getWidth());
         }

         this.addColumn(var6);
      }

      this.mapKeyStrokes();
      this.registerListeners();
      this.setUpdateSelectionOnSort(true);
      this.setFillsViewportHeight(true);
   }

   @Override
   public void setModel(TableModel var1) {
      List var2 = this.getAutoCreateRowSorter() ? this.getCurrentSortKeys() : null;
      super.setModel(var1);
      if (var2 != null) {
         this.restoreSortKeys(var2);
      }
   }

   private List<? extends SortKey> getCurrentSortKeys() {
      RowSorter var1 = this.getRowSorter();
      return var1 != null ? var1.getSortKeys() : null;
   }

   private void restoreSortKeys(List<? extends SortKey> var1) {
      RowSorter var2 = this.getRowSorter();
      if (var2 != null) {
         var2.setSortKeys(var1);
      }
   }

   protected void mapKeyStrokes() {
   }

   private void registerListeners() {
      this.mouseClickListener = new KongaTable.MouseClickListener();
      this.addMouseListener(this.mouseClickListener);
   }

   protected TableCellRenderer createCellRenderer(TableColumnMetaData var1) {
      return null;
   }

   protected void onPopupTriggered(int var1, int var2) {
   }

   @Override
   public void showPopupMenu(JPopupMenu var1) {
      var1.show(this, this.mouseClickListener.getLastPoint().x, this.mouseClickListener.getLastPoint().y);
   }

   public final void setRendererPreparer(TableRendererPreparer var1) {
      this.rendererPreparer = var1;
   }

   public final TableRendererPreparer getRendererPreparer() {
      return this.rendererPreparer;
   }

   @Override
   public final Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
      if (this.rendererPreparer == null) {
         return super.prepareRenderer(var1, var2, var3);
      }

      Component var4 = null;
      if (this.rendererPreparer.doDefaultPreparation()) {
         var4 = super.prepareRenderer(var1, var2, var3);
      }

      return this.rendererPreparer.prepare(this, var1, var4, var2, var3);
   }

   public void setCellEditorAt(int var1, TableCellEditor var2) {
      TableColumn var3 = this.getColumnModel().getColumn(var1);
      var3.setCellEditor(var2);
   }

   public void setCellEditorFor(TableColumnMetaData var1, TableCellEditor var2) {
      this.setCellEditorAt(this.indexOfColumn(var1), var2);
   }

   public TableColumnMetaData columnDescriptorAtPoint(Point var1) {
      int var2 = this.columnAtPoint(var1);
      if (var2 >= 0) {
         var2 = this.convertColumnIndexToModel(var2);
         return ((KongaTableModel)this.getModel()).getColumnDescriptor(var2);
      } else {
         return null;
      }
   }

   public boolean isInColumn(Point var1, TableColumnMetaData var2) {
      return this.columnDescriptorAtPoint(var1) == var2;
   }

   public final void setRowFilter(RowFilter<? extends TableModel, Integer> var1) {
      this.rowFilter = var1;
      RowSorter var2 = this.getRowSorter();
      if (var2 instanceof DefaultRowSorter) {
         ((DefaultRowSorter)var2).setRowFilter(var1);
      }
   }

   public void setSortingStatus(int var1, SortOrder var2) {
      RowSorter var3 = this.getRowSorter();
      if (var3 == null) {
         throw new IllegalStateException("No RowSorter has been set");
      }

      List var4 = Arrays.asList(new SortKey(var1, var2));
      var3.setSortKeys(var4);
   }

   public void setSortingStatus(TableColumnMetaData var1, SortOrder var2) {
      int var3 = this.indexOfColumn(var1);
      if (var3 >= 0) {
         this.setSortingStatus(var3, var2);
      }
   }

   public final int indexOfColumn(TableColumnMetaData var1) {
      return ((KongaTableModel)this.getModel()).indexOfColumn(var1);
   }

   public void setLabel(String var1) {
      this.setLabel(var1 != null ? KongaLabel.withMnemonic(var1) : null);
   }

   public void setLabel(JLabel var1) {
      this.label = var1;
      if (var1 != null) {
         var1.setLabelFor(this);
      }
   }

   public JLabel getLabel() {
      return this.label;
   }

   @Override
   public UiProvider asUiProvider() {
      return this.label == null ? new UiProviderAdapter(this) : new BorderLayoutBuilder(0, 2).north(this.label).center(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      if (this.painter != var1) {
         this.painter = var1;
         this.repaint();
      }
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
   }

   public void setCellRendererAt(int var1, TableCellRenderer var2) {
      TableColumn var3 = this.getColumnModel().getColumn(var1);
      var3.setCellRenderer(var2);
   }

   public void setCellRendererFor(TableColumnMetaData var1, TableCellRenderer var2) {
      this.setCellRendererAt(this.indexOfColumn(var1), var2);
   }

   public final void setComparator(int var1, Comparator<?> var2) {
      this.sortSupport.setComparator(var1, var2);
      this.applySortingRules();
   }

   private void applySortingRules() {
      RowSorter var1 = this.getRowSorter();
      if (var1 != null) {
         this.sortSupport.apply(this, var1);
      }
   }

   public final void setComparator(TableColumnMetaData var1, Comparator<?> var2) {
      this.sortSupport.setComparator(var1, var2);
      this.applySortingRules();
   }

   public final void setSortsOnUpdates(boolean var1) {
      if (var1 != this.sortSupport.getSortsOnUpdates()) {
         this.sortSupport.setSortsOnUpdates(var1);
         this.applySortingRules();
      }
   }

   @Override
   public void setRowSorter(RowSorter<? extends TableModel> var1) {
      RowSorter var2 = this.getRowSorter();
      if (var2 != null && this.rowSorterListener != null) {
         var2.removeRowSorterListener(this.rowSorterListener);
      }

      if (var1 instanceof DefaultRowSorter && this.rowFilter != null) {
         ((DefaultRowSorter)var1).setRowFilter((RowFilter<? super TableModel, ? super Integer>)this.rowFilter);
      }

      this.sortSupport.apply(this, var1);
      super.setRowSorter(var1);
      if (var1 != null && this.rowSorterListener != null) {
         var1.addRowSorterListener(this.rowSorterListener);
         if (this.sortStatusMemento != null) {
            this.sortStatusMemento.store(this);
         }
      }
   }

   public void setSortStatusMemento(SortStatusMemento var1) {
      if (var1 != this.sortStatusMemento) {
         RowSorter var2 = this.getRowSorter();
         if (var2 == null) {
            throw new IllegalStateException("No RowSorter has been set");
         }

         if (var1 != null) {
            if (this.rowSorterListener == null) {
               this.rowSorterListener = new KongaTable.SortStatusListener();
               var2.addRowSorterListener(this.rowSorterListener);
            }

            this.sortStatusMemento = null;
            var1.restore(this);
         }

         this.sortStatusMemento = var1;
      }
   }

   public void setClickCountToStartEdit(int var1) {
      for (int var2 = 0; var2 < this.getColumnCount(); var2++) {
         TableCellEditor var3 = this.getColumnModel().getColumn(var2).getCellEditor();
         if (var3 instanceof DefaultCellEditor) {
            ((DefaultCellEditor)var3).setClickCountToStart(var1);
         }
      }
   }

   public void setMaxWidth(TableColumnMetaData var1, int var2) {
      this.getColumn(var1).setMaxWidth(var2);
   }

   public final TableColumn getColumn(TableColumnMetaData var1) {
      return this.getColumnModel().getColumn(this.indexOfColumn(var1));
   }

   public void setMinWidth(TableColumnMetaData var1, int var2) {
      this.getColumn(var1).setMinWidth(var2);
   }

   public void selectFirstRow() {
      if (this.getRowCount() > 0) {
         this.getSelectionModel().setSelectionInterval(0, 0);
      }
   }

   public void setVisibleRowCount(int var1) {
      TableUtils.setVisibleRowCount(this, var1);
   }

   public Disposable installKeyTraversalBindings(JComponent var1) {
      Preconditions.checkNotNull(var1, "c");
      ListContainerKeyBindingsInstaller var2 = new ListContainerKeyBindingsInstaller(var1);
      var2.install(this);
      return var2;
   }

   public final void setTerminateEditOnFocusLost(boolean var1) {
      this.putClientProperty("terminateEditOnFocusLost", var1);
   }

   @Override
   public final void editingStopped(ChangeEvent var1) {
      super.editingStopped(var1);
      this.firePropertyChange("editing", true, false);
   }

   @Override
   public final void editingCanceled(ChangeEvent var1) {
      super.editingCanceled(var1);
      this.firePropertyChange("editing", true, false);
   }

   @Override
   public final boolean editCellAt(int var1, int var2, EventObject var3) {
      boolean var4 = super.editCellAt(var1, var2, var3);
      if (var4) {
         this.firePropertyChange("editing", false, true);
      }

      return var4;
   }

   public final Object getValueAt(int var1, TableColumnMetaData var2) {
      TableModel var3 = this.getModel();
      if (var3 instanceof KongaTableModel) {
         int var4 = ((KongaTableModel)var3).indexOfColumn(var2);
         return super.getValueAt(var1, var4);
      } else {
         return null;
      }
   }

   public final boolean editCellAt(int var1, TableColumnMetaData var2) {
      int var3 = this.indexOfColumn(var2);
      return super.editCellAt(var1, var3);
   }

   public final TableCellEditor getCellEditor(int var1, TableColumnMetaData var2) {
      int var3 = this.indexOfColumn(var2);
      return super.getCellEditor(var1, var3);
   }

   public final TableColumnMetaData getColumnDescriptor(int var1) {
      return ((KongaTableModel)this.getModel()).getColumnDescriptor(var1);
   }

   public final KongaTable withSingleSelection() {
      this.setSelectionMode(0);
      return this;
   }

   private final class MouseClickListener extends MouseActionListener {
      private Point lastPoint = new Point();

      private MouseClickListener() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         this.lastPoint = var1.getPoint();
         int var2 = KongaTable.this.rowAtPoint(this.lastPoint);
         KongaTable.this.getSelectionModel().setSelectionInterval(var2, var2);
         KongaTable.this.onPopupTriggered(var2, KongaTable.this.columnAtPoint(this.lastPoint));
      }

      public Point getLastPoint() {
         return this.lastPoint;
      }
   }

   private final class SortStatusListener implements RowSorterListener {
      private SortStatusListener() {
      }

      @Override
      public void sorterChanged(RowSorterEvent var1) {
         if (KongaTable.this.sortStatusMemento != null) {
            KongaTable.this.sortStatusMemento.store(KongaTable.this);
         }
      }
   }
}
