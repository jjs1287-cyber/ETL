package org.etlforge.application.ui.wizard;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

public final class NumberedSectionsLayout implements UiProvider {
   private final TwoColumnPanel grid = new TwoColumnPanel();
   private int number;
   private double[] xWeights;
   private double[] yWeights;
   private GridBagLayoutPanel.Anchor anchor;
   private GridBagLayoutPanel.Fill[] fills;
   private JLabel lastLabel;
   private JComponent lastSection;

   public NumberedSectionsLayout() {
      this.grid.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST).withLeftFill(GridBagLayoutPanel.Fill.NONE).withRightFill(GridBagLayoutPanel.Fill.BOTH);
      this.grid.withLeftWeights(0.0, 0.0).withRightWeights(1.0, 0.5);
      this.setVerticalGap(30);
   }

   public void setVerticalGap(int var1) {
      this.grid.withLeftInsets(0, 0, var1, 10);
      this.grid.withRightInsets(0, 0, var1, 0);
   }

   public NumberedSectionsLayout addSection(String var1, JComponent var2) {
      return this.addSection(var1, new UiProviderAdapter(var2));
   }

   public NumberedSectionsLayout addSection(String var1, UiProvider var2) {
      if (this.xWeights != null && this.yWeights != null) {
         this.grid.withRightWeights(this.xWeights[this.number], this.yWeights[this.number]);
      }

      if (this.anchor != null) {
         this.grid.withAnchor(this.anchor);
      }

      if (this.fills != null) {
         this.grid.withRightFill(this.fills[this.number]);
      }

      this.number++;
      this.addRow(var2, this.number, var1);
      return this;
   }

   private void addRow(UiProvider var1, int var2, String var3) {
      this.lastLabel = WizardStyleSheet.number(var2);
      this.lastSection = this.layoutSection(var1, var3);
      this.grid.addRow(this.lastLabel, this.lastSection);
   }

   private JComponent layoutSection(UiProvider var1, String var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
      var3.north(WizardStyleSheet.caption(var2)).west(new JLabel("   ")).center(var1);
      var3.withBorder(Empty.border(15, 0, 0, 0)).nonOpaque();
      return var3.container();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.grid.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public NumberedSectionsLayout setWeights(double[] var1, double[] var2) {
      this.xWeights = (double[])var1.clone();
      this.yWeights = (double[])var2.clone();
      return this;
   }

   public NumberedSectionsLayout setAnchor(GridBagLayoutPanel.Anchor var1) {
      this.anchor = var1;
      return this;
   }

   public NumberedSectionsLayout setFills(GridBagLayoutPanel.Fill[] var1) {
      this.fills = (GridBagLayoutPanel.Fill[])var1.clone();
      return this;
   }

   public NumberedSectionsLayout withBackground(Color var1) {
      this.grid.withBackground(var1);
      return this;
   }

   public NumberedSectionsLayout withBorder(Border var1) {
      this.grid.withBorder(var1);
      return this;
   }

   public void setLastSectionVisible(boolean var1) {
      this.lastLabel.setVisible(var1);
      this.lastSection.setVisible(var1);
   }
}
