package org.etlforge.ui.widget.panel;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.CustomPaintable;

public class KongaPanel extends JPanel implements CustomPaintable {
   private ComponentPainter painter;
   private float alpha = 1.0F;

   public KongaPanel() {
      this((ComponentPainter)null);
   }

   public KongaPanel(ComponentPainter var1) {
      this.setPainter(var1);
   }

   public KongaPanel(LayoutManager var1) {
      this(var1, null);
   }

   public KongaPanel(LayoutManager var1, ComponentPainter var2) {
      super(var1);
      this.setPainter(var2);
   }

   public KongaPanel(boolean var1, ComponentPainter var2) {
      super(var1);
      this.setPainter(var2);
   }

   public KongaPanel(LayoutManager var1, boolean var2, ComponentPainter var3) {
      super(var1, var2);
      this.setPainter(var3);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      this.painter = var1;
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
   }

   public void setAlpha(float var1) {
      this.alpha = var1;
      this.repaint();
   }

   @Override
   public void paint(Graphics var1) {
      if (this.alpha < 1.0F) {
         Graphics2D var2 = (Graphics2D)var1;
         Composite var3 = var2.getComposite();
         var2.setComposite(AlphaComposite.getInstance(3, this.alpha));
         super.paint(var1);
         var2.setComposite(var3);
      } else {
         super.paint(var1);
      }
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }
}
