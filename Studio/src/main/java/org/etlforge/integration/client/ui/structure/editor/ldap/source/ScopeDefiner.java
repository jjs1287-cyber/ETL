package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.data.structure.ldap.LdapSearchScope;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ScopeDefiner implements UiProvider {
   private final RadioButtonGroupPanel<LdapSearchScope> selector = new RadioButtonGroupPanel<>();

   public ScopeDefiner() {
      for (LdapSearchScope var4 : LdapSearchScope.values()) {
         this.selector.addOption(var4, var4.name(), var4 == LdapSearchScope.ONE_LEVEL);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.selector.getUi(null, RadioButtonGroupPanel.Layout.ONE_ROW);
      JPanel var3 = InvisiblePanel.of(var2);
      var3.setBorder(StyleSheet.getLineTitleBorder(PackageResources.SearchParamsDefiner.SCOPE_TITLE));
      var1.display(var3);
   }

   @Override
   public void requestFocus() {
   }

   public void setScope(LdapSearchScope var1) {
      this.selector.setSelectedValue(var1);
   }

   public LdapSearchScope getScope() {
      return this.selector.getSelection();
   }
}
