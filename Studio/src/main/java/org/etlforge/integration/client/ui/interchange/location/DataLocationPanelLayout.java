package org.etlforge.integration.client.ui.interchange.location;

import java.awt.Container;
import javax.swing.JComponent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class DataLocationPanelLayout implements UiProvider {
   private final NColumnPanel panel = new NColumnPanel(3);

   public DataLocationPanelLayout() {
      this.panel.setHGap(5);
      this.panel.setAnchors(GridBagLayoutPanel.Anchor.EAST, GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.WEST);
      this.panel.setWeights(0, 0.0, 0.0).setWeights(1, 1.0, 0.0).setWeights(2, 0.0, 0.0);
      this.panel.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.HORIZONTAL);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.panel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocus();
   }

   public void add(InputField var1) {
      this.add(var1, true);
   }

   public void addInMiddle(UiProvider var1) {
      this.add(null, InvisiblePanel.of(var1), null);
   }

   public void add(InputField var1, boolean var2) {
      this.add(var1.getLabel(), var1.getInputComponent(), var2);
   }

   public void add(JComponent var1, JComponent var2, JComponent var3) {
      this.panel.newRow().addRow(var1, var2, var3);
   }

   public void add(JComponent var1, JComponent var2, boolean var3) {
      if (var3) {
         this.panel.newRow().add(var1).add(var2, 2);
      } else {
         this.add(var1, var2, null);
      }
   }

   public void add(JComponent var1, boolean var2) {
      int var3 = var2 ? 3 : 1;
      this.panel.newRow().add(var1, var3);
   }

   public void addToParent(InputPanel var1) {
      var1.wrap(this.panel);
   }

   public void addToParent(Container var1, Object var2) {
      this.panel.addToParent(var1, var2);
   }
}
