package org.etlforge.ui.layout;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.widget.button.KongaButton;

public final class NColumnPanel extends GridBagLayoutPanel implements LayoutContainer {
   private final NColumnPanel.Params[] params;
   private final GridBagConstraints constraint;

   public NColumnPanel(int var1) {
      this(var1, GridBagLayoutPanel.WEST);
   }

   public NColumnPanel(JPanel var1, int var2) {
      this(var1, var2, GridBagLayoutPanel.WEST);
   }

   public NColumnPanel(int var1, GridBagLayoutPanel.Anchor var2) {
      this(null, var1, var2, 10, 10);
   }

   public NColumnPanel(JPanel var1, int var2, GridBagLayoutPanel.Anchor var3) {
      this(var1, var2, var3, 10, 10);
   }

   public NColumnPanel(int var1, GridBagLayoutPanel.Anchor var2, int var3, int var4) {
      this(null, var1, var2, var3, var4);
   }

   public NColumnPanel(JPanel var1, int var2, GridBagLayoutPanel.Anchor var3, int var4, int var5) {
      super(var1);
      this.params = new NColumnPanel.Params[var2];

      for (int var6 = 0; var6 < var2; var6++) {
         this.params[var6] = new NColumnPanel.Params();
         this.params[var6].anchor = var3.getSwingCode();
      }

      this.constraint = new GridBagConstraints();
      this.constraint.gridx = 0;
      this.constraint.gridy = 0;
      this.setHGap(var4);
      this.setVGap(var5);
   }

   public int getColumnCount() {
      return this.params.length;
   }

   public NColumnPanel addRow(Object... var1) {
      Preconditions.checkArgument(var1.length == this.params.length, "Wrong number of items. Expected: " + this.params.length + ", got: " + var1.length);
      if (this.constraint.gridx > 0) {
         this.constraint.gridx = 0;
         this.constraint.gridy++;
      }

      for (Object var5 : var1) {
         this.params[this.constraint.gridx].apply(this.constraint);
         this.addImpl(var5);
         this.constraint.gridx++;
      }

      return this;
   }

   public NColumnPanel emptyColumn() {
      return this.add(null, 1);
   }

   public NColumnPanel add(Component var1) {
      return this.add(var1, 1);
   }

   public NColumnPanel add(UiProvider var1) {
      return this.add(var1, 1);
   }

   public NColumnPanel add(Object var1) {
      return this.add(var1, 1);
   }

   public NColumnPanel add(Object var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException("width must be >= 1");
      }

      if (this.constraint.gridx >= this.params.length) {
         this.constraint.gridx = 0;
         this.constraint.gridy++;
      }

      if (var2 > this.params.length - this.constraint.gridx) {
         throw new IllegalArgumentException("width exceeds number of available columns");
      }

      if (var2 == 1) {
         this.params[this.constraint.gridx].apply(this.constraint);
      } else {
         NColumnPanel.Params[] var3 = new NColumnPanel.Params[var2];
         int var4 = this.constraint.gridx;

         for (int var5 = this.constraint.gridx + var2; var4 < var5; var4++) {
            var3[var4 - this.constraint.gridx] = this.params[var4];
         }

         NColumnPanel.Params.combine(var3).apply(this.constraint);
      }

      this.constraint.gridwidth = var2;
      this.addImpl(var1);
      this.constraint.gridwidth = 1;
      this.constraint.gridx += var2;
      return this;
   }

   public NColumnPanel newRow() {
      this.constraint.gridx = 0;
      this.constraint.gridy++;
      this.constraint.gridwidth = 1;
      return this;
   }

   private void addImpl(Object var1) {
      if (var1 instanceof Component) {
         this.add((Component)var1, this.constraint);
      } else if (var1 instanceof UiProvider) {
         ((UiProvider)var1).decorate(ComponentCanvasFactory.gridBag(this.container(), this.constraint));
      } else if (var1 instanceof String) {
         this.add(new JLabel((String)var1), this.constraint);
      } else if (var1 instanceof Action) {
         this.add(new KongaButton((Action)var1), this.constraint);
      } else if (var1 != null) {
         throw new IllegalArgumentException("I do not know how to add objects of type " + var1.getClass().getName());
      }
   }

   public NColumnPanel withBorder(Border var1) {
      return (NColumnPanel)super.withBorder(var1);
   }

   public NColumnPanel withEmptyBorder(int var1) {
      return (NColumnPanel)super.withEmptyBorder(var1);
   }

   public NColumnPanel withBackground(Color var1) {
      return (NColumnPanel)super.withBackground(var1);
   }

   public NColumnPanel setVGap(int var1) {
      for (NColumnPanel.Params var5 : this.params) {
         var5.insets.bottom = var1;
      }

      return this;
   }

   public NColumnPanel setHGap(int var1) {
      for (int var2 = 0; var2 < this.params.length - 1; var2++) {
         this.params[var2].insets.right = var1;
      }

      this.params[this.params.length - 1].insets.right = 0;
      return this;
   }

   public NColumnPanel setHGaps(int... var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.params[var2].insets.right = var1[var2];
      }

      return this;
   }

   public NColumnPanel setInsets(int var1, int var2, int var3, int var4) {
      return this.setInsets(new Insets(var1, var2, var3, var4));
   }

   public NColumnPanel setInsets(Insets var1) {
      for (NColumnPanel.Params var5 : this.params) {
         var5.insets = var1;
      }

      return this;
   }

   public NColumnPanel setInsets(int var1, int var2, int var3, int var4, int var5) {
      return this.setInsets(var1, new Insets(var2, var3, var4, var5));
   }

   public NColumnPanel setInsets(int var1, Insets var2) {
      this.params[var1].insets = var2 != null ? var2 : new Insets(0, 0, 0, 0);
      return this;
   }

   public NColumnPanel setWeights(int var1, double var2, double var4) {
      NColumnPanel.Params var6 = this.params[var1];
      var6.weightx = var2;
      var6.weighty = var4;
      return this;
   }

   public NColumnPanel setXWeights(double... var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.params[var2].weightx = var1[var2];
      }

      return this;
   }

   public NColumnPanel setYWeights(double... var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.params[var2].weighty = var1[var2];
      }

      return this;
   }

   public NColumnPanel setFill(int var1, GridBagLayoutPanel.Fill var2) {
      this.params[var1].fill = var2.getSwingCode();
      return this;
   }

   public NColumnPanel setFills(GridBagLayoutPanel.Fill... var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.params[var2].fill = var1[var2].getSwingCode();
      }

      return this;
   }

   public NColumnPanel setAnchors(GridBagLayoutPanel.Anchor... var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.params[var2].anchor = var1[var2].getSwingCode();
      }

      return this;
   }

   public NColumnPanel setAnchor(GridBagLayoutPanel.Anchor var1) {
      for (NColumnPanel.Params var5 : this.params) {
         var5.anchor = var1.getSwingCode();
      }

      return this;
   }

   public NColumnPanel setAnchor(int var1, GridBagLayoutPanel.Anchor var2) {
      this.params[var1].anchor = var2.getSwingCode();
      return this;
   }

   public NColumnPanel asFocusCycleRoot() {
      return (NColumnPanel)super.asFocusCycleRoot();
   }

   public NColumnPanel withFocusTraversalPolicy(FocusTraversalPolicy var1) {
      return (NColumnPanel)super.withFocusTraversalPolicy(var1);
   }

   public NColumnPanel setOpaque(boolean var1, boolean var2) {
      return (NColumnPanel)super.setOpaque(var1, var2);
   }

   public NColumnPanel fillWith(Object var1) {
      int var2 = this.getColumnCount();
      double var3 = 1.0 / var2;

      for (int var5 = 0; var5 < var2; var5++) {
         this.setFill(var5, GridBagLayoutPanel.Fill.BOTH);
         this.setWeights(var5, var3, 1.0);
      }

      return this.add(var1, var2);
   }

   private static class Params {
      public Insets insets = new Insets(0, 0, 0, 0);
      public double weightx;
      public double weighty;
      public int anchor;
      public int fill;

      private Params() {
      }

      public void apply(GridBagConstraints var1) {
         var1.anchor = this.anchor;
         var1.insets = this.insets;
         var1.weightx = this.weightx;
         var1.weighty = this.weighty;
         var1.fill = this.fill;
      }

      public static NColumnPanel.Params combine(NColumnPanel.Params... var0) {
         NColumnPanel.Params var1 = new NColumnPanel.Params();
         double var2 = 0.0;
         double var4 = 0.0;
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;

         for (NColumnPanel.Params var12 : var0) {
            if (var12.insets.top > var6) {
               var6 = var12.insets.top;
            }

            if (var12.insets.bottom > var7) {
               var7 = var12.insets.bottom;
            }

            if (var12.weightx > var2) {
               var2 = var12.weightx;
            }

            if (var12.weighty > var4) {
               var4 = var12.weighty;
            }

            if (var12.fill == 1) {
               var8 = var12.fill;
            } else if (var12.fill != 0) {
               if (var8 == 0) {
                  var8 = var12.fill;
               } else if (var8 == 2) {
                  if (var12.fill == 3) {
                     var8 = 1;
                  }
               } else if (var8 == 3 && var12.fill == 2) {
                  var8 = 1;
               }
            }
         }

         var1.weightx = var2;
         var1.weighty = var4;
         var1.fill = var8;
         var1.insets = new Insets(var6, var0[0].insets.left, var7, var0[var0.length - 1].insets.right);
         var1.anchor = var0[0].anchor;
         return var1;
      }
   }
}
