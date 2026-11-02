package org.etlforge.application.ui.widget;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.selection.SelectionWidget;
import org.etlforge.ui.widget.table.TableUtils;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public class SelectionGrid<V> implements UiProvider, SelectionSourceWithDefaultAction, SelectionWidget {
   private final Class<V> valueClass;
   private final int numberOfColumns;
   private final SelectionGrid<V>.TableImpl table;
   private final SelectionGrid<V>.SelectionDispatcher selectionDispatcher = new SelectionGrid.SelectionDispatcher();
   private DefaultActionHandler defaultAction;
   private Predicate<? super V> filter;
   private ImmutableList<V> unfilteredValues = ImmutableList.of();
   private SelectionGrid<V>.ModelBinding modelBinding;
   private Disposable filterBinding;

   public static <T> SelectionGrid<T> emptyGrid(Class<T> var0, int var1) {
      return new SelectionGrid(var0, var1);
   }

   public SelectionGrid(Class<V> var1) {
      this(var1, 3);
   }

   public SelectionGrid(Class<V> var1, int var2) {
      this.valueClass = Preconditions.checkNotNull(var1, "valueClass");
      this.numberOfColumns = ArgCheck.checkGreaterThan(var2, 1, "numberOfColumns");
      this.table = this.createEmptyTable();
   }

   private SelectionGrid<V>.TableImpl createEmptyTable() {
      SelectionGrid.TableImpl var1 = new SelectionGrid.TableImpl(new Object[0][this.numberOfColumns], this.getColumnNames());
      var1.setCellSelectionEnabled(true);
      var1.setSelectionMode(0);
      var1.getSelectionModel().addListSelectionListener(this.selectionDispatcher);
      var1.getColumnModel().getSelectionModel().addListSelectionListener(this.selectionDispatcher);
      var1.setTableHeader(null);
      TableStyles.normal().makeOver(var1);
      Font var2 = TextStyles.DefaultTableText.getFont();
      Font var3 = var2.deriveFont(var2.getSize() + 1.0F);
      var1.setFont(var3);
      TableUtils.setVisibleRowCount(var1, 4);
      return var1;
   }

   private Object[] getColumnNames() {
      Object[] var1 = new Object[this.numberOfColumns];
      Arrays.fill(var1, "");
      return var1;
   }

   public final void setValues(Iterable<V> var1) {
      this.unfilteredValues = ImmutableList.copyOf(var1);
      List var2 = this.getFilteredList(this.unfilteredValues);
      ArrayList var3 = Lists.newArrayList();

      for (int var4 = 0; var4 < var2.size(); var4 += this.numberOfColumns) {
         Object[] var5 = new Object[this.numberOfColumns];

         for (int var6 = 0; var6 < this.numberOfColumns; var6++) {
            int var7 = var4 + var6;
            if (var7 == var2.size()) {
               break;
            }

            Object var8 = var2.get(var7);
            var5[var6] = var8;
         }

         var3.add(var5);
      }

      Object[][] var9 = new Object[var3.size()][];

      for (int var10 = 0; var10 < var3.size(); var10++) {
         var9[var10] = (Object[])var3.get(var10);
      }

      DefaultTableModel var11 = new DefaultTableModel(var9, this.getColumnNames());
      this.table.setModel(var11);
   }

   protected List<V> getFilteredList(ImmutableList<V> var1) {
      return this.filter == null ? var1 : KongaListUtils.filter(var1, this.filter);
   }

   public final Binding bindWithModel(ListProperty<V> var1, Property<V> var2) {
      if (this.modelBinding != null) {
         this.modelBinding.dispose();
      }

      this.modelBinding = new SelectionGrid.ModelBinding(var1, var2);
      this.modelBinding.syncUi();
      return this.modelBinding;
   }

   public final Predicate<? super V> getFilter() {
      return this.filter;
   }

   public final void setFilter(Predicate<? super V> var1) {
      if (var1 != this.filter) {
         this.filter = var1;
         this.setValues(this.unfilteredValues);
      }
   }

   public final Disposable bindFilterProperty(Property<Predicate<V>> var1) {
      return this.installNewFilterBinding(new SelectionGrid.FilterBinding(var1));
   }

   public final Disposable bindFilterProperty(Property<String> var1, Function<String, Predicate<? super V>> var2) {
      return this.installNewFilterBinding(new SelectionGrid.FilterStringBinding(var1, var2));
   }

   private Disposable installNewFilterBinding(SelectionGrid.AbstractFilterBinding var1) {
      if (this.filterBinding != null) {
         this.filterBinding.dispose();
      }

      var1.bind();
      this.filterBinding = var1;
      return var1;
   }

   public final void setRenderer(TableCellRenderer var1) {
      this.table.setCellRenderer(var1);
   }

   @Override
   public final void setDefaultAction(SelectionAction var1) {
      if (this.defaultAction != null) {
         this.defaultAction.uninstall();
      }

      this.selectionDispatcher.defaultAction = Preconditions.checkNotNull(var1);
      this.defaultAction = new DefaultActionHandler(this.table, var1);
      this.defaultAction.install();
   }

   public final OrderedSelection getSelection() {
      return this.selectionDispatcher.selection;
   }

   @Override
   public final void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.listeners.add(var1);
   }

   @Override
   public final void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.listeners.remove(var1);
   }

   @Override
   public final void selectFirst() {
      if (this.table.getRowCount() > 0) {
         this.table.changeSelection(0, 0, false, false);
      }
   }

   @Override
   public final boolean isEmpty() {
      return this.unfilteredValues.isEmpty();
   }

   @Override
   public final void decorate(UiCanvas var1) {
      KongaScrollPane var2 = new KongaScrollPane(this.table);
      var2.getViewport().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var1.display(var2);
   }

   @Override
   public final void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public final void repaint() {
      this.table.repaint();
   }

   private abstract static class AbstractFilterBinding implements Disposable {
      private AbstractFilterBinding() {
      }

      public abstract void bind();
   }

   private class FilterBinding extends SelectionGrid.AbstractFilterBinding implements PropertyChangeListener {
      private final Property<Predicate<V>> filter;

      public FilterBinding(Property<Predicate<V>> var2) {
         this.filter = var2;
      }

      @Override
      public void bind() {
         this.installFilter();
         this.filter.addListener(this);
      }

      private void installFilter() {
         SelectionGrid.this.setFilter(this.filter.get());
      }

      @Override
      public void dispose() {
         this.filter.removeListener(this);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.installFilter();
      }
   }

   private class FilterStringBinding extends SelectionGrid.AbstractFilterBinding implements PropertyChangeListener {
      private final Property<String> filterString;
      private final Function<String, Predicate<? super V>> filterFunction;
      private DelayedJob filterInstallationJob;

      public FilterStringBinding(Property<String> var2, Function<String, Predicate<? super V>> var3) {
         this.filterString = var2;
         this.filterFunction = var3;
      }

      @Override
      public void bind() {
         this.installFilter();
         this.filterString.addListener(this);
      }

      private void installFilter() {
         String var1 = this.filterString.get();
         Predicate var2 = this.filterFunction.apply(var1);
         SelectionGrid.this.setFilter(var2);
      }

      @Override
      public void dispose() {
         this.cancelPendingJob();
         this.filterString.removeListener(this);
      }

      private void cancelPendingJob() {
         if (this.filterInstallationJob != null) {
            this.filterInstallationJob.cancel();
            this.filterInstallationJob = null;
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.cancelPendingJob();
         this.filterInstallationJob = DelayedJob.createAndSchedule(200, new Runnable() {
            @Override
            public void run() {
               FilterStringBinding.this.installFilter();
               FilterStringBinding.this.filterInstallationJob = null;
            }
         });
      }
   }

   private class ModelBinding implements Binding {
      private final SelectionGrid<V>.ModelBinding.DataBinding dataBinding;
      private final SelectionGrid<V>.ModelBinding.SelectionBinding selectionBinding;

      public ModelBinding(ListProperty<V> var2, Property<V> var3) {
         this.dataBinding = new SelectionGrid.ModelBinding.DataBinding(var2);
         this.selectionBinding = new SelectionGrid.ModelBinding.SelectionBinding(var3);
      }

      @Override
      public void syncUi() {
         this.dataBinding.syncUi();
         this.selectionBinding.syncUi();
      }

      @Override
      public void syncModel() {
         this.selectionBinding.syncModel();
      }

      @Override
      public void setUiToModelEnabled(boolean var1) {
         this.selectionBinding.setUiToModelEnabled(var1);
      }

      @Override
      public void setModelToUiEnabled(boolean var1) {
         this.dataBinding.setModelToUiEnabled(var1);
         this.selectionBinding.setModelToUiEnabled(var1);
      }

      @Override
      public void dispose() {
         this.selectionBinding.dispose();
      }

      private class DataBinding extends AbstractBinding<List<V>> {
         public DataBinding(ListProperty<V> var2) {
            super(var2);
         }

         protected void updateUiWithValue(List<V> var1) {
            SelectionGrid.this.setValues(var1);
         }
      }

      private class SelectionBinding extends AbstractBinding<V> implements SelectionListener {
         public SelectionBinding(Property<V> var2) {
            super(var2);
            SelectionGrid.this.addSelectionListener(this);
         }

         @Override
         protected void updateUiWithValue(V var1) {
            int var2 = 0;

            for (int var3 = SelectionGrid.this.table.getRowCount(); var2 < var3; var2++) {
               for (int var4 = 0; var4 < SelectionGrid.this.numberOfColumns; var4++) {
                  if (SelectionGrid.this.table.getValueAt(var2, var4) == var1) {
                     SelectionGrid.this.table.changeSelection(var2, var4, false, false);
                     return;
                  }
               }
            }

            SelectionGrid.this.table.clearSelection();
         }

         @Override
         protected void disposeUiListener() {
            SelectionGrid.this.removeSelectionListener(this);
         }

         @Override
         protected V getValueFromUi() {
            return SelectionGrid.this.selectionDispatcher.selectedValue;
         }

         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            if (this.isUiToModelEnabled()) {
               this.syncModel();
            }
         }
      }
   }

   private class SelectionDispatcher implements ListSelectionListener {
      public SelectionAction defaultAction;
      public final SelectionListeners listeners = new SelectionListeners();
      public V selectedValue;
      public OrderedSelection selection = OrderedSelection.EMPTY;

      private SelectionDispatcher() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            this.selectedValue = (V)SelectionGrid.this.table.getSelectedObject();
            this.selection = this.selectedValue == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(this.selectedValue);
            if (this.defaultAction != null) {
               this.defaultAction.setSelection(this.selection);
            }

            SelectionChangedEvent var2 = new SelectionChangedEvent(SelectionGrid.this, this.selection);
            this.listeners.call().selectionChanged(var2);
         }
      }
   }

   private class TableImpl extends JTable {
      private TableCellRenderer renderer = new DefaultTableCellRenderer();

      public TableImpl(Object[][] var2, Object[] var3) {
         super(var2, var3);
      }

      @Override
      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public void setCellRenderer(TableCellRenderer var1) {
         this.renderer = var1;
         this.setDefaultRenderer(SelectionGrid.this.valueClass, var1);

         for (int var2 = 0; var2 < SelectionGrid.this.numberOfColumns; var2++) {
            this.getColumnModel().getColumn(var2).setCellRenderer(var1);
         }
      }

      @Override
      public TableCellRenderer getCellRenderer(int var1, int var2) {
         return this.renderer;
      }

      public V getSelectedObject() {
         int var1 = this.getSelectedRow();
         if (var1 == -1) {
            return null;
         }

         int var2 = this.getSelectedColumn();
         if (var2 == -1) {
            return null;
         }

         Object var3 = this.getValueAt(var1, var2);
         return SelectionGrid.this.valueClass.isInstance(var3) ? SelectionGrid.this.valueClass.cast(var3) : null;
      }
   }
}

