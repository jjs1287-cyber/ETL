package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.Orientation;

public final class OneRowPanel extends OneLinePanel {
   public OneRowPanel() {
      this(null);
   }

   public OneRowPanel(JPanel var1) {
      this(var1, NORTHWEST, HORIZONTAL);
   }

   public OneRowPanel(GridBagLayoutPanel.Anchor var1, GridBagLayoutPanel.Fill var2) {
      this(null, var1, var2);
   }

   public OneRowPanel(JPanel var1, GridBagLayoutPanel.Anchor var2, GridBagLayoutPanel.Fill var3) {
      super(var1, var2, var3, Orientation.HORIZONTAL);
   }

   public OneRowPanel of(Object... var1) {
      return this.addAll(var1);
   }

   public OneRowPanel addAll(Object... var1) {
      return (OneRowPanel)super.addAll(var1);
   }

   public OneRowPanel addAll(Component... var1) {
      return (OneRowPanel)super.addAll(var1);
   }

   public OneRowPanel add(Object var1) {
      return (OneRowPanel)super.add(var1);
   }

   public OneRowPanel add(GridBagLayoutPanel var1) {
      return (OneRowPanel)super.add(var1);
   }

   public OneRowPanel add(Component var1) {
      return (OneRowPanel)super.add(var1);
   }

   public OneRowPanel add(UiProvider var1) {
      return (OneRowPanel)super.add(var1);
   }

   public OneRowPanel setAnchor(GridBagLayoutPanel.Anchor var1) {
      return (OneRowPanel)super.setAnchor(var1);
   }

   public OneRowPanel setFill(GridBagLayoutPanel.Fill var1) {
      return (OneRowPanel)super.setFill(var1);
   }

   public OneRowPanel setFillNone() {
      return (OneRowPanel)super.setFillNone();
   }

   public OneRowPanel setFillOut() {
      return (OneRowPanel)super.setFillOut();
   }

   public OneRowPanel withBorder(Border var1) {
      return (OneRowPanel)super.withBorder(var1);
   }

   public OneRowPanel withEmptyBorder(int var1) {
      return (OneRowPanel)super.withEmptyBorder(var1);
   }

   public OneRowPanel withBackground(Color var1) {
      return (OneRowPanel)super.withBackground(var1);
   }

   public OneRowPanel withInsets(Insets var1) {
      return (OneRowPanel)super.withInsets(var1);
   }

   public OneRowPanel withInsets(int var1, int var2, int var3, int var4) {
      return (OneRowPanel)super.withInsets(var1, var2, var3, var4);
   }

   public OneRowPanel setWeightX(double var1) {
      return (OneRowPanel)super.setWeightX(var1);
   }

   public OneRowPanel setWeightY(double var1) {
      return (OneRowPanel)super.setWeightY(var1);
   }

   public OneRowPanel asFocusCycleRoot() {
      return (OneRowPanel)super.asFocusCycleRoot();
   }

   public OneRowPanel withFocusTraversalPolicy(FocusTraversalPolicy var1) {
      return (OneRowPanel)super.withFocusTraversalPolicy(var1);
   }
}
