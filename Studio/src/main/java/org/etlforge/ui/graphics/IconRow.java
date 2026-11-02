package org.etlforge.ui.graphics;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.Icon;

public final class IconRow implements Icon {
   private final Icon[] icons;
   private int gap = 2;
   private int height;
   private int width;

   public IconRow(Iterable<Icon> var1) {
      this.icons = convert(var1);
      this.calculateSize();
   }

   private static Icon[] convert(Iterable<Icon> var0) {
      ArrayList var1 = Lists.newArrayList(var0);
      return (Icon[]) var1.toArray(new Icon[var1.size()]);
   }

   public IconRow(Icon... var1) {
      this.icons = (Icon[])var1.clone();
      this.calculateSize();
   }

   @Override
   public int getIconHeight() {
      return this.height;
   }

   @Override
   public int getIconWidth() {
      return this.width;
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      int var5 = var3;

      for (Icon var9 : this.icons) {
         var9.paintIcon(var1, var2, var5, var4);
         var5 += var9.getIconWidth() + this.gap;
      }
   }

   public void setIconGap(int var1) {
      Preconditions.checkArgument(var1 >= 0, "pixels must be >= 0 (was " + var1 + ")");
      this.gap = var1;
      this.calculateSize();
   }

   private void calculateSize() {
      this.height = 0;
      this.width = 0;

      for (Icon var4 : this.icons) {
         int var5 = var4.getIconHeight();
         if (var5 > this.height) {
            this.height = var5;
         }

         this.width = this.width + var4.getIconWidth();
      }

      this.width = this.width + this.gap * (this.icons.length - 1);
   }
}
