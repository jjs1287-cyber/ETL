package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.Orientation;

public final class OneColumnPanel extends OneLinePanel {
   public OneColumnPanel() {
      this(null);
   }

   public OneColumnPanel(JPanel var1) {
      this(var1, NORTHWEST, HORIZONTAL);
   }

   public OneColumnPanel(GridBagLayoutPanel.Anchor var1, GridBagLayoutPanel.Fill var2) {
      this(null, var1, var2);
   }

   public OneColumnPanel(JPanel var1, GridBagLayoutPanel.Anchor var2, GridBagLayoutPanel.Fill var3) {
      super(var1, var2, var3, Orientation.VERTICAL);
   }

   public OneColumnPanel of(Object... var1) {
      return this.addAll(var1);
   }

   public OneColumnPanel addAll(Object... var1) {
      return (OneColumnPanel)super.addAll(var1);
   }

   public OneColumnPanel addAll(Iterable<?> var1) {
      for (Object var3 : var1) {
         this.add(var3);
      }

      return this;
   }

   public OneColumnPanel addAll(Component... var1) {
      return (OneColumnPanel)super.addAll(var1);
   }

   public OneColumnPanel add(Object var1) {
      return (OneColumnPanel)super.add(var1);
   }

   public OneColumnPanel add(GridBagLayoutPanel var1) {
      return (OneColumnPanel)super.add(var1);
   }

   public OneColumnPanel add(Component var1) {
      return (OneColumnPanel)super.add(var1);
   }

   public OneColumnPanel add(UiProvider var1) {
      return (OneColumnPanel)super.add(var1);
   }

   public OneColumnPanel addAll(UiProvider... var1) {
      for (UiProvider var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public OneColumnPanel withInsets(int var1, int var2, int var3, int var4) {
      return (OneColumnPanel)super.withInsets(var1, var2, var3, var4);
   }

   public OneColumnPanel withInsets(Insets var1) {
      return (OneColumnPanel)super.withInsets(var1);
   }

   public OneColumnPanel withBorder(Border var1) {
      return (OneColumnPanel)super.withBorder(var1);
   }

   public OneColumnPanel withEmptyBorder(int var1) {
      return (OneColumnPanel)super.withEmptyBorder(var1);
   }

   public OneColumnPanel withBackground(Color var1) {
      return (OneColumnPanel)super.withBackground(var1);
   }

   public OneColumnPanel nonOpaque() {
      return (OneColumnPanel)super.nonOpaque();
   }

   public OneColumnPanel deeplyNonOpaque() {
      return (OneColumnPanel)super.deeplyNonOpaque();
   }

   public OneColumnPanel fillOut(Object var1) {
      this.setFillOut();
      this.add(var1);
      return this;
   }

   public OneColumnPanel noFill(Object var1) {
      this.setFillNone();
      this.add(var1);
      return this;
   }

   public OneColumnPanel stretchHorizontally() {
      this.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      this.setWeightX(1.0);
      return this;
   }

   public OneColumnPanel setAnchor(GridBagLayoutPanel.Anchor var1) {
      return (OneColumnPanel)super.setAnchor(var1);
   }

   public OneColumnPanel setFill(GridBagLayoutPanel.Fill var1) {
      return (OneColumnPanel)super.setFill(var1);
   }

   public OneColumnPanel setFillNone() {
      return (OneColumnPanel)super.setFillNone();
   }

   public OneColumnPanel setFillOut() {
      return (OneColumnPanel)super.setFillOut();
   }

   public OneColumnPanel asFocusCycleRoot() {
      return (OneColumnPanel)super.asFocusCycleRoot();
   }

   public OneColumnPanel withFocusTraversalPolicy(FocusTraversalPolicy var1) {
      return (OneColumnPanel)super.withFocusTraversalPolicy(var1);
   }

   public OneColumnPanel setWeightX(double var1) {
      return (OneColumnPanel)super.setWeightX(var1);
   }

   public OneColumnPanel setWeightY(double var1) {
      return (OneColumnPanel)super.setWeightY(var1);
   }
}
