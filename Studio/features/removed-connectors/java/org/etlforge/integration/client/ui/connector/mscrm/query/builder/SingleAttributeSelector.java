package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import org.etlforge.application.ui.widget.FilteredListPopupSelector;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.util.name.Nameables;

final class SingleAttributeSelector implements UiProvider {
   private final ImmutableMap<String, MSCrmAttribute> allAttributes;
   private final FilteredListPopupSelector<MSCrmAttribute> ui;

   public SingleAttributeSelector(MSCrmObject var1) {
      this(var1.getAttributes());
   }

   public SingleAttributeSelector(Iterable<MSCrmAttribute> var1) {
      this.allAttributes = Nameables.toImmutableSortedMap(var1);
      this.ui = this.buildUi();
   }

   private FilteredListPopupSelector<MSCrmAttribute> buildUi() {
      return FilteredListPopupSelector.builder(MSCrmAttribute.class)
         .withValues(this.allAttributes.values())
         .withManualEntry(Functions.forMap(this.allAttributes, null), this.autoCompleter())
         .withFieldLabel("Type an attribute name, or click Select to select an attribute")
         .build();
   }

   private AutoCompleter autoCompleter() {
      TokenListCompletionContext var1 = new TokenListCompletionContext(this.allAttributes.keySet());
      return new DropDownAutoCompleter(var1);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.ui.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.ui.removeSelectionListener(var1);
   }

   public MSCrmAttribute getSelectedAttribute() {
      return this.ui.getSelectedItem();
   }

   public boolean hasSelection() {
      return this.getSelectedAttribute() != null;
   }

   public void setSelectedAttribute(MSCrmAttribute var1) {
      this.ui.setSelectedValue(var1);
   }

   public void setSelectedAttributeByName(String var1) {
      MSCrmAttribute var2 = this.allAttributes.get(var1);
      this.setSelectedAttribute(var2);
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
