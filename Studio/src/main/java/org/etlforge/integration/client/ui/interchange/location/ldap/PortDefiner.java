package org.etlforge.integration.client.ui.interchange.location.ldap;

import java.awt.Component;
import org.etlforge.application.ui.widget.DefaultPortDefiner;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.LayoutContainer;

class PortDefiner implements LdapLocationPanelPart {
   private final DefaultPortDefiner impl = new DefaultPortDefiner();

   public PortDefiner(InputPanel var1) {
      this.impl.registerAsChangeSource(var1);
   }

   @Override
   public void layout(final LayoutContainer var1) {
      this.impl.decorate(new UiCanvas() {
         @Override
         public void display(Component var1x) {
            var1.add(var1x);
         }
      });
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.addInMiddle(this.impl);
   }

   public void applyTo(LdapLocation var1) {
      var1.setPort(this.impl.getPort());
   }

   @Override
   public void dispose() {
      this.impl.dispose();
   }

   public void reset(LdapLocation var1) {
      this.impl.setPort(var1.getPort());
   }
}
