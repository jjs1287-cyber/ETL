package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class SplitButtonsLayout2 implements UserConsoleViewLayout {
   private final BorderLayoutBuilder pageArea = new BorderLayoutBuilder(10, 10);

   public SplitButtonsLayout2(UserConsoleView var1, Action[] var2, Action[] var3, Action[] var4) {
      this.pageArea.north(this.layoutServerActions(var2));
      this.pageArea.center(var1.getUsersAndGroupsViewer());
      this.pageArea.east(this.layoutEastPart(var1, var3, var4));
      this.pageArea.deeplyNonOpaque();
   }

   @Override
   public JComponent getUi() {
      return this.pageArea.container();
   }

   private JComponent layoutServerActions(Action[] var1) {
      Grid var2 = Grid.oneRow(10).addAll(var1);
      return InvisiblePanel.of(var2);
   }

   private JComponent layoutModelAndViewActions(Action[] var1, Action[] var2) {
      Grid var3 = Grid.oneColumn(10).addAll(var1).addAll(var2);
      return InvisiblePanel.of(var3);
   }

   private UiProvider layoutEastPart(UserConsoleView var1, Action[] var2, Action[] var3) {
      BorderLayoutBuilder var4 = new BorderLayoutBuilder(10, 0);
      var4.west(this.layoutModelAndViewActions(var2, var3)).center(var1.getDetailsViewer());
      return var4;
   }
}
