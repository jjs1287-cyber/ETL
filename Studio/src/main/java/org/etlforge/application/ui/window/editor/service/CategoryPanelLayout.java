package org.etlforge.application.ui.window.editor.service;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.UIManager;
import org.etlforge.application.ui.look.SupportedLookAndFeel;

final class CategoryPanelLayout implements LayoutManager {
   private final int hGap;
   private final int vGap;

   public CategoryPanelLayout() {
      SupportedLookAndFeel var1 = SupportedLookAndFeel.forClassName(UIManager.getLookAndFeel().getClass().getName());
      if (var1 == null) {
         var1 = SupportedLookAndFeel.CROSS_PLATFORM;
      }

      switch (var1) {
         case NIMBUS:
            this.hGap = 1;
            this.vGap = 1;
            break;
         case SYSTEM:
            this.hGap = 3;
            this.vGap = 3;
            break;
         default:
            this.hGap = 5;
            this.vGap = 5;
      }
   }

   @Override
   public void addLayoutComponent(String var1, Component var2) {
   }

   @Override
   public void removeLayoutComponent(Component var1) {
   }

   @Override
   public Dimension preferredLayoutSize(Container var1) {
      return this.layoutSize(var1);
   }

   @Override
   public Dimension minimumLayoutSize(Container var1) {
      return this.layoutSize(var1);
   }

   @Override
   public void layoutContainer(Container var1) {
      int var2 = var1.getComponentCount();
      if (var2 != 0) {
         Insets var3 = var1.getInsets();
         Component var4 = var1.getComponent(var2 - 1);
         Dimension var5 = var4.getPreferredSize();
         int var6 = var1.getWidth() - var3.right - var5.width;
         int var7 = var3.top + this.vGap;
         var4.setBounds(var6, var7, var5.width, var5.height);
         int var8 = var3.left;
         int var9 = 0;
         int var10 = 0;
         int var11 = var6 - this.hGap;

         for (int var12 = 0; var12 < var2 - 1; var12++) {
            Component var13 = var1.getComponent(var12);
            Dimension var14 = var13.getPreferredSize();
            int var15 = var8 + var14.width;
            if (var15 >= var11) {
               if (var9 == 0) {
                  var13.setBounds(var8, var7, var14.width, var14.height);
                  var7 += var14.height;
                  var8 = var3.left;
               } else {
                  var7 += var10 + this.vGap;
                  var8 = var3.left;
                  var13.setBounds(var8, var7, var14.width, var14.height);
                  var8 += var14.width;
                  var9 = 1;
               }
            } else {
               if (var9 > 0) {
                  var8 += this.hGap;
               }

               var13.setBounds(var8, var7, var14.width, var14.height);
               var10 = Math.max(var10, var14.height);
               var8 += var14.width;
               var9++;
            }
         }
      }
   }

   private Dimension layoutSize(Container var1) {
      Insets var2 = var1.getInsets();
      Dimension var3 = new Dimension(var2.left + var2.right, var2.top + var2.bottom);
      int var4 = var1.getComponentCount();
      if (var4 == 0) {
         return var3;
      }

      Component var5 = var1.getComponent(var4 - 1);
      Dimension var6 = var5.getPreferredSize();
      int var7 = var1.getWidth() - var2.left - var2.right - var6.width - this.hGap;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      int var12 = 1;

      for (int var13 = 0; var13 < var4 - 1; var13++) {
         Component var14 = var1.getComponent(var13);
         Dimension var15 = var14.getPreferredSize();
         var9 += var15.width;
         if (var9 >= var7) {
            var12++;
            if (var10 == 0) {
               var11 += var15.height;
               var9 = 0;
               var8 = 0;
            } else {
               var11 += var8;
               var10 = 1;
               var9 = var15.width;
               var8 = var15.height;
            }
         } else {
            if (var10 > 0) {
               var9 += this.hGap;
            }

            var10++;
            var8 = Math.max(var8, var15.height);
         }
      }

      var11 += var8 + this.vGap * var12 + this.vGap;
      var3.height = Math.max(var11, var6.height);
      return var3;
   }
}
