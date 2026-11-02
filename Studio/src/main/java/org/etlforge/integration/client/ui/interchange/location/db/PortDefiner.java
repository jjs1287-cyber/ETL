package org.etlforge.integration.client.ui.interchange.location.db;

import javax.swing.JPanel;
import org.etlforge.application.ui.widget.DefaultPortDefiner;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class PortDefiner implements DatabaseLocationPanelPart {
   private final DefaultPortDefiner impl = new DefaultPortDefiner();

   public PortDefiner(InputPanel var1) {
      this.impl.registerAsChangeSource(var1);
   }

   @Override
   public void addToLayout(DatabaseLocationPanelLayout var1) {
      JPanel var2 = InvisiblePanel.of(this.impl);
      var1.add(var2);
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.addInMiddle(this.impl);
   }

   public void applyTo(DatabaseLocation var1) {
      var1.setPort(this.impl.getPort());
   }

   @Override
   public void dispose() {
      this.impl.dispose();
   }

   public void reset(DatabaseLocation var1) {
      this.impl.setPort(var1.getPort());
   }

   public void setEnabled(boolean var1) {
      this.impl.setEnabled(var1);
   }
}
