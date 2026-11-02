package com.etlforge.integration.client.ui.script.fcn.arg;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;

final class LabelLayout implements ArgumentFieldLayout {
   private final NColumnPanel grid = new NColumnPanel(3, GridBagLayoutPanel.Anchor.WEST);

   public LabelLayout() {
      this.grid.setWeights(1, 1.0, 0.0);
      this.grid.setFill(1, GridBagLayoutPanel.Fill.HORIZONTAL);
   }

   @Override
   public void addField(JLabel var1, JComponent var2, JComponent var3) {
      addColonIfNecessary(var1);
      this.grid.addRow(var1, var2, var3);
   }

   private static void addColonIfNecessary(JLabel var0) {
      String var1 = var0.getText();
      if (!var1.endsWith(":")) {
         var1 = var1 + ":";
      }

      var0.setText(var1);
   }

   @Override
   public void addButton(JButton var1) {
      this.grid.addRow(var1, null, null);
   }

   @Override
   public JComponent getContainer() {
      return this.grid.container();
   }
}
