package org.etlforge.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class DialogLayout implements LayoutManager {
   protected int m_iDivider = -1;
   protected int m_iGapH = 10;
   protected int m_iGapV = 5;

   public DialogLayout() {
      this(10, 5);
   }

   public DialogLayout(int var1, int var2) {
      this.m_iGapH = var1;
      this.m_iGapV = var2;
   }

   @Override
   public void removeLayoutComponent(Component var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void layoutContainer(Container var1) {
      int var2 = this.getDivider(var1);
      Insets var3 = var1.getInsets();
      int var4 = var1.getWidth() - var3.left - var3.right - var2;
      int var5 = var3.left;
      int var6 = var3.top;

      for (byte var7 = 1; var7 < var1.getComponentCount(); var7 += 2) {
         Component var8 = var1.getComponent(var7 - 1);
         Component var9 = var1.getComponent(var7);
         Dimension var10 = var9.getPreferredSize();
         var8.setBounds(var5, var6, var2 - this.m_iGapH, var10.height);
         var9.setBounds(var5 + var2, var6, var4, var10.height);
         var6 += var10.height + this.m_iGapV;
      }
   }

   @Override
   public void addLayoutComponent(String var1, Component var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Dimension minimumLayoutSize(Container var1) {
      return this.preferredLayoutSize(var1);
   }

   @Override
   public Dimension preferredLayoutSize(Container var1) {
      int var2 = this.getDivider(var1);
      int var3 = 0;
      int var4 = 0;

      for (byte var5 = 1; var5 < var1.getComponentCount(); var5 += 2) {
         Component var6 = var1.getComponent(var5);
         Dimension var7 = var6.getPreferredSize();
         var3 = Math.max(var3, var7.width);
         var4 += var7.height + this.m_iGapV;
      }

      var4 -= this.m_iGapH;
      Insets var9 = var1.getInsets();
      return new Dimension(var2 + var3 + var9.left + var9.right, var4 + var9.top + var9.bottom);
   }

   public int getHGap() {
      return this.m_iGapH;
   }

   public int getVGap() {
      return this.m_iGapV;
   }

   public void setDivider(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("divider must be > 0 (supplied value: " + var1 + ").");
      }

      this.m_iDivider = var1;
   }

   public int getDivider() {
      return this.m_iDivider;
   }

   protected int getDivider(Container var1) {
      if (this.m_iDivider > 0) {
         return this.m_iDivider;
      }

      int var2 = 0;

      for (byte var3 = 0; var3 < var1.getComponentCount(); var3 += 2) {
         Component var4 = var1.getComponent(var3);
         Dimension var5 = var4.getPreferredSize();
         var2 = Math.max(var2, var5.width);
      }

      return var2 + this.m_iGapH;
   }
}
