package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.IndexedSelectionBuilder;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public final class SelectorTable<E> implements UiProvider {
   private final SelectorTable<E>.Model<E> model;
   private final KongaRowTable table;
   private final SelectorTable<E>.SelectionSourceImpl selSource;
   private SelectorTable.Mode mode = SelectorTable.Mode.MULTIPLE;
   private ImmutableSet<E> disabledRows = ImmutableSet.of();
   private final TableRowSorter<SelectorTable<E>.Model<E>> sorter;
   private boolean includeBulkSelectionButtons;
   private final Action selectAllAction;
   private final Action unselectAllAction;

   public static <E> SelectorTable<E> empty(Class<E> var0) {
      return new SelectorTable<>(Collections.emptyList(), var0);
   }

   public SelectorTable(Collection<E> var1, Class<E> var2) {
      this.model = new Model<E>(var1, var2, createColumnDescriptors());
      this.table = this.createTable(this.model, var2);
      this.table.getAdaptee().setRowSorter(this.sorter = new TableRowSorter<>(this.model));
      this.selSource = new SelectorTable.SelectionSourceImpl();
      this.selectAllAction = new SelectorTable.BulkSelectionAction(UiResourceBundle.getString("TableSelectorDialog.SelectAll"), true);
      this.unselectAllAction = new SelectorTable.BulkSelectionAction(UiResourceBundle.getString("TableSelectorDialog.UnselectAll"), false);
   }

   private static TableColumnMetaData[] createColumnDescriptors() {
      return new TableColumnMetaData[]{
         new TableColumnMetaData("Selected", Boolean.class, true, 50, false), new TableColumnMetaData("Item", Object.class, false, 400, true)
      };
   }

   public SelectorTable(E[] var1, Class<E> var2) {
      this(Arrays.asList((E[])var1), var2);
   }

   public void replaceValues(Collection<E> var1) {
      this.table.clearSelection();
      this.model.deleteAll();
      this.model.addRows(var1);
   }

   public void setFilter(Predicate<? super E> var1, boolean var2) {
      if (var1 == null) {
         this.sorter.setRowFilter(null);
      } else {
         this.sorter.setRowFilter(new RowObjectFilter<E>(var1, var2));
      }
   }

   public void setValueWidth(int var1) {
      TableColumnModel var2 = this.table.getAdaptee().getColumnModel();
      TableColumn var3 = var2.getColumn(1);
      var3.setMinWidth(var1);
      var3.setMaxWidth(var1);
      var3.setWidth(var1);
   }

   public void setSelectionMode(SelectorTable.Mode var1) {
      Preconditions.checkNotNull(var1, "mode");
      if (var1 != this.mode) {
         if (var1 == SelectorTable.Mode.SINGLE) {
            this.ensureSingleSelection();
            this.table.getAdaptee().getSelectionModel().setSelectionMode(0);
         } else {
            this.table.getAdaptee().getSelectionModel().setSelectionMode(2);
         }

         this.mode = var1;
      }
   }

   private void ensureSingleSelection() {
      IndexedSelection var1 = this.selSource.getSelection();
      if (var1.size() > 1) {
         this.selSource.setSelection(SelectionFactory.newIndexedSelection(var1.head(), var1.headIndex()));
      }
   }

   public void setInitialSelectionState(boolean var1) {
      int var2 = 0;

      for (int var3 = this.sorter.getViewRowCount(); var2 < var3; var2++) {
         int var4 = this.sorter.convertRowIndexToModel(var2);
         this.model.setValueAt(var1, var4, 0);
         if (this.mode == SelectorTable.Mode.SINGLE && var1) {
            break;
         }
      }
   }

   public void setObjectCellRenderer(TableCellRenderer var1) {
      if (var1 == null) {
         var1 = new DefaultTableCellRenderer();
      }

      this.table.getAdaptee().setCellRendererAt(1, var1);
   }

   public List<E> getSelectedObjects() {
      return this.collectObjects(true);
   }

   private List<E> collectObjects(boolean var1) {
      ArrayList var2 = Lists.newArrayList();
      int var3 = 0;

      for (int var4 = this.model.getRowCount(); var3 < var4; var3++) {
         if (this.model.isSelected(var3) == var1) {
            var2.add(this.model.getRowObjectAt(var3));
         }
      }

      return var2;
   }

   public void setSelectedObjects(Collection<? extends E> var1) {
      this.startBulkChange();
      int var2 = 0;

      for (int var3 = this.model.getRowCount(); var2 < var3; var2++) {
         Object var4 = this.model.getRowObjectAt(var2);
         this.model.setRowSelected(var2, var1.contains(var4));
      }

      this.endBulkChange();
   }

   public List<E> getUnselectedObjects() {
      return this.collectObjects(false);
   }

   public boolean isEverythingSelected() {
      int var1 = 0;

      for (int var2 = this.model.getRowCount(); var1 < var2; var1++) {
         if (!this.model.isSelected(var1)) {
            return false;
         }
      }

      return true;
   }

   public void setEnabled(boolean var1) {
      this.table.getAdaptee().setEnabled(var1);
      this.selectAllAction.setEnabled(var1);
      this.unselectAllAction.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.table.getAdaptee().isEnabled();
   }

   public void makeOver(TextStyle var1) {
      var1.makeOver(this.table.getAdaptee());
   }

   public void setStyle(TableStyle var1) {
      var1.makeOver(this.table.getAdaptee());
   }

   public MutableSelectionSource asSelectionSource() {
      return this.selSource;
   }

   public void setIncludeBulkSelectionButtons(boolean var1) {
      this.includeBulkSelectionButtons = var1;
   }

   public void setDisabledRows(Collection<E> var1) {
      this.disabledRows = ImmutableSet.copyOf(var1);
   }

   public void setLabel(String var1) {
      this.table.getAdaptee().setLabel(var1);
   }

   public void setLabel(JLabel var1) {
      this.table.getAdaptee().setLabel(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.includeBulkSelectionButtons) {
         BoxBuilder var2 = BoxBuilder.horizontal();
         var2.glue().addAllWithSpace(5, this.getSelectAllAction(), this.getUnselectAllAction());
         new BorderLayoutBuilder(0, 5).center(this.table).south(var2).decorate(var1);
      } else {
         this.table.decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SelectorTable.this.table.selectFirstRow();
            SelectorTable.this.table.requestFocus();
         }
      });
   }

   public Action getSelectAllAction() {
      return this.selectAllAction;
   }

   public Action getUnselectAllAction() {
      return this.unselectAllAction;
   }

   public void setVisibleRows(int var1) {
      KongaTable var2 = this.table.getAdaptee();
      var2.setVisibleRowCount(var1);
   }

   public void highlightFirstRow() {
      this.table.selectFirstRow();
   }

   private KongaRowTable createTable(SelectorTable<E>.Model<E> var1, Class<E> var2) {
      KongaTable var3 = new KongaTable(var1);
      var3.setShowGrid(false);
      var3.setTableHeader(null);
      var3.getColumnModel().getColumn(0).setMaxWidth(30);
      var3.setDefaultRenderer(Boolean.class, new SelectorTable.BooleanRenderer());
      var3.setDefaultRenderer(var2, new SelectorTable.ObjectRenderer());
      var3.setRendererPreparer(new SelectorTable.RendererPreparerImpl());
      var3.setTerminateEditOnFocusLost(true);
      this.registerKeyBindings(var3);
      return new KongaRowTable(var3);
   }

   private void registerKeyBindings(KongaTable var1) {
      KeyStroke var2 = KeyStroke.getKeyStroke(32, 0);
      var1.getInputMap(1).put(var2, "toggle");
      var1.getActionMap().put("toggle", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            SelectorTable.this.toggleSelectedRows();
         }
      });
   }

   private void toggleSelectedRows() {
      int[] var1 = this.table.getAdaptee().getSelectedRows();
      if (var1.length != 0) {
         int[] var2 = this.convertRowIndecesToModel(var1);
         this.startBulkChange();
         if (var2.length == 1) {
            this.model.toggle(var2[0]);
         } else {
            this.model.toggle(var2);
         }

         this.endBulkChange();
      }
   }

   private int[] convertRowIndecesToModel(int[] var1) {
      int[] var2 = new int[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         int var4 = this.sorter.convertRowIndexToModel(var1[var3]);
         var2[var3] = var4;
      }

      return var2;
   }

   private void startBulkChange() {
      this.selSource.setBlocked(true);
   }

   private void endBulkChange() {
      this.selSource.fireSelectionChanged();
      this.selSource.setBlocked(false);
   }

   private boolean isRowEnabled(int var1) {
      if (!this.table.getAdaptee().isEnabled()) {
         return false;
      }

      if (this.disabledRows.isEmpty()) {
         return true;
      }

      int var2 = this.sorter.convertRowIndexToModel(var1);
      Object var3 = this.model.getRowObjectAt(var2);
      return !this.disabledRows.contains(var3);
   }

   private boolean isRowDisabled(int var1) {
      return !this.isRowEnabled(var1);
   }

   private class BooleanRenderer extends JCheckBox implements TableCellRenderer {
      private final Border border = new EmptyBorder(1, 1, 1, 1);

      public BooleanRenderer() {
         this.setHorizontalAlignment(0);
         this.setBorderPainted(true);
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setEnabled(SelectorTable.this.isRowEnabled(var5));
         this.setForeground(var1.getForeground());
         this.setBackground(var1.getBackground());
         this.setSelected(var2 != null && (Boolean)var2);
         this.setBorder(this.border);
         return this;
      }
   }

   private class BulkSelectionAction extends AbstractAction {
      private final boolean value;

      public BulkSelectionAction(String var2, boolean var3) {
         super(var2);
         this.value = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SelectorTable.this.setInitialSelectionState(BulkSelectionAction.this.value);
            }
         });
      }
   }

   public enum Mode {
      SINGLE,
      MULTIPLE;
   }

   private class Model<T> extends TypedAbstractKongaTableModel<T> {
      public Model(Iterable<T> var2, Class<T> var3, TableColumnMetaData[] var4) {
         super(var3, var4, var2);
      }

      public boolean isSelected(int var1) {
         return (Boolean)this.getValueAt(var1, 0);
      }

      public void setSelected(int[] var1) {
         this.clearSelection();
         this.setCheckState(var1, Boolean.TRUE);
      }

      private void setCheckState(int[] var1, Boolean var2) {
         for (int var6 : var1) {
            this.setRowSelected(var6, var2);
         }
      }

      public void setSelected(Object[] var1) {
         this.clearSelection();
         HashSet var2 = Sets.newHashSet(var1);

         for (int var3 = 0; var3 < this.getRowCount(); var3++) {
            if (var2.contains(this.getRowObjectAt(var3))) {
               this.setRowSelected(var3, true);
            }
         }
      }

      public void setRowSelected(int var1, boolean var2) {
         this.setValueAt(var2, var1, 0);
      }

      public void toggle(int[] var1) {
         boolean var2 = false;

         for (int var6 : var1) {
            if (!this.isSelected(var6)) {
               var2 = true;
               break;
            }
         }

         this.setCheckState(var1, var2);
      }

      public void toggle(int var1) {
         this.setRowSelected(var1, !this.isSelected(var1));
      }

      public void clearSelection() {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            this.setRowSelected(var1, false);
         }
      }

      @Override
      protected Object[] getColumnDataForTypedObject(T var1) {
         return new Object[]{Boolean.FALSE, var1};
      }

      @Override
      public void setValueAt(Object var1, int var2, int var3) {
         if (var1 == Boolean.TRUE && SelectorTable.this.mode == SelectorTable.Mode.SINGLE) {
            this.doExclusiveRowSelection(var2, var3);
         } else {
            super.setValueAt(var1, var2, var3);
         }
      }

      @Override
      public boolean isCellEditable(int var1, int var2) {
         return !super.isCellEditable(var1, var2)
            ? false
            : SelectorTable.this.disabledRows == null || !SelectorTable.this.disabledRows.contains(this.getRowObjectAt(var1));
      }

      private void doExclusiveRowSelection(int var1, int var2) {
         SelectorTable.this.selSource.setBlocked(true);

         try {
            this.clearSelection();
            super.setValueAt(Boolean.TRUE, var1, var2);
         } finally {
            SelectorTable.this.selSource.setBlocked(false);
         }

         SelectorTable.this.selSource.fireSelectionChanged();
      }
   }

   public static class ObjectRenderer extends DefaultTableCellRenderer {
      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         return super.getTableCellRendererComponent(var1, var2, var3, false, var5, var6);
      }
   }

   private class RendererPreparerImpl implements TableRendererPreparer {
      private RendererPreparerImpl() {
      }

      @Override
      public boolean doDefaultPreparation() {
         return true;
      }

      @Override
      public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
         boolean var6 = var1.isRowSelected(var4);
         if (!var6 && SelectorTable.this.isRowDisabled(var4)) {
            var3.setForeground(Color.GRAY);
         } else {
            var3.setForeground(var6 ? var1.getSelectionForeground() : var1.getForeground());
         }

         return var3;
      }
   }

   private class RowObjectFilter<T> extends RowFilter<SelectorTable<E>.Model<T>, Integer> {
      private final Predicate<? super T> condition;
      private final boolean alwaysAcceptSelectedRows;

      public RowObjectFilter(Predicate<? super T> var2, boolean var3) {
         this.condition = var2;
         this.alwaysAcceptSelectedRows = var3;
      }

      @Override
      public boolean include(Entry<? extends SelectorTable<E>.Model<T>, ? extends Integer> var1) {
         Integer var2 = (Integer)var1.getIdentifier();
         if (this.alwaysAcceptSelectedRows) {
            boolean var3 = ((SelectorTable.Model)var1.getModel()).isSelected(var2);
            if (var3) {
               return true;
            }
         }

         Object var4 = ((SelectorTable.Model)var1.getModel()).getRowObjectAt(var2);
         return this.condition.apply((T)var4);
      }
   }

   private class SelectionSourceImpl implements MutableSelectionSource, TableModelListener {
      private final SelectionSourceWidgetSupport support = new SelectionSourceWidgetSupport(this, SelectorTable.this.table.getAdaptee());
      private boolean blocked;

      public SelectionSourceImpl() {
         SelectorTable.this.table.getModel().addTableModelListener(this);
      }

      public IndexedSelection getSelection() {
         IndexedSelectionBuilder var1 = new IndexedSelectionBuilder();

         for (int var2 = 0; var2 < SelectorTable.this.model.getRowCount(); var2++) {
            if (SelectorTable.this.model.isSelected(var2)) {
               var1.add(SelectorTable.this.model.getRowObjectAt(var2), var2);
            }
         }

         return var1.build();
      }

      @Override
      public void setSelection(Selection var1) {
         this.blocked = true;

         try {
            this.setSelectionImpl(var1);
         } finally {
            this.blocked = false;
         }
      }

      public void setBlocked(boolean var1) {
         this.blocked = var1;
      }

      public void fireSelectionChanged() {
         this.support.fireSelectionChange(this.getSelection());
      }

      private void setSelectionImpl(Selection var1) {
         if (var1 == null || var1.isEmpty()) {
            SelectorTable.this.model.clearSelection();
         } else if (var1 instanceof IndexedSelection) {
            SelectorTable.this.model.setSelected(((IndexedSelection)var1).indeces());
         } else {
            if (!(var1 instanceof OrderedSelection)) {
               throw new IllegalArgumentException("Invalid selection type");
            }

            SelectorTable.this.model.setSelected(((OrderedSelection)var1).toArray());
         }

         this.support.fireSelectionChange(var1);
      }

      @Override
      public void addSelectionListener(SelectionListener var1) {
         this.support.addListener(var1);
      }

      @Override
      public void removeSelectionListener(SelectionListener var1) {
         this.support.removeListener(var1);
      }

      @Override
      public void tableChanged(TableModelEvent var1) {
         if (!this.blocked) {
            if (var1.getType() == 0) {
               this.fireSelectionChanged();
            }
         }
      }
   }
}
