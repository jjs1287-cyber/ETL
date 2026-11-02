package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.LdapSecurityLevel;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.LayoutContainer;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class SecurityLevelDefiner implements LdapLocationPanelPart {
   private final CheckBoxInputField secureActivator;
   private final CheckBoxInputField sslActivator;

   public SecurityLevelDefiner(InputPanel var1) {
      this.secureActivator = this.createSecureActivator(var1);
      this.sslActivator = this.createSslActivator(var1, this.secureActivator);
   }

   @Override
   public void layout(LayoutContainer var1) {
      UiProvider var2 = this.getLayout();
      var1.add(var2);
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(null, InvisiblePanel.of(this.getLayout()), false);
   }

   public void applyTo(LdapLocation var1) {
      LdapSecurityLevel var2 = LdapSecurityLevel.NON_SECURE;
      if (this.secureActivator.isSelected()) {
         var2 = this.sslActivator.isSelected() ? LdapSecurityLevel.SECURE_SSL : LdapSecurityLevel.SECURE;
      }

      var1.setSecurityLevel(var2);
   }

   @Override
   public void dispose() {
   }

   public void reset(LdapLocation var1) {
      LdapSecurityLevel var2 = var1.getSecurityLevel();
      this.secureActivator.setSelected(var2 != LdapSecurityLevel.NON_SECURE);
      this.sslActivator.setSelected(var2 == LdapSecurityLevel.SECURE_SSL);
   }

   private CheckBoxInputField createSecureActivator(InputPanel var1) {
      return this.createCheckBox(var1, Strings.get("Ldap.Secure"));
   }

   private CheckBoxInputField createSslActivator(InputPanel var1, CheckBoxInputField var2) {
      CheckBoxInputField var3 = this.createCheckBox(var1, Strings.get("Ldap.EnableSSL"));
      ToggleButtonActivationConstraint var4 = new ToggleButtonActivationConstraint(var2);
      var4.bind(var3, true);
      var3.addActivationConstraint(var4);
      return var3;
   }

   private CheckBoxInputField createCheckBox(InputPanel var1, String var2) {
      String var3 = Strings.get("Ldap.ConnectionMode");
      CheckBoxInputField var4 = InputFieldFactory.getCheckBoxInputField(var3, var2);
      var1.addInputField(var4);
      return var4;
   }

   private UiProvider getLayout() {
      BorderBuilder var1 = new BorderBuilder().titled(Strings.get("Ldap.ConnectionMode")).empty(10, 10, 5, 50);
      return new OneColumnPanel()
         .withInsets(0, 0, 10, 0)
         .withBorder(var1)
         .setWeightX(1.0)
         .add(InputFieldFactory.getCapsule(this.secureActivator))
         .add(InputFieldFactory.getCapsule(this.sslActivator));
   }
}
