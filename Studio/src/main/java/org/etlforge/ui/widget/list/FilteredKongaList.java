package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

public final class FilteredKongaList implements KongaList, UiProvider {
   private final KongaList list;
   private final ListFilterField filterField;
   private final FilteredKongaList.Orientation orientation;

   public static FilteredKongaList.Builder builder(KongaListFactory var0) {
      return new FilteredKongaList.Builder(var0.newList());
   }

   public static FilteredKongaList.Builder builder(KongaList var0) {
      return new FilteredKongaList.Builder(var0);
   }

   private FilteredKongaList(KongaList var1, Function<String, ListFilter> var2, FilteredKongaList.Orientation var3) {
      this.list = var1;
      this.filterField = new ListFilterField(var2);
      this.orientation = var3;
      this.filterField.associateWithList(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.center(this.list);
      switch (this.orientation) {
         case FIELD_ABOVE:
            var2.north(this.filterField);
            break;
         case FIELD_BELOW:
            var2.south(this.filterField);
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   @Override
   public void addKeyBinding(KeyStroke var1, String var2, Action var3) {
      this.list.addKeyBinding(var1, var2, var3);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
      this.filterField.addActionListener(var1);
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.list.addFocusListener(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.list.removeFocusListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.list.removeSelectionListener(var1);
   }

   @Override
   public ListModel getModel() {
      return this.list.getModel();
   }

   @Override
   public void setModel(ListModel var1) {
      this.list.setModel(var1);
   }

   @Override
   public void setEnabled(boolean var1) {
      this.list.setEnabled(var1);
   }

   @Override
   public IndexedSelection getSelection() {
      return this.list.getSelection();
   }

   @Override
   public void setSelection(Selection var1) {
      this.list.setSelection(var1);
   }

   @Override
   public void setSelectedIndex(int var1) {
      this.list.setSelectedIndex(var1);
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   @Override
   public void selectFirst() {
      this.list.selectFirst();
   }

   @Override
   public Disposable installKeyTraversalBindings(JComponent var1) {
      return this.list.installKeyTraversalBindings(var1);
   }

   @Override
   public void setTransient(boolean var1) {
      this.list.setTransient(var1);
   }

   @Override
   public boolean processKeyEvent(KeyEvent var1) {
      return this.list.processKeyEvent(var1);
   }

   @Override
   public void ensureSelectedIndexVisible() {
      this.list.ensureSelectedIndexVisible();
   }

   @Override
   public void repaint() {
      this.list.repaint();
   }

   public static final class Builder {
      private final KongaList list;
      private Function<String, ListFilter> filterFunction = new Function<String, ListFilter>() {
         public ListFilter apply(@Nullable String var1) {
            return Strings.isNullOrEmpty(var1) ? ListFilter.ACCEPT_ALL : new ListFilter.ToStringContains(var1);
         }
      };
      private String filterLabel = "&Filter: ";
      private Optional<TextStyle> filterLabelStyle = Optional.absent();
      private Optional<SelectionAction> defaultAction = Optional.absent();

      public Builder(KongaList var1) {
         this.list = Preconditions.checkNotNull(var1);
      }

      public FilteredKongaList.Builder withFilterFunction(Function<String, ListFilter> var1) {
         this.filterFunction = Preconditions.checkNotNull(var1);
         return this;
      }

      public FilteredKongaList.Builder withFilterLabel(String var1) {
         this.filterLabel = var1;
         return this;
      }

      public FilteredKongaList.Builder withFilterLabelStyle(TextStyle var1) {
         this.filterLabelStyle = Optional.of(var1);
         return this;
      }

      public FilteredKongaList.Builder withDefaultAction(SelectionAction var1) {
         this.defaultAction = Optional.of(var1);
         return this;
      }

      public FilteredKongaList buildWithFilterAbove() {
         return this.build(FilteredKongaList.Orientation.FIELD_ABOVE);
      }

      public FilteredKongaList buildWithFilterBelow() {
         return this.build(FilteredKongaList.Orientation.FIELD_BELOW);
      }

      private FilteredKongaList build(FilteredKongaList.Orientation var1) {
         FilteredKongaList var2 = new FilteredKongaList(this.list, this.filterFunction, var1);
         var2.filterField.setLabel(Strings.isNullOrEmpty(this.filterLabel) ? null : KongaLabel.withMnemonic(this.filterLabel));
         if (this.filterLabelStyle.isPresent()) {
            this.filterLabelStyle.get().makeOver(var2.filterField.getLabel());
         }

         if (this.defaultAction.isPresent()) {
            var2.setDefaultAction(this.defaultAction.get());
         }

         return var2;
      }
   }

   private enum Orientation {
      FIELD_ABOVE,
      FIELD_BELOW;
   }
}
