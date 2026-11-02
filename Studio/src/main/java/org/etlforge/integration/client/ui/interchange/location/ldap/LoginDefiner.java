package org.etlforge.integration.client.ui.interchange.location.ldap;

import com.google.common.base.Optional;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelLoginPart;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.LayoutContainer;
import org.etlforge.ui.layout.OneRowPanel;

class LoginDefiner extends DataLocationPanelLoginPart<LdapLocation> implements LdapLocationPanelPart {
   public LoginDefiner(InputPanel var1) {
      super(var1);
   }

   @Override
   public void layout(LayoutContainer var1) {
      JComponent[] var2 = this.getLayoutComponents();
      OneRowPanel var3 = new OneRowPanel();
      var3.setAnchor(OneRowPanel.SOUTHWEST);
      var3.withInsets(0, 0, 0, 5);
      var3.setFillOut();
      var3.add(var2[0]);
      var3.setFillNone();
      var3.withInsets(0, 0, 0, 0);
      var3.add(var2[1]);
      var1.add(var3);
   }

   protected void applyValues(LdapLocation var1, String var2, Optional<Password> var3) {
      var1.setUser(var2);
      var1.setPassword(var3);
   }

   protected String getCurrentLogin(LdapLocation var1) {
      return var1.getLogin();
   }

   protected Optional<Password> getCurrentPassword(LdapLocation var1) {
      return var1.getPassword();
   }
}
