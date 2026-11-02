package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

class SplitButtonsLayout implements UserConsoleViewLayout {
   private final JComponent pageArea;

   public SplitButtonsLayout(UserConsoleView var1, Action[] var2, Action[] var3) {
      this.pageArea = Vertical.borderLayout().of(this.topControls(var2), this.layoutView(var1, var3)).withGap(10).deeplyNonOpaque().container();
   }

   private JComponent topControls(Action[] var1) {
      Grid var2 = Grid.oneRow(10).addAll(var1);
      return InvisiblePanel.of(var2.container());
   }

   private TwoColumnPanel layoutView(UserConsoleView var1, Action[] var2) {
      return new TwoColumnPanel()
         .withLeftInsets(0, 0, 0, 10)
         .withRightInsets(0, 0, 0, 0)
         .withLeftWeights(0.5, 0.0)
         .withRightWeights(0.5, 1.0)
         .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withRightFill(GridBagLayoutPanel.Fill.BOTH)
         .withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST)
         .addRow(this.layoutLeftPartOfViewerArea(var1.getUsersAndGroupsViewer(), var2), var1.getDetailsViewer());
   }

   private UiProvider layoutLeftPartOfViewerArea(UiProvider var1, Action[] var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10);
      var3.center(var1);
      Grid var4 = new Grid(0, 2, 10, 10);

      for (Action var8 : var2) {
         KongaButton var9 = new KongaButton(var8).setUseSmallActionIcon(true);
         var4.add(var9);
      }

      var3.south(InvisiblePanel.of(var4));
      return var3;
   }

   @Override
   public JComponent getUi() {
      return this.pageArea;
   }
}
