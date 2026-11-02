package org.etlforge.integration.client.ui.interchange.location.db;

import com.google.common.base.Optional;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelLoginPart;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DbLoginDefiner extends DataLocationPanelLoginPart<DatabaseLocation> implements DatabaseLocationPanelPart {
   public DbLoginDefiner(InputPanel var1) {
      super(var1);
   }

   protected void applyValues(DatabaseLocation var1, String var2, Optional<Password> var3) {
      var1.setLogin(var2);
      var1.setPassword(var3);
   }

   protected String getCurrentLogin(DatabaseLocation var1) {
      return var1.getLogin();
   }

   protected Optional<Password> getCurrentPassword(DatabaseLocation var1) {
      return var1.getPassword();
   }

   @Override
   public void addToLayout(DatabaseLocationPanelLayout var1) {
      JComponent[] var2 = this.getLayoutComponents();
      OneRowPanel var3 = new OneRowPanel();
      var3.setAnchor(OneRowPanel.SOUTHEAST);
      var3.add(var2[0]);
      var3.add(Box.createHorizontalStrut(5));
      var3.add(var2[1]);
      JPanel var4 = InvisiblePanel.newPanel();
      var3.addToParent(var4, null);
      var1.add(var4);
   }
}
