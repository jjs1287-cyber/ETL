package org.etlforge.application.ui.window.editor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

class GenericEditorTitlePanelLayout implements LayoutManager {
   static final String LABELS = "labels";
   static final String ADDITIONAL = "additional";
   static final String TOOLBAR = "toolbar";
   private static final int HORIZONTAL_GAP = 30;
   private static final int VERTICAL_PAD = 2;
   private Component labels;
   private Component additional;
   private Component toolbar;

   @Override
   public void addLayoutComponent(String var1, Component var2) {
      if ("labels".equals(var1)) {
         this.labels = var2;
      } else if ("additional".equals(var1)) {
         this.additional = var2;
      } else {
         if (!"toolbar".equals(var1)) {
            throw new IllegalArgumentException(
               "Invalid name: " + var1 + ". Must be one of \"" + "labels" + "\", \"" + "additional" + "\", or \"" + "toolbar" + "\"."
            );
         }

         this.toolbar = var2;
      }
   }

   @Override
   public void layoutContainer(Container var1) {
      synchronized (var1.getTreeLock()) {
         Insets var3 = var1.getInsets();
         int var4 = var1.getWidth() - var3.left - var3.right;
         int var5 = var1.getHeight() - var3.top - var3.bottom;
         int var6 = var3.left;
         int var7 = var3.top;
         int var8 = var4;
         if (this.toolbar != null) {
            var8 = layoutGuaranteedSizeComponent(this.toolbar, var8, var7, var5);
         }

         if (this.additional != null) {
            if (this.toolbar != null) {
               var8 -= 30;
            }

            var8 = layoutGuaranteedSizeComponent(this.additional, var8, var7, var5);
         }

         if (this.labels != null) {
            if (this.toolbar != null || this.additional != null) {
               var8 -= 30;
            }

            int var9 = this.labels.getPreferredSize().height;
            this.labels.setBounds(var6, var3.top + (var5 - var9) / 2, var8, var9);
         }
      }
   }

   private static int layoutGuaranteedSizeComponent(Component var0, int var1, int var2, int var3) {
      Dimension var4 = var0.getPreferredSize();
      var1 -= var4.width;
      var0.setBounds(var1, var2 + (var3 - var4.height) / 2, var4.width, var4.height);
      return var1;
   }

   @Override
   public Dimension minimumLayoutSize(Container var1) {
      synchronized (var1.getTreeLock()) {
         return getMinimumSize(this.labels, this.additional, this.toolbar);
      }
   }

   @Override
   public Dimension preferredLayoutSize(Container var1) {
      synchronized (var1.getTreeLock()) {
         return getPreferredSize(var1, this.labels, this.additional, this.toolbar);
      }
   }

   @Override
   public void removeLayoutComponent(Component var1) {
      if (var1 == this.labels) {
         this.labels = null;
      } else if (var1 == this.additional) {
         this.additional = null;
      } else if (var1 == this.toolbar) {
         this.toolbar = null;
      }
   }

   private static Dimension getMinimumSize(Component... var0) {
      int var1 = 0;
      int var2 = 0;

      for (Component var6 : var0) {
         if (var6 != null) {
            Dimension var7 = var6.getMinimumSize();
            var1 += var7.width;
            var2 = Math.max(var2, var7.height);
         }
      }

      return new Dimension(var1, var2 + 4);
   }

   private static Dimension getPreferredSize(Container var0, Component... var1) {
      int var2 = 0;
      int var3 = 0;

      for (Component var7 : var1) {
         if (var7 != null) {
            Dimension var8 = var7.getPreferredSize();
            var2 += var8.width;
            var3 = Math.max(var3, var8.height);
         }
      }

      return new Dimension(var2, var3 + 4);
   }
}
