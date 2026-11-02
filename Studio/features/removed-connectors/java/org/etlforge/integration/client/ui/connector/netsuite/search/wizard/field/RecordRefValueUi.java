package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.List;
import org.etlforge.application.ui.widget.FilteredListPopupSelector;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.NetSuiteRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueRecordRef;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.ui.widget.text.BeanStringPropertyBindings;
import org.etlforge.ui.widget.text.KongaTextField;

final class RecordRefValueUi extends ValueUi {
   private final ImmutableSet<String> types;
   private final FilteredListPopupSelector<String> typeSelector;
   private final KongaTextField typeField = new KongaTextField();
   private final KongaTextField nameField = new KongaTextField();
   private final KongaTextField internalIdField = new KongaTextField();
   private final KongaTextField externalIdField = new KongaTextField();
   private final BeanStringPropertyBindings fieldBindings;

   public RecordRefValueUi(SearchFieldRecordRef var1) {
      this(var1.getPredefinedList(), false);
   }

   public RecordRefValueUi(List<String> var1, boolean var2) {
      if (!var2) {
         this.types = ImmutableSet.copyOf(var1);
         this.typeSelector = buildTypeSelector(var1);
         this.fieldBindings = new BeanStringPropertyBindings(
            NetSuiteRecordRef.class,
            Arrays.asList("name", "internalId", "externalId"),
            Arrays.asList(this.nameField, this.internalIdField, this.externalIdField)
         );
      } else {
         this.types = null;
         this.typeSelector = null;
         this.fieldBindings = new BeanStringPropertyBindings(
            NetSuiteRecordRef.class,
            Arrays.asList("type", "name", "internalId", "externalId"),
            Arrays.asList(this.typeField, this.nameField, this.internalIdField, this.externalIdField)
         );
      }
   }

   private static FilteredListPopupSelector<String> buildTypeSelector(List<String> var0) {
      TokenListCompletionContext var1 = new TokenListCompletionContext(var0);
      var1.setDeleteSupported(false);
      DropDownAutoCompleter var2 = new DropDownAutoCompleter(var1);
      return FilteredListPopupSelector.builder(String.class)
         .withValues(var0)
         .withManualEntry(Functions.identity(), var2)
         .deriveFieldWidthFromValues(Size.Columns.of(25))
         .build();
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.typeSelector == null) {
         TwoColumnPanel var2 = TwoColumnPanel.forFormEntry()
            .addLabeledComponent("TypeId:", this.typeField)
            .addLabeledComponent("Name:", this.nameField)
            .addLabeledComponent("Internal ID:", this.internalIdField)
            .addLabeledComponent("External ID:", this.externalIdField);
         Vertical.Anchor.north(var2).decorate(var1);
      } else {
         TwoColumnPanel var3 = TwoColumnPanel.forFormEntry()
            .addLabeledComponent("Type:", this.typeSelector)
            .addLabeledComponent("Name:", this.nameField)
            .addLabeledComponent("Internal ID:", this.internalIdField)
            .addLabeledComponent("External ID:", this.externalIdField);
         Vertical.Anchor.north(var3).decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      if (this.typeSelector != null) {
         this.typeSelector.requestFocus();
      } else {
         this.typeField.requestFocus();
      }
   }

   protected SearchFieldValueRecordRef constructValue(NetSuiteSearchOperator var1) {
      NetSuiteRecordRef var2 = this.getRecordRef();
      return new SearchFieldValueRecordRef(var1, var2);
   }

   public NetSuiteRecordRef getRecordRef() {
      NetSuiteRecordRef var1 = new NetSuiteRecordRef();
      if (this.typeSelector != null) {
         var1.setType(this.typeSelector.getSelectedItem());
      }

      this.fieldBindings.updateBean(var1);
      return var1;
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueRecordRef var2 = ArgCheck.checkInstance(var1, SearchFieldValueRecordRef.class);
      NetSuiteRecordRef var3 = var2.getSearchValue();
      this.setRecordRef(var3);
   }

   public void setRecordRef(NetSuiteRecordRef var1) {
      if (this.typeSelector != null) {
         this.typeSelector.setSelectedValue(var1.getType());
      }

      this.fieldBindings.updateUi(var1);
   }

   @Override
   public ValueUi.ValidationResult validateInput() {
      if (this.typeSelector == null) {
         return ValueUi.ValidationResult.success();
      }

      String var1 = this.typeSelector.getSelectedItem();
      if (Strings.isNullOrEmpty(var1)) {
         return ValueUi.ValidationResult.success();
      }

      boolean var2 = this.types.contains(var1);
      return var2 ? ValueUi.ValidationResult.success() : ValueUi.ValidationResult.error("Invalid type: " + var1 + ". Please select a type from the list.");
   }
}
