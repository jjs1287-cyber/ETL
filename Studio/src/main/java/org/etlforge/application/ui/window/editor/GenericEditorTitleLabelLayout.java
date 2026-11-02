package org.etlforge.application.ui.window.editor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

class GenericEditorTitleLabelLayout implements LayoutManager {
   @Override
   public void layoutContainer(Container var1) {
      synchronized (var1.getTreeLock()) {
         Insets var3 = var1.getInsets();
         int var4 = var1.getWidth() - var3.left - var3.right;
         int var5 = var1.getHeight() - var3.top - var3.bottom;
         int var6 = var3.left;

         for (Component var10 : var1.getComponents()) {
            Dimension var11 = var10.getPreferredSize();
            int var12 = var5 - var3.bottom - var11.height;
            int var13 = Math.min(var11.width, var4 - var6);
            var10.setBounds(var6, var12, var13, var11.height);
            var6 += var13;
            if (var6 > var4) {
               break;
            }
         }
      }
   }

   @Override
   public Dimension minimumLayoutSize(Container var1) {
      synchronized (var1.getTreeLock()) {
         int var3 = 0;
         int var4 = 0;

         for (Component var8 : var1.getComponents()) {
            Dimension var9 = var8.getMinimumSize();
            var3 += var9.width;
            var4 = Math.max(var4, var9.height);
         }

         return new Dimension(var3, var4);
      }
   }

   @Override
   public Dimension preferredLayoutSize(Container var1) {
      synchronized (var1.getTreeLock()) {
         int var3 = 0;
         int var4 = 0;

         for (Component var8 : var1.getComponents()) {
            Dimension var9 = var8.getPreferredSize();
            var3 += var9.width;
            var4 = Math.max(var4, var9.height);
         }

         return new Dimension(var3, var4);
      }
   }

   @Override
   public void addLayoutComponent(String var1, Component var2) {
   }

   @Override
   public void removeLayoutComponent(Component var1) {
   }
}
