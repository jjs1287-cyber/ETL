package org.etlforge.integration.client.ui.interchange.location.http;

import com.google.common.base.Optional;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelLoginPart;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

final class HttpLoginDefiner extends DataLocationPanelLoginPart<HttpLocation> implements HttpLocationPanelPart {
   HttpLoginDefiner(InputPanel var1) {
      super(var1);
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      JComponent[] var2 = this.getLayoutComponents();
      var1.addRow(var2[0], var2[1]);
   }

   protected void applyValues(HttpLocation var1, String var2, Optional<Password> var3) {
      var1.setLogin(var2);
      var1.setPassword(var3);
   }

   protected String getCurrentLogin(HttpLocation var1) {
      return var1.getLogin();
   }

   protected Optional<Password> getCurrentPassword(HttpLocation var1) {
      return var1.getPassword();
   }
}
