package org.etlforge.application.ui.widget;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.JWindow;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.callback.Receiver;
import org.etlforge.system.thread.Runnables;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.widget.ComponentDropDownWindow;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.OutsideWindowMouseClickListener;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.list.FilteredKongaList;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListItemAppearance;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.window.WindowEscapeCloser;

public final class FilteredListPopupSelector<T> implements UiProvider {
   private final Class<T> valueClass;
   private final FilteredListPopupSelector.ListPopulator<T> listPopulator;
   private final Runnable runWhenReady;
   private final Function<? super T, String> displayNameFunction;
   private final KongaTextField textField = new KongaTextField();
   private final FilteredKongaList list;
   private final ListItemAppearance listItemAppearance;
   private final FilteredListPopupSelector<T>.OpenListAction openListAction = new FilteredListPopupSelector.OpenListAction();
   private final KongaButton openListButton = new KongaButton(this.openListAction);
   private JWindow listWindow;
   private FilteredListPopupSelector<T>.MouseCloser mouseCloser;
   private T selectedValue;
   private boolean blockDocumentEvents;
   private final SelectionListeners selectionListeners = new SelectionListeners();

   public static <E> FilteredListPopupSelector.Builder<E> builder(Class<E> var0) {
      return new FilteredListPopupSelector.Builder<>(var0);
   }

   private FilteredListPopupSelector(
      Class<T> var1,
      FilteredListPopupSelector.ListPopulator<T> var2,
      Function<? super T, String> var3,
      @Nullable String var4,
      ListItemAppearance var5,
      Runnable var6
   ) {
      this.valueClass = var1;
      this.listPopulator = var2;
      this.displayNameFunction = var3;
      this.runWhenReady = var6;
      this.listItemAppearance = var5;
      this.list = this.createList();
      this.configureTextField(var4);
   }

   private FilteredKongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(12);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      this.listItemAppearance.configure(var1);
      return FilteredKongaList.builder(var1)
         .withFilterFunction(new FilteredListPopupSelector.FilterFunction())
         .withFilterLabel("Filter: ")
         .withDefaultAction(new FilteredListPopupSelector.SelectAction(this.valueClass))
         .buildWithFilterAbove();
   }

   private void configureTextField(@Nullable String var1) {
      this.textField.setEditable(false);
      this.textField.setLabel(var1);
      ActionKeyBinding.install(this.openListAction, this.textField, KeyUtils.getAltDown(40));
   }

   public T getSelectedItem() {
      return this.selectedValue;
   }

   public void setSelectedValue(@Nullable T var1) {
      this.setSelectedValue((T)var1, true);
   }

   private void setSelectedValue(@Nullable T var1, boolean var2) {
      if (!Objects.equal(this.selectedValue, var1)) {
         this.selectedValue = (T)var1;
         if (var2) {
            String var3 = var1 == null ? "" : this.displayNameFunction.apply((T)var1);
            this.blockDocumentEvents = true;
            this.textField.setText(var3);
            this.blockDocumentEvents = false;
         }

         this.fireSelectionEvent((T)var1);
      }
   }

   private void fireSelectionEvent(T var1) {
      OrderedSelection var2 = var1 == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
      this.selectionListeners.call().selectionChanged(new SelectionChangedEvent(this, var2));
   }

   public void setTextValue(String var1) {
      this.textField.setText(var1);
   }

   public String getTextValue() {
      return this.textField.getText();
   }

   public void addSelectionListener(SelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   public void addFocusListener(FocusListener var1) {
      this.textField.addFocusListener(var1);
      this.openListButton.addFocusListener(var1);
   }

   public void removeFocusListener(FocusListener var1) {
      this.textField.removeFocusListener(var1);
      this.openListButton.removeFocusListener(var1);
   }

   public void setEnabled(boolean var1) {
      this.textField.setEnabled(var1);
      this.openListAction.setEnabled(var1);
   }

   public WaitLock showWaitMessage(String var1) {
      this.blockDocumentEvents = true;
      this.textField.setForeground(Color.GRAY);
      this.textField.setCursor(Cursor.getPredefinedCursor(3));
      this.textField.setText(var1);
      final boolean var2 = this.textField.isEditable();
      return new WaitLock() {
         private boolean released;

         @Override
         public synchronized void release() {
            if (!this.released) {
               FilteredListPopupSelector.this.textField.setText("");
               FilteredListPopupSelector.this.textField.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
               FilteredListPopupSelector.this.blockDocumentEvents = false;
               FilteredListPopupSelector.this.textField.setCursor(Cursor.getDefaultCursor());
               FilteredListPopupSelector.this.textField.setEditable(var2);
            }
         }
      };
   }

   public void setFieldWidth(Size.Columns var1) {
      this.textField.setColumns(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().bottomAligned().addAllWithSpace(5, this.textField.asUiProvider(), this.openListButton).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocusInWindow();
   }

   private void enableManualEntry(final Function<String, T> var1, @Nullable AutoCompleter var2) {
      this.textField.setEditable(true);
      this.textField.getDocument().addDocumentListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1x) {
            if (!FilteredListPopupSelector.this.blockDocumentEvents) {
               String var2x = FilteredListPopupSelector.this.textField.getText().trim();
               T var3 = var1.apply(var2x);
               FilteredListPopupSelector.this.setSelectedValue(var3, false);
            }
         }
      });
      if (var2 != null) {
         var2.install(this.textField);
      }
   }

   private void populateList() {
      this.listPopulator.populate(this.list, new Runnable() {
         @Override
         public void run() {
            FilteredListPopupSelector.this.openListAction.setReady();
            FilteredListPopupSelector.this.runWhenReady.run();
         }
      });
   }

   private void doWindowClosedCleanup() {
      if (this.mouseCloser != null) {
         this.mouseCloser.dispose();
         this.mouseCloser = null;
      }

      this.listWindow = null;
   }

   private static final class BackgroundThreadListPopulator<T> implements FilteredListPopupSelector.ListPopulator<T> {
      private final Callable<FilteredListModel> producer;

      public BackgroundThreadListPopulator(final Callable<? extends Iterable<T>> var1) {
         this.producer = new Callable<FilteredListModel>() {
            public FilteredListModel call() throws Exception {
               Iterable var1x = (Iterable)var1.call();
               return new FilteredListModel(ListUtils.createModel(var1x));
            }
         };
      }

      @Override
      public void populate(final FilteredKongaList var1, final Runnable var2) {
         Receiver var3 = new Receiver<FilteredListModel>() {
            public void handle(FilteredListModel var1x) {
               var1.setModel(var1x);
               var2.run();
            }
         };
         Computation.create(this.producer, var3).swingWorkerBuilder().buildAndExecute();
      }
   }

   public static final class Builder<E> {
      private final Class<E> valueClass;
      private FilteredListPopupSelector.ListPopulator<E> listPopulator;
      private Function<? super E, String> displayNameFunction = Functions.toStringFunction();
      @Nullable
      private Function<String, E> valueLookup;
      @Nullable
      private AutoCompleter autoCompleter;
      @Nullable
      private String fieldLabel;
      private ListItemAppearance listAppearance = new ListItemAppearance();
      private Runnable runWhenReady = Runnables.NO_OP;
      private boolean deriveFieldWidthFromValues;
      private Size.Columns maxDerivedWidth;

      public Builder(Class<E> var1) {
         this.valueClass = Preconditions.checkNotNull(var1);
      }

      public FilteredListPopupSelector.Builder<E> withValues(Iterable<E> var1) {
         this.listPopulator = new FilteredListPopupSelector.FixedValuesListPopulator(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withValueProducer(Callable<? extends Iterable<E>> var1) {
         this.listPopulator = new FilteredListPopupSelector.BackgroundThreadListPopulator(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withDisplayNameFunction(Function<? super E, String> var1) {
         this.displayNameFunction = Preconditions.checkNotNull(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withManualEntry(Function<String, E> var1) {
         this.valueLookup = Preconditions.checkNotNull(var1);
         this.autoCompleter = null;
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withManualEntry(Function<String, E> var1, AutoCompleter var2) {
         this.valueLookup = Preconditions.checkNotNull(var1);
         this.autoCompleter = Preconditions.checkNotNull(var2);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withFieldLabel(String var1) {
         this.fieldLabel = ArgCheck.checkNotEmpty(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> withListAppearance(ListItemAppearance var1) {
         this.listAppearance = Preconditions.checkNotNull(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> runWhenReady(Runnable var1) {
         this.runWhenReady = Preconditions.checkNotNull(var1);
         return this;
      }

      public FilteredListPopupSelector.Builder<E> deriveFieldWidthFromValues(Size.Columns var1) {
         this.maxDerivedWidth = Preconditions.checkNotNull(var1);
         this.deriveFieldWidthFromValues = true;
         return this;
      }

      public FilteredListPopupSelector<E> build() {
         Preconditions.checkState(
            this.listPopulator != null, "No values have been provided. Please use withValues or withValueProducer before building the selector."
         );
         FilteredListPopupSelector var1 = new FilteredListPopupSelector(
            this.valueClass, this.listPopulator, this.displayNameFunction, this.fieldLabel, this.listAppearance, this.runWhenReady
         );
         var1.populateList();
         if (this.valueLookup != null) {
            var1.enableManualEntry(this.valueLookup, this.autoCompleter);
         }

         if (this.listPopulator instanceof FilteredListPopupSelector.FixedValuesListPopulator && this.deriveFieldWidthFromValues) {
            Size.Columns var2 = this.deriveFieldWidth();
            var1.setFieldWidth(var2);
         }

         return var1;
      }

      private Size.Columns deriveFieldWidth() {
         int var1 = 10;

         for (Object var4 : (FilteredListPopupSelector.FixedValuesListPopulator)this.listPopulator) {
            String var5 = this.displayNameFunction.apply((E)var4);
            var1 = Ints.max(var1, var5.length());
         }

         return Ordering.natural().min(Size.Columns.of(var1), this.maxDerivedWidth);
      }
   }

   private class Filter implements ListFilter {
      private final FilteredListPopupSelector.FilterString filterString;

      public Filter(FilteredListPopupSelector.FilterString var2) {
         this.filterString = var2;
      }

      @Override
      public boolean test(Object var1) {
         String var2 = FilteredListPopupSelector.this.listItemAppearance.getDisplayValue(var1);
         return this.filterString.matches(var2);
      }
   }

   private class FilterFunction implements Function<String, ListFilter> {
      private FilterFunction() {
      }

      public ListFilter apply(String var1) {
         if (var1.isEmpty()) {
            return null;
         }

         FilteredListPopupSelector.FilterString var2 = new FilteredListPopupSelector.FilterString(var1);
         return FilteredListPopupSelector.this.new Filter(var2);
      }
   }

   private static class FilterString {
      private final boolean exactMatch;
      private final String text;

      public FilterString(String var1) {
         this.exactMatch = var1.endsWith(" ");
         this.text = var1.trim().toLowerCase();
      }

      public boolean matches(String var1) {
         return this.exactMatch ? var1.equalsIgnoreCase(this.text) : var1.toLowerCase().contains(this.text);
      }
   }

   private static final class FixedValuesListPopulator<T> implements FilteredListPopupSelector.ListPopulator<T>, Iterable<T> {
      private final Iterable<T> values;

      public FixedValuesListPopulator(Iterable<T> var1) {
         this.values = var1;
      }

      @Override
      public void populate(FilteredKongaList var1, Runnable var2) {
         FilteredListModel var3 = new FilteredListModel(ListUtils.createModel(this.values));
         var1.setModel(var3);
         var2.run();
      }

      @Override
      public Iterator<T> iterator() {
         return this.values.iterator();
      }
   }

   private interface ListPopulator<T> {
      void populate(FilteredKongaList var1, Runnable var2);
   }

   private class MouseCloser extends OutsideWindowMouseClickListener {
      public MouseCloser(JWindow var2) {
         super(var2);
         this.setDisposeOnOutsideClick(true);
      }

      @Override
      protected void onOutsideClick() {
         if (FilteredListPopupSelector.this.listWindow != null) {
            FilteredListPopupSelector.this.listWindow.dispose();
            FilteredListPopupSelector.this.doWindowClosedCleanup();
         }
      }
   }

   private class OpenListAction extends AbstractAction {
      private UiProvider ui;
      private boolean ready;

      public OpenListAction() {
         super("Select...");
         this.setEnabled(false);
      }

      public void setReady() {
         this.ready = true;
         this.setEnabled(true);
      }

      @Override
      public void setEnabled(boolean var1) {
         super.setEnabled(var1 && this.ready);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (OpenListAction.this.isEnabled()) {
                  OpenListAction.this.select();
               }
            }
         });
      }

      private void select() {
         this.createUiIfNecessary();
         ComponentDropDownWindow var1 = new ComponentDropDownWindow(FilteredListPopupSelector.this.textField, this.ui) {
            @Override
            protected void onWindowOpened() {
               FilteredListPopupSelector.this.list.selectFirst();
               FilteredListPopupSelector.this.listWindow.toFront();
               FilteredListPopupSelector.this.listWindow.requestFocus();
               FilteredListPopupSelector.this.list.requestFocus();
            }
         };
         var1.setTrackWidthOfOwner(true);
         FilteredListPopupSelector.this.listWindow = var1.buildWindow();
         FilteredListPopupSelector.this.listWindow.setFocusableWindowState(true);
         FilteredListPopupSelector.this.mouseCloser = FilteredListPopupSelector.this.new MouseCloser(FilteredListPopupSelector.this.listWindow);
         this.bindEscape();
         FilteredListPopupSelector.this.listWindow.setVisible(true);
      }

      private void createUiIfNecessary() {
         if (this.ui == null) {
            this.ui = new BorderLayoutBuilder().withBorder(new BorderBuilder().line().empty(4)).center(FilteredListPopupSelector.this.list);
         }
      }

      private void bindEscape() {
         WindowEscapeCloser var1 = WindowEscapeCloser.bind(FilteredListPopupSelector.this.listWindow);
         var1.addRunToJobOnClose(new Runnable() {
            @Override
            public void run() {
               FilteredListPopupSelector.this.doWindowClosedCleanup();
            }
         });
      }
   }

   private class SelectAction extends AbstractSingleObjectSelectionAction<T> {
      public SelectAction(Class<T> var2) {
         super(var2);
      }

      @Override
      protected void run(T var1, ActionEvent var2) {
         if (FilteredListPopupSelector.this.listWindow != null) {
            FilteredListPopupSelector.this.listWindow.dispose();
            FilteredListPopupSelector.this.doWindowClosedCleanup();
         }

         FilteredListPopupSelector.this.setSelectedValue((T)var1);
      }
   }
}

