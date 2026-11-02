package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.integration.salesforce.wsdl.Picklist;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.ui.widget.text.KongaTextField;

final class ConditionValueAutoCompleter {
   private final DropDownAutoCompleter autoCompleter;
   private final CompletionContext emptyContext = new TokenListCompletionContext(new String[0]);

   public ConditionValueAutoCompleter(KongaTextField var1) {
      AutoCompleterFactory var2 = AutoCompleterFactory.dropDownFactory();
      this.autoCompleter = (DropDownAutoCompleter)var2.createAutoCompleter(this.emptyContext);
      this.autoCompleter.setClickCountToTrigger(1);
      this.autoCompleter.install(var1);
   }

   public void setField(SObjectSchema.Field var1) {
      CompletionContext var2 = this.getCompletionContextForField(var1);
      this.autoCompleter.setCompletionContext(var2);
   }

   private CompletionContext getCompletionContextForField(SObjectSchema.Field var1) {
      if (var1 == null) {
         return this.emptyContext;
      }

      if (var1.getType() == SObjectSchema.Field.Type.BOOLEAN) {
         return this.createBooleanContext();
      }

      Picklist var2 = var1.getPicklist();
      return var2.isEmpty() ? this.emptyContext : new PicklistCompletionContext(var2);
   }

   private CompletionContext createBooleanContext() {
      TokenListCompletionContext var1 = new TokenListCompletionContext(new String[]{"TRUE", "FALSE"});
      var1.setDeleteSupported(false);
      return var1;
   }
}
