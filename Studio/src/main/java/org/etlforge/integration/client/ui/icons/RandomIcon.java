package org.etlforge.integration.client.ui.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.Icon;

public final class RandomIcon implements Icon {
   private final int size;
   private final int blocks;
   private final int blockSize;
   private final Color[][] colors;

   public static Icon small() {
      return new RandomIcon(16);
   }

   public static Icon medium() {
      return new RandomIcon(24);
   }

   public static final Icon large() {
      return new RandomIcon(32);
   }

   private RandomIcon(int var1) {
      this.size = var1;
      if (var1 == 16) {
         this.blocks = 3;
         this.blockSize = 4;
      } else if (var1 == 24) {
         this.blocks = 4;
         this.blockSize = 5;
      } else {
         if (var1 != 32) {
            throw new IllegalArgumentException();
         }

         this.blocks = 4;
         this.blockSize = 7;
      }

      Random var2 = new Random();
      this.colors = new Color[this.blocks][this.blocks];

      for (int var3 = 0; var3 < this.blocks; var3++) {
         for (int var4 = 0; var4 < this.blocks; var4++) {
            this.colors[var3][var4] = new Color(100 + var2.nextInt(156), var2.nextInt(256), var2.nextInt(256));
         }
      }
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      Color var5 = var2.getColor();

      for (int var6 = 0; var6 < this.blocks; var6++) {
         for (int var7 = 0; var7 < this.blocks; var7++) {
            Color var8 = this.colors[var6][var7];
            var2.setColor(var8);
            var2.fillRect(var3 + 2 + var7 * this.blockSize, var4 + 2 + var6 * this.blockSize, this.blockSize, this.blockSize);
         }
      }

      var2.setColor(var5);
   }

   @Override
   public int getIconWidth() {
      return this.size;
   }

   @Override
   public int getIconHeight() {
      return this.size;
   }
}
