package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.LayoutContainer;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;

final class ServerDefiner implements LdapLocationPanelPart {
   private final TextInputField2 field;
   private TokenListCompletionSupport autoCompleteSupport;

   public ServerDefiner(InputPanel var1, int var2) {
      KongaLabel var3 = KongaLabel.withMnemonic(Strings.get("Ldap.Server.Label"));
      this.field = InputFieldFactory.getNonEmptyTextInputField(Strings.get("Ldap.Server.Name"), var3, var2, false);
      var1.addInputField(this.field, this.field, this.field);
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

   public void applyTo(LdapLocation var1) {
      var1.setServer(this.field.getValue().trim());
   }

   @Override
   public void dispose() {
      this.field.dispose();
      this.uninstallAutoCompletion();
   }

   public void reset(LdapLocation var1) {
      this.field.setValue(var1.getServer());
   }

   @Override
   public void layout(LayoutContainer var1) {
      var1.add(InputFieldFactory.getCapsule(this.field));
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.field);
   }

   public void requestFocus() {
      this.field.requestFocus();
   }
}
