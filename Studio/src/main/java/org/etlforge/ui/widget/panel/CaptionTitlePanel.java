package org.etlforge.ui.widget.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public class CaptionTitlePanel implements UiProvider {
   private final JPanel panel;

   public CaptionTitlePanel(JComponent var1, int var2) {
      this.panel = new CaptionTitlePanel.PanelImpl(var1, var2);
   }

   public CaptionTitlePanel(UiProvider var1, int var2) {
      this(InvisiblePanel.of(var1), var2);
   }

   public CaptionTitlePanel(String var1, int var2) {
      this(new JLabel(var1), var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
   }

   private static class PanelImpl extends JPanel {
      private final JComponent label;

      public PanelImpl(JComponent var1, int var2) {
         this.label = var1;
         InvisiblePanel.convert(this);
         this.add(Box.createRigidArea(new Dimension(var2, 0)));
         this.add(var1);
         this.add(Box.createHorizontalGlue());
      }

      @Override
      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Color var2 = var1.getColor();
         Rectangle var3 = this.label.getBounds();
         int var4 = 0;
         int var5 = var3.y + var3.height / 2;
         int var6 = var3.x - 5;
         byte var7 = 3;
         var1.setColor(this.getBackground().darker());
         var1.drawRect(var4, var5, var6 - 2, var7 - 2);
         var1.setColor(this.getBackground().brighter());
         var1.drawLine(var4 + 1, var5 + var7 - 3, var4 + 1, var5 + 1);
         var1.drawLine(var4 + 1, var5 + 1, var6 - 3, var5 + 1);
         var1.drawLine(var4, var5 + var7 - 1, var6 - 1, var5 + var7 - 1);
         var1.drawLine(var4 + var6 - 1, var5 + var7 - 1, var4 + var6 - 1, var5);
         var4 = var3.x + var3.width + 5;
         var6 = this.getWidth();
         var1.setColor(this.getBackground().darker());
         var1.drawRect(var4, var5, var6 - 2, var7 - 2);
         var1.setColor(this.getBackground().brighter());
         var1.drawLine(var4 + 1, var5 + var7 - 3, var4 + 1, var5 + 1);
         var1.drawLine(var4 + 1, var5 + 1, var6 - 3, var5 + 1);
         var1.drawLine(var4, var5 + var7 - 1, var6 - 1, var5 + var7 - 1);
         var1.drawLine(var4 + var6 - 1, var5 + var7 - 1, var4 + var6 - 1, var5);
         var1.setColor(var2);
      }
   }
}
