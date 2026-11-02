package org.etlforge.ui.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Collection;
import javax.swing.JPanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.util.Orientation;

public abstract class OneLinePanel extends GridBagLayoutPanel implements LayoutContainer, UiCanvas {
   private GridBagConstraints constraints;
   private UiCanvas canvas;

   protected OneLinePanel(JPanel var1, GridBagLayoutPanel.Anchor var2, GridBagLayoutPanel.Fill var3, Orientation var4) {
      super(var1);
      this.constraints = getInitialConstraints(var4);
      this.setAnchor(var2);
      this.setFill(var3);
   }

   public UiCanvas asDecor() {
      if (this.canvas == null) {
         this.canvas = ComponentCanvasFactory.gridBag(this.container(), this.constraints);
      }

      return this.canvas;
   }

   public OneLinePanel setAnchor(GridBagLayoutPanel.Anchor var1) {
      this.constraints.anchor = var1.getSwingCode();
      return this;
   }

   public OneLinePanel withInsets(int var1, int var2, int var3, int var4) {
      this.withInsets(new Insets(var1, var2, var3, var4));
      return this;
   }

   public OneLinePanel withInsets(Insets var1) {
      this.constraints.insets = var1;
      return this;
   }

   public OneLinePanel setFillOut() {
      this.setWeightX(1.0);
      this.setWeightY(1.0);
      this.setFill(BOTH);
      return this;
   }

   public OneLinePanel setFillNone() {
      this.setWeightX(0.0);
      this.setWeightY(0.0);
      this.setFill(NONE);
      return this;
   }

   public OneLinePanel setWeightX(double var1) {
      this.constraints.weightx = var1;
      return this;
   }

   public OneLinePanel setWeightY(double var1) {
      this.constraints.weighty = var1;
      return this;
   }

   protected OneLinePanel setGridX(int var1) {
      this.constraints.gridx = var1;
      return this;
   }

   protected OneLinePanel setGridY(int var1) {
      this.constraints.gridy = var1;
      return this;
   }

   public OneLinePanel setFill(GridBagLayoutPanel.Fill var1) {
      this.constraints.fill = var1.getSwingCode();
      return this;
   }

   public Object getLayoutState() {
      return this.constraints.clone();
   }

   public void setLayoutState(Object var1) {
      if (var1 instanceof GridBagConstraints) {
         this.constraints = (GridBagConstraints)((GridBagConstraints)var1).clone();
      }
   }

   public OneLinePanel add(Component var1) {
      this.add(var1, this.constraints);
      return this;
   }

   public OneLinePanel add(UiProvider var1) {
      var1.decorate(this.asDecor());
      return this;
   }

   public OneLinePanel addAll(Object... var1) {
      for (Object var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public OneLinePanel add(Object var1) {
      if (var1 instanceof UiProvider) {
         this.add((UiProvider)var1);
      } else if (var1 != null && var1.getClass().isArray() && Object.class.isAssignableFrom(var1.getClass().getComponentType())) {
         System.err.println("OneLinePanel.add(Object) called with array as argument:");

         for (StackTraceElement var5 : Thread.currentThread().getStackTrace()) {
            System.err.println(var5.toString());
         }

         this.addAll((Object[])var1);
      } else if (var1 instanceof Collection) {
         for (Object var4 : (Collection)var1) {
            super.add(var4, this.constraints);
         }
      } else {
         super.add(var1, this.constraints);
      }

      return this;
   }

   public OneLinePanel add(GridBagLayoutPanel var1) {
      super.add(var1, this.constraints);
      return this;
   }

   @Override
   public void display(Component var1) {
      this.add(var1);
   }

   private static GridBagConstraints getInitialConstraints(Orientation var0) {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.anchor = 18;
      if (var0 == Orientation.HORIZONTAL) {
         var1.gridy = 0;
      } else {
         var1.gridx = 0;
      }

      var1.fill = 2;
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      return var1;
   }
}
