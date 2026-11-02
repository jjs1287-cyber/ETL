package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.List;
import org.etlforge.application.ui.widget.FilteredListPopupSelector;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

final class OperatorSelector implements UiProvider {
   private final ImmutableMap<String, ConditionOperator> operatorLookup;
   private final FilteredListPopupSelector<ConditionOperator> ui;

   public OperatorSelector() {
      ImmutableList var1 = ConditionOperator.operatorList();
      this.operatorLookup = Maps.uniqueIndex(var1, Functions.toStringFunction());
      this.ui = FilteredListPopupSelector.builder(ConditionOperator.class)
         .withValues(var1)
         .withManualEntry(Functions.forMap(this.operatorLookup, null), this.autoCompleter(var1))
         .withFieldLabel("Type an operator name, or click Select to select an operator")
         .build();
   }

   private AutoCompleter autoCompleter(List<ConditionOperator> var1) {
      TokenListCompletionContext var2 = new TokenListCompletionContext(Iterables.transform(var1, Functions.toStringFunction()));
      return new DropDownAutoCompleter(var2);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.ui.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.ui.removeSelectionListener(var1);
   }

   public ConditionOperator getSelectedOperator() {
      return this.ui.getSelectedItem();
   }

   public boolean hasSelection() {
      return this.getSelectedOperator() != null;
   }

   public void setSelectedOperator(ConditionOperator var1) {
      this.ui.setSelectedValue(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }
}
