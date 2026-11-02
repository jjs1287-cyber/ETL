package org.etlforge.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.Border;

public final class Empty {
   private static final Icon EMPTY_INFINITESIMAL_ICON = new Empty.EmptyIcon(0, 0);

   public static Border border() {
      return BorderFactory.createEmptyBorder();
   }

   public static Border border(int var0) {
      return border(var0, var0, var0, var0);
   }

   public static Border border(Insets var0) {
      return border(var0.top, var0.left, var0.bottom, var0.right);
   }

   public static Border border(int var0, int var1, int var2, int var3) {
      return BorderFactory.createEmptyBorder(var0, var1, var2, var3);
   }

   public static Icon icon(Dimension var0) {
      return icon(var0.width, var0.height);
   }

   public static Icon icon(int var0, int var1) {
      return var0 <= 0 && var1 <= 0 ? EMPTY_INFINITESIMAL_ICON : new Empty.EmptyIcon(var0, var1);
   }

   public static Insets insets() {
      return new Insets(0, 0, 0, 0);
   }

   public static Icon iconOfSameSize(Icon var0) {
      return icon(var0.getIconWidth(), var0.getIconHeight());
   }

   private Empty() {
   }

   private static class EmptyIcon implements Icon {
      private final int width;
      private final int height;

      public EmptyIcon(int var1, int var2) {
         this.width = var1;
         this.height = var2;
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
      }
   }
}
