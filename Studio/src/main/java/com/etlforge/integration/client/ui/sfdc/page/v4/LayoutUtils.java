package com.etlforge.integration.client.ui.sfdc.page.v4;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class LayoutUtils {
   public static JComponent layoutActions(Action... var0) {
      Grid var1 = Grid.oneRow(10);
      var1.setUseMediumSizedIconsForActions(true);
      var1.addAll(var0).nonOpaque();
      JPanel var2 = InvisiblePanel.of(var1);
      var2.setBorder(StyleSheet.getDefaultTitleBorder("Actions"));
      return var2;
   }

   private LayoutUtils() {
   }
}
