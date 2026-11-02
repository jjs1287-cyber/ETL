package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Functions;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.list.FilteredKongaList;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.StringMatcherListFilterFunction;

public final class FieldList implements UiProvider {
   private final FilteredKongaList listWidget = createList();

   public FieldList(QueryBuilderModel var1) {
      this.bindWithModel(var1);
   }

   private static FilteredKongaList createList() {
      KongaListFactory var0 = ComponentFactories.newKongaListFactory();
      var0.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      KongaList var1 = var0.newList();
      return FilteredKongaList.builder(var1)
         .withFilterLabelStyle(WizardStyleSheet.SMALL_CAPTION_STYLE)
         .withFilterFunction(StringMatcherListFilterFunction.of(StringMatchers.CONTAINS_IGNORE_CASE, Functions.toStringFunction()))
         .buildWithFilterBelow();
   }

   private void bindWithModel(QueryBuilderModel var1) {
      ListBinding var2 = new ListBinding(var1, "fields", this.listWidget);
      var2.setUseFilteredModel(true);
      Bindings.of(var2, new FieldList.SelectionBinding(var1)).syncUi();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.listWidget.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.listWidget.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.listWidget.requestFocus();
   }

   private class SelectionBinding extends AbstractBinding<String> implements SelectionListener {
      public SelectionBinding(QueryBuilderModel var2) {
         super(var2, "selectedField");
         FieldList.this.listWidget.addSelectionListener(this);
      }

      @Override
      protected void disposeUiListener() {
         FieldList.this.listWidget.removeSelectionListener(this);
      }

      protected String getValueFromUi() {
         IndexedSelection var1 = FieldList.this.listWidget.getSelection();
         return var1.size() == 1 ? (String)var1.head() : null;
      }

      protected void updateUiWithValue(String var1) {
         OrderedSelection var2 = var1 == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
         FieldList.this.listWidget.setSelection(var2);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (this.isUiToModelEnabled()) {
            this.syncModel();
         }
      }
   }
}
