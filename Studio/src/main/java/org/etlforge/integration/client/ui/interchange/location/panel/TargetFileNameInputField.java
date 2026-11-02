package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public abstract class TargetFileNameInputField extends TextInputField2 {
   private final Map<String, String> invalidValues;
   private AutoCompleter autoCompleter;

   public TargetFileNameInputField(String var1, String var2, int var3) {
      super(var1, createInputComponent(var3), KongaLabel.withMnemonic(var2));
      this.setAutoTrim(true);
      this.invalidValues = Maps.newHashMap();
      this.installAutoComplete();
   }

   private void installAutoComplete() {
      TargetFileNameCompletionContext var1 = new TargetFileNameCompletionContext();
      AutoCompleterFactory var2 = AutoCompleterFactory.defaultFactory();
      this.autoCompleter = var2.createAutoCompleter(var1);
      this.autoCompleter.install(this.getInputComponent());
   }

   @Override
   public final void dispose() {
      try {
         this.autoCompleter.uninstall();
      } finally {
         super.dispose();
      }
   }

   @Override
   public final boolean isInputValid(String var1) {
      if (!this.isRequired() && var1.length() == 0) {
         return true;
      }

      Set var2 = this.getValidationIssues(var1);
      if (var2.isEmpty()) {
         return true;
      }

      StringBuilder var3 = new StringBuilder("<html>");

      for (Object __etlItem58 : var2) {
         ValidationIssue var5 = (ValidationIssue) __etlItem58;
         var3.append(var5.getMessage()).append("<br>");
      }

      var3.append("</html>");
      this.invalidValues.put(var1, var3.toString());
      return false;
   }

   protected abstract Set<ValidationIssue> getValidationIssues(String var1);

   @Override
   public final String getInvalidMessage(String var1) {
      assert this.invalidValues.containsKey(var1);
      return this.invalidValues.get(var1);
   }

   public final UiProvider asUiProvider() {
      JComponent var1 = InputFieldFactory.getCapsule(this);
      return UiProviderAdapter.of(var1);
   }

   private static JTextComponent createInputComponent(int var0) {
      return new KongaTextField(var0);
   }
}
