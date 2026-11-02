package org.etlforge.ui.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D.Double;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.util.UiUtils;

public abstract class GlassSheetPanel extends JComponent {
   private final int red;
   private final int green;
   private final int blue;
   private final int alpha;
   private int currentAlpha;

   public GlassSheetPanel(Color var1) {
      this.red = var1.getRed();
      this.green = var1.getGreen();
      this.blue = var1.getBlue();
      this.alpha = var1.getAlpha();
      this.currentAlpha = -1;
   }

   protected final int getCurrentAlpha() {
      return this.currentAlpha;
   }

   public final void setGlassSheetVisible(boolean var1) {
      this.currentAlpha = var1 ? this.alpha : -1;
      this.repaint();
   }

   public final void animateGlasSheetVisible(boolean var1) {
      if (!var1 || this.currentAlpha != this.alpha) {
         if (var1 || this.currentAlpha != -1) {
            this.animateTransition(var1);
         }
      }
   }

   private void animateTransition(boolean var1) {
      Animator var2 = this.createAnimator();
      if (var1) {
         var2.addTarget(new TimingTargetAdapter() {
            @Override
            public void timingEvent(float var1) {
               GlassSheetPanel.this.currentAlpha = (int)(GlassSheetPanel.this.alpha * var1);
               GlassSheetPanel.this.repaint();
            }

            @Override
            public void end() {
               GlassSheetPanel.this.currentAlpha = GlassSheetPanel.this.alpha;
            }
         });
      } else {
         final int var3 = this.currentAlpha;
         var2.addTarget(new TimingTargetAdapter() {
            @Override
            public void timingEvent(float var1) {
               int var2 = (int)(var1 * var3);
               GlassSheetPanel.this.currentAlpha = var3 - var2;
               GlassSheetPanel.this.repaint();
            }

            @Override
            public void end() {
               GlassSheetPanel.this.currentAlpha = -1;
            }
         });
      }

      var2.start();
   }

   protected Animator createAnimator() {
      Animator var1 = new Animator(150);
      var1.setResolution(25);
      var1.setAcceleration(0.3F);
      var1.setDeceleration(0.2F);
      return var1;
   }

   @Override
   public void paint(Graphics var1) {
      super.paint(var1);
      if (this.currentAlpha >= 0) {
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var1.getColor();
         Shape var4 = this.drawGlassSheet(var2);
         int var5 = this.getAlphaOfBorder();
         if (var5 > 0) {
            this.drawGlassSheetBorder(var2, var4, var5);
         }

         var2.setColor(var3);
      }
   }

   private Shape drawGlassSheet(Graphics2D var1) {
      var1.setColor(new Color(this.red, this.green, this.blue, this.currentAlpha));
      Shape var2 = this.getShapeOfGlassSheet();
      var1.fill(var2);
      return var2;
   }

   protected abstract Shape getShapeOfGlassSheet();

   protected int getAlphaOfBorder() {
      return -1;
   }

   private void drawGlassSheetBorder(Graphics2D var1, Shape var2, int var3) {
      RenderingHints var4 = var1.getRenderingHints();
      UiUtils.makeAntiAliased(var1);
      var1.setColor(new Color(this.red, this.green, this.blue, var3));
      var1.draw(var2);
      var1.setRenderingHints(var4);
   }

   public static class RoundedRect extends GlassSheetPanel {
      private final int margin;
      private final int roundness;

      public RoundedRect(Color var1, int var2, int var3) {
         super(var1);
         this.margin = var2;
         this.roundness = var3;
      }

      @Override
      protected final Shape getShapeOfGlassSheet() {
         return new Double(
            this.margin, this.margin, this.getWidth() - 2 * this.margin - 1, this.getHeight() - 2 * this.margin - 1, this.roundness, this.roundness
         );
      }
   }
}
