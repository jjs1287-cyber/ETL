package org.etlforge.integration.client.ui.interchange.location.db;

import java.awt.Component;
import java.awt.Container;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

final class DatabaseLocationPanelLayout {
   private final TwoColumnPanel panel = new TwoColumnPanel().withRightFill(TwoColumnPanel.HORIZONTAL).withAnchor(TwoColumnPanel.SOUTHWEST);
   private Component last;

   void addRow(Component var1) {
      this.newRow();
      this.panel.addRow(var1);
   }

   private void newRow() {
      if (this.last != null) {
         this.panel.addRow(this.last, (Component)null);
      }

      this.last = null;
   }

   void add(Component var1) {
      if (this.last == null) {
         this.last = var1;
      } else {
         this.panel.addRow(this.last, var1);
         this.last = null;
      }
   }

   void addToParent(InputPanel var1) {
      if (this.last != null) {
         this.panel.addRow(this.last, (Component)null);
      }

      var1.wrap(this.panel);
   }

   void addToParent(Container var1, Object var2) {
      if (this.last != null) {
         this.panel.addRow(this.last, (Component)null);
      }

      this.panel.addToParent(var1, var2);
   }
}
