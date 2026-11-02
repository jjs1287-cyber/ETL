package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;

public class TipWindow extends Panel {
   private static final int max_line_number = 40;
   public Object m_obj;
   private final List<String> m_lines;
   private FontMetrics m_fm;

   public TipWindow(JPanel var1) {
      super(null);
      var1.add(this);
      this.setFont(StagingPanel.dataValueFont);
      this.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
      this.setBackground(UIManager.getColor("ToolTip.background"));
      this.setSize(1, 1);
      this.setVisible(false);
      this.m_lines = Lists.newArrayList();
   }

   public void showItAtXY(String var1, int var2, int var3, Object var4) {
      this.m_obj = var4;
      Dimension var5 = this.calculateTipSize(var1);
      this.setSize(var5.width, var5.height);
      Container var6 = this.getParent();
      var2 -= 2;
      if (var6 != null) {
         Dimension var7 = var6.getSize();
         if (var2 + var5.width > var7.width) {
            var2 = var7.width - var5.width;
         }

         if (var2 < 0) {
            var2 = 0;
         }
      }

      this.setLocation(var2, var3);
      this.setVisible(true);
   }

   @Override
   public void paint(Graphics var1) {
      for (int var2 = 0; var2 < this.m_lines.size(); var2++) {
         String var3 = this.m_lines.get(var2);
         var1.drawString(var3, 3, this.m_fm.getHeight() * (var2 + 1));
      }

      var1.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
   }

   private Dimension calculateTipSize(String var1) {
      this.m_lines.clear();
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      this.m_fm = this.getFontMetrics(this.getFont());
      StringTokenizer var5 = new StringTokenizer(var1, "\n");
      int var6 = 0;
      int var7 = 0;

      while (var5.hasMoreTokens()) {
         String var8;
         if (var6 >= 40) {
            var8 = "...";
            var1 = var1.substring(0, var7) + var8;
            this.m_lines.add(var8);
         } else {
            var8 = var5.nextToken();
            this.m_lines.add(var8);
         }

         try {
            var2 = this.m_fm.stringWidth(var8);
            var4 = var4 > var2 ? var4 : var2;
            var3 += this.m_fm.getHeight();
         } catch (Exception var10) {
            byte var11 = 50;
            var4 = var4 > var11 ? var4 : var11;
            var3 = 20;
         }

         if (var6 >= 40) {
            break;
         }

         var6++;
         var7 += var8.length() + 1;
      }

      var4 += 8;
      var3 += 8;
      return new Dimension(var4, var3);
   }
}

