package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.input.InputField;

public class TwoColumnPanel extends GridBagLayoutPanel implements TwoColumnLayout {
   private static final Insets defaultLeftInsets = new Insets(3, 0, 10, 10);
   private static final Insets defaultRightInsets = new Insets(3, 5, 10, 0);
   private final GridBagConstraints leftConstraints = getInitialLeftConstraints();
   private final GridBagConstraints rightConstraints = getInitialRightConstraints();
   private UiCanvas leftDecor;

   public static TwoColumnPanel forFormEntry() {
      return forFormEntry(5, 5);
   }

   public static TwoColumnPanel forFormEntry(int var0, int var1) {
      return new TwoColumnPanel()
         .withAnchor(GridBagLayoutPanel.Anchor.WEST)
         .withLeftFill(GridBagLayoutPanel.Fill.NONE)
         .withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withLeftWeights(0.0, 0.0)
         .withRightWeights(1.0, 0.0)
         .withLeftInsets(0, 0, var1, var0)
         .withRightInsets(0, 0, var1, 0);
   }

   public TwoColumnPanel() {
      this(null);
   }

   public TwoColumnPanel(JPanel var1) {
      super(var1);
   }

   public TwoColumnPanel addRow(InputField var1) {
      this.addRow(var1.getLabel(), var1.getInputComponent());
      return this;
   }

   public TwoColumnPanel addRow(Component var1, Component var2) {
      if (var1 != null) {
         this.add(var1, this.leftConstraints);
      }

      if (var2 != null) {
         this.add(var2, this.rightConstraints);
      }

      this.postRowAdded();
      return this;
   }

   public TwoColumnPanel addLabeledComponent(String var1, Component var2) {
      return this.addRow(new JLabel(var1), var2);
   }

   public TwoColumnPanel addLabeledComponent(String var1, UiProvider var2) {
      return this.addRow(new UiProviderAdapter(new JLabel(var1)), var2);
   }

   public TwoColumnPanel addRow(UiProvider var1, UiProvider var2) {
      if (var1 != null) {
         this.add((UiProvider)var1, this.leftConstraints);
      }

      if (var2 != null) {
         this.add((UiProvider)var2, this.rightConstraints);
      }

      this.postRowAdded();
      return this;
   }

   public TwoColumnPanel addRow(Component var1) {
      this.leftConstraints.gridwidth = 2;
      this.add(var1, this.leftConstraints);
      this.postRowAdded();
      return this;
   }

   public TwoColumnPanel addRow(UiProvider var1) {
      this.leftConstraints.gridwidth = 2;
      var1.decorate(this.getLeftDecor());
      this.postRowAdded();
      return this;
   }

   public TwoColumnPanel withBorder(Border var1) {
      return (TwoColumnPanel)super.withBorder(var1);
   }

   public TwoColumnPanel withEmptyBorder(int var1) {
      return (TwoColumnPanel)super.withEmptyBorder(var1);
   }

   public TwoColumnPanel withBackground(Color var1) {
      return (TwoColumnPanel)super.withBackground(var1);
   }

   public TwoColumnPanel withAnchor(GridBagLayoutPanel.Anchor var1) {
      this.withLeftAnchor(var1);
      this.withRightAnchor(var1);
      return this;
   }

   public TwoColumnPanel withLeftAnchor(GridBagLayoutPanel.Anchor var1) {
      this.leftConstraints.anchor = var1.getSwingCode();
      return this;
   }

   public TwoColumnPanel withRightAnchor(GridBagLayoutPanel.Anchor var1) {
      this.rightConstraints.anchor = var1.getSwingCode();
      return this;
   }

   public TwoColumnPanel withLeftGridHeight(int var1) {
      this.leftConstraints.gridheight = var1;
      return this;
   }

   public TwoColumnPanel withRightGridHeight(int var1) {
      this.rightConstraints.gridheight = var1;
      return this;
   }

   public TwoColumnPanel withLeftFill(GridBagLayoutPanel.Fill var1) {
      this.leftConstraints.fill = var1.getSwingCode();
      return this;
   }

   public TwoColumnPanel withRightFill(GridBagLayoutPanel.Fill var1) {
      this.rightConstraints.fill = var1.getSwingCode();
      return this;
   }

   public TwoColumnPanel withLeftWeights(double var1, double var3) {
      this.leftConstraints.weightx = var1;
      this.leftConstraints.weighty = var3;
      return this;
   }

   public TwoColumnPanel withRightWeights(double var1, double var3) {
      this.rightConstraints.weightx = var1;
      this.rightConstraints.weighty = var3;
      return this;
   }

   public TwoColumnPanel withInsets(int var1, int var2, int var3, int var4) {
      Insets var5 = new Insets(var1, var2, var3, var4);
      this.leftConstraints.insets = var5;
      this.rightConstraints.insets = var5;
      return this;
   }

   public TwoColumnPanel withLeftInsets(Insets var1) {
      this.leftConstraints.insets = var1;
      return this;
   }

   public TwoColumnPanel withLeftInsets(int var1, int var2, int var3, int var4) {
      this.withLeftInsets(new Insets(var1, var2, var3, var4));
      return this;
   }

   public TwoColumnPanel withRightInsets(Insets var1) {
      this.rightConstraints.insets = var1;
      return this;
   }

   public TwoColumnPanel withRightInsets(int var1, int var2, int var3, int var4) {
      this.withRightInsets(new Insets(var1, var2, var3, var4));
      return this;
   }

   public TwoColumnPanel nonOpaque() {
      return (TwoColumnPanel)super.nonOpaque();
   }

   public TwoColumnPanel asFocusCycleRoot() {
      return (TwoColumnPanel)super.asFocusCycleRoot();
   }

   public TwoColumnPanel withFocusTraversalPolicy(FocusTraversalPolicy var1) {
      return (TwoColumnPanel)super.withFocusTraversalPolicy(var1);
   }

   private void postRowAdded() {
      this.leftConstraints.gridy++;
      this.rightConstraints.gridy++;
      this.leftConstraints.gridwidth = 1;
      this.rightConstraints.gridwidth = 1;
      this.leftConstraints.gridheight = 1;
      this.rightConstraints.gridheight = 1;
   }

   private UiCanvas getLeftDecor() {
      if (this.leftDecor == null) {
         this.leftDecor = ComponentCanvasFactory.gridBag(this.container(), this.leftConstraints);
      }

      return this.leftDecor;
   }

   private static GridBagConstraints getInitialLeftConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.anchor = 17;
      var0.insets = defaultLeftInsets;
      var0.gridx = 0;
      var0.gridy = 0;
      return var0;
   }

   public static final GridBagConstraints getInitialRightConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.anchor = 17;
      var0.insets = defaultRightInsets;
      var0.gridx = 1;
      var0.gridy = 0;
      return var0;
   }
}
