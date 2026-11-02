package com.etlforge.integration.client.ui.script.fcn.arg;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class QuestionLayout implements ArgumentFieldLayout {
   private final TwoColumnPanel grid = new TwoColumnPanel();
   private final Border fieldInsets;
   private final Border descriptionInsets;

   public QuestionLayout() {
      this.grid.withInsets(0, 0, 0, 0);
      this.grid.withAnchor(GridBagLayoutPanel.Anchor.WEST);
      this.grid.withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      this.grid.withLeftWeights(1.0, 0.0);
      this.fieldInsets = Empty.border(5, 20, 20, 0);
      this.descriptionInsets = Empty.border(5, 0, 20, 0);
   }

   @Override
   public void addField(JLabel var1, JComponent var2, JComponent var3) {
      this.grid.addRow(var1);
      JComponent var4 = this.wrapField(var2);
      JComponent var5 = this.wrapDescription(var3);
      this.grid.addRow(var4, var5);
   }

   private JComponent wrapField(JComponent var1) {
      JComponent var2 = new BorderLayoutBuilder().north(var1).container();
      var2.setBorder(this.fieldInsets);
      return var2;
   }

   private JComponent wrapDescription(JComponent var1) {
      JPanel var2 = InvisiblePanel.of(var1);
      var2.setBorder(this.descriptionInsets);
      return var2;
   }

   @Override
   public void addButton(JButton var1) {
      JPanel var2 = InvisiblePanel.of(var1);
      this.grid.addRow(var2, null);
   }

   @Override
   public JComponent getContainer() {
      return this.grid.container();
   }
}
