package org.etlforge.ui.util.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import org.etlforge.ui.Empty;

public final class BorderBuilder implements Border, Serializable {
   private CompoundBorder compound = new CompoundBorder();

   public BorderBuilder() {
   }

   public BorderBuilder(Border... var1) {
      this();

      for (Border var5 : var1) {
         if (var5 != null) {
            this.add(var5);
         }
      }
   }

   public BorderBuilder bevel(int var1) {
      return this.add(BorderFactory.createBevelBorder(var1));
   }

   public BorderBuilder bevel(int var1, Color var2, Color var3) {
      return this.add(BorderFactory.createBevelBorder(var1, var2, var3));
   }

   public BorderBuilder etched() {
      return this.add(BorderFactory.createEtchedBorder());
   }

   public BorderBuilder etched(Color var1, Color var2) {
      return this.add(BorderFactory.createEtchedBorder(var1, var2));
   }

   public BorderBuilder etched(int var1) {
      return this.add(BorderFactory.createEtchedBorder(var1));
   }

   public BorderBuilder etched(int var1, Color var2, Color var3) {
      return this.add(BorderFactory.createEtchedBorder(var1, var2, var3));
   }

   public BorderBuilder empty(int var1) {
      return this.empty(var1, var1, var1, var1);
   }

   public BorderBuilder empty(int var1, int var2, int var3, int var4) {
      return this.add(Empty.border(var1, var2, var3, var4));
   }

   public BorderBuilder empty(Insets var1) {
      return this.empty(var1.top, var1.left, var1.bottom, var1.right);
   }

   public BorderBuilder line() {
      return this.line(Color.BLACK);
   }

   public BorderBuilder line(Color var1) {
      return this.add(BorderFactory.createLineBorder(var1));
   }

   public BorderBuilder line(Color var1, int var2) {
      return this.add(BorderFactory.createLineBorder(var1, var2));
   }

   public BorderBuilder titled(String var1) {
      return this.add(BorderFactory.createTitledBorder(var1));
   }

   public BorderBuilder titled(Border var1, String var2) {
      return this.add(BorderFactory.createTitledBorder(var1, var2));
   }

   public BorderBuilder rounded(int var1, int var2) {
      return this.add(new RoundedBorder(var1, var2));
   }

   public BorderBuilder rounded(int var1, int var2, Color var3) {
      return this.add(new RoundedBorder(var1, var2, var3));
   }

   public BorderBuilder rounded(int var1, int var2, int var3, Color var4) {
      return this.add(new RoundedBorder(var1, var2, var3, var4));
   }

   public BorderBuilder add(Border var1) {
      if (this.compound.getOutsideBorder() == null) {
         this.compound = new CompoundBorder(var1, null);
      } else if (this.compound.getInsideBorder() == null) {
         this.compound = new CompoundBorder(this.compound.getOutsideBorder(), var1);
      } else {
         this.compound = new CompoundBorder(this.compound, var1);
      }

      return this;
   }

   @Override
   public Insets getBorderInsets(Component var1) {
      return this.compound.getBorderInsets(var1);
   }

   @Override
   public boolean isBorderOpaque() {
      return this.compound.isBorderOpaque();
   }

   @Override
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.compound.paintBorder(var1, var2, var3, var4, var5, var6);
   }
}
