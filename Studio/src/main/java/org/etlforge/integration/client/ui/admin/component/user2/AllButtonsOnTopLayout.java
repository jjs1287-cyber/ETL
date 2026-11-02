package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class AllButtonsOnTopLayout implements UserConsoleViewLayout {
   private final JComponent pageArea;

   public AllButtonsOnTopLayout(UserConsoleView var1, Action[] var2, Action[] var3) {
      this.pageArea = Vertical.borderLayout().of(this.topControls(var2, var3), this.layoutView(var1)).withGap(10).deeplyNonOpaque().container();
   }

   private JComponent topControls(Action[] var1, Action[] var2) {
      Action[] var3 = var1;
      Action[] var4 = var2;
      int var5 = Math.max(var3.length, var4.length);
      Grid var6 = new Grid(2, var5, 10, 10);
      this.layoutButtons(var6, var5, var3);
      this.layoutButtons(var6, var5, var4);
      return InvisiblePanel.of(var6);
   }

   private TwoColumnPanel layoutView(UserConsoleView var1) {
      return new TwoColumnPanel()
         .withLeftInsets(0, 0, 0, 10)
         .withRightInsets(0, 0, 0, 0)
         .withLeftWeights(0.5, 0.0)
         .withRightWeights(0.5, 1.0)
         .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withRightFill(GridBagLayoutPanel.Fill.BOTH)
         .withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
         .addRow(var1.getUsersAndGroupsViewer(), var1.getDetailsViewer());
   }

   private void layoutButtons(Grid var1, int var2, Action... var3) {
      for (Action var7 : var3) {
         var1.add(createButton(var7));
      }

      for (int var8 = var3.length; var8 < var2; var8++) {
         var1.add("");
      }
   }

   private static KongaButton createButton(Action var0) {
      return new KongaButton(var0).setUseSmallActionIcon(true);
   }

   @Override
   public JComponent getUi() {
      return this.pageArea;
   }
}
