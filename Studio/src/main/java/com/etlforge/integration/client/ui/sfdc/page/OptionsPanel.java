package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

public final class OptionsPanel {
   private final ImmutableList<OptionsPanelSection> sections;

   public OptionsPanel(List<OptionsPanelSection> var1) {
      ArgCheck.checkNotEmpty(var1);
      this.sections = ImmutableList.copyOf(var1);
   }

   public void apply() {
      for (OptionsPanelSection var2 : this.sections) {
         var2.apply();
      }
   }

   public JComponent getUi() {
      OneColumnPanel var1 = new OneColumnPanel().setWeightX(1.0);
      byte var2 = 8;
      var1.withInsets(var2, 0, var2, 0);

      for (OptionsPanelSection var4 : this.sections) {
         TwoColumnPanel var5 = this.createSectionsGrid(var4.getTitle());
         var4.layoutIn(var5);
         var1.add(var5);
      }

      return var1.nonOpaque().container();
   }

   private TwoColumnPanel createSectionsGrid(String var1) {
      byte var2 = 8;
      return new TwoColumnPanel()
         .nonOpaque()
         .withLeftFill(GridBagLayoutPanel.Fill.NONE)
         .withLeftWeights(0.0, 0.0)
         .withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withRightWeights(1.0, 0.0)
         .withLeftInsets(0, 0, var2, 8)
         .withRightInsets(0, 0, var2, 0)
         .withBorder(StyleSheet.getDefaultTitleBorder(var1));
   }

   public void requestFocus() {
      this.sections.get(0).requestFocus();
   }
}
