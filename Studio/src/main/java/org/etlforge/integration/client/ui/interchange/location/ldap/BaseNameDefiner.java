package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.LayoutContainer;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;

final class BaseNameDefiner implements LdapLocationPanelPart {
   private final TextInputField field;
   private TokenListCompletionSupport autoCompleteSupport;

   public BaseNameDefiner(InputPanel var1, int var2) {
      this.field = InputFieldFactory.getTextInputField(
         Strings.get("Ldap.BaseName.Name"), KongaLabel.withMnemonic(Strings.get("Ldap.BaseName.Label")), var2, true
      );
      this.field.setTextWhenEmpty(Strings.get("Ldap.BaseName.Example"));
      var1.addInputField(this.field);
   }

   @Override
   public void layout(LayoutContainer var1) {
      var1.add(InputFieldFactory.getCapsule(this.field));
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(null, InputFieldFactory.getCapsule(this.field), true);
   }

   public void applyTo(LdapLocation var1) {
      var1.setBaseName(this.field.getValue());
   }

   public void reset(LdapLocation var1) {
      this.field.setValue(var1.getBaseName());
   }

   @Override
   public void dispose() {
      this.field.dispose();
      this.uninstallAutoCompletion();
   }

   public void setHistory(StringValueHistory var1) {
      this.uninstallAutoCompletion();
      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   private void uninstallAutoCompletion() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }
   }
}
