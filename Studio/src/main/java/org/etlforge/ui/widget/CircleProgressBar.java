package org.etlforge.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.etlforge.ui.QuickFrame;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitIndicator;

public class CircleProgressBar extends JComponent implements WaitIndicator {
   private boolean indeterminate;
   private int value;
   private Timer timer;
   private int numberOfCircles;
   private int currentCircle;
   private double[] angularPositions;
   private Color[] colors;
   private final int circleRadius = 9;

   public CircleProgressBar() {
      this.setNumberOfCircles(10);
   }

   public int getNumberOfCircles() {
      return this.numberOfCircles;
   }

   public void setNumberOfCircles(int var1) {
      this.numberOfCircles = var1;
      this.angularPositions = new double[var1];

      for (int var2 = 0; var2 < var1; var2++) {
         this.angularPositions[var2] = var2 * 2 * Math.PI / var1;
      }

      this.colors = new Color[var1];

      for (int var5 = 0; var5 < var1; var5++) {
         int var3 = (int)(250.0 * Math.sin((Math.PI / 2) * var5 / var1));
         int var4 = var5 == 0 ? 0 : var1 - var5;
         this.colors[var4] = new Color(var3, var3, var3);
      }
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int var1) {
      this.value = var1;
   }

   public boolean isIndeterminate() {
      return this.indeterminate;
   }

   public void setIndeterminate(boolean var1) {
      this.indeterminate = var1;
   }

   @Override
   public void start() {
      if (this.timer == null) {
         this.timer = new Timer(125, new CircleProgressBar.Worker());
         this.timer.setRepeats(true);
         this.timer.start();
         this.repaint();
      }
   }

   @Override
   public void stop() {
      if (this.timer != null) {
         this.timer.stop();
         this.timer = null;
         this.currentCircle = 0;
         this.repaint();
      }
   }

   @Override
   public JComponent getUi() {
      return this;
   }

   @Override
   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      if (this.timer != null) {
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         RenderingHints var4 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         int var5 = this.getWidth() / 2;
         int var6 = this.getHeight() / 2;
         int var7 = Math.min(var5, var6) - 9 - 2;

         for (int var8 = 0; var8 < this.numberOfCircles; var8++) {
            int var9 = 0;
            var9 = var8 - this.currentCircle;
            if (var9 < 0) {
               var9 += this.numberOfCircles;
            }

            int var10 = var5 + (int)(var7 * Math.sin(this.angularPositions[var8]));
            int var11 = var6 - (int)(var7 * Math.cos(this.angularPositions[var8]));
            var2.setColor(this.colors[var9]);
            var2.fillOval(var10 - 9, var11 - 9, 9, 9);
         }

         var2.setRenderingHints(var4);
         var2.setColor(var3);
      }
   }

   public static void main(String[] var0) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            new CircleProgressBar.Tester().start();
         }
      });
   }

   private static class Tester {
      private Tester() {
      }

      public void start() {
         final CircleProgressBar var1 = new CircleProgressBar();
         var1.setPreferredSize(new Dimension(75, 75));
         JPanel var2 = new JPanel();
         var2.add(var1);
         JButton var3 = new JButton("Start");
         var3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               var1.start();
            }
         });
         var2.add(var3);
         JButton var4 = new JButton("Stop");
         var4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               var1.stop();
            }
         });
         var2.add(var4);
         QuickFrame.show(var2, this.getClass().getName());
      }
   }

   private class Worker implements ActionListener {
      private Worker() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         CircleProgressBar.this.currentCircle = (CircleProgressBar.this.currentCircle + 1) % CircleProgressBar.this.numberOfCircles;
         CircleProgressBar.this.repaint();
      }
   }
}
