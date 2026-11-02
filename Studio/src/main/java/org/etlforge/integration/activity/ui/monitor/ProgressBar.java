package org.etlforge.integration.activity.ui.monitor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BoxBuilder;

final class ProgressBar implements ActionListener {
   private final JLabel[] labels;
   private final Icon on;
   private final Icon off;
   private int index = -1;
   private Timer liveTimer;
   private Timer stoppingTimer;
   private int interval = 100;

   public ProgressBar() {
      this.on = new ProgressBar.OnIcon();
      this.off = Empty.iconOfSameSize(this.on);
      this.labels = new JLabel[5];

      for (int var1 = 0; var1 < this.labels.length; var1++) {
         this.labels[var1] = new JLabel(this.off);
      }
   }

   public JComponent getUi() {
      return BoxBuilder.horizontal().addAllWithSpace(2, this.labels).nonOpaque().container();
   }

   public void setIntervalInMillis(int var1) {
      this.interval = var1;
   }

   public void start() {
      if (this.liveTimer == null) {
         if (this.stoppingTimer != null) {
            this.stopNow(this.stoppingTimer);
         }

         this.liveTimer = new Timer(this.interval, this);
         this.liveTimer.setInitialDelay(0);
         this.liveTimer.setRepeats(true);
         this.liveTimer.start();
      }
   }

   public void stop() {
      if (this.liveTimer != null) {
         if (this.atEndOfCycle()) {
            this.stopNow(this.liveTimer);
         } else {
            this.stopWhenAtEndOfCycle();
         }
      }
   }

   private boolean atEndOfCycle() {
      return this.index >= this.labels.length;
   }

   private void stopWhenAtEndOfCycle() {
      this.stoppingTimer = this.liveTimer;
      this.liveTimer = null;
   }

   private void stopNow(Timer var1) {
      assert var1 == this.liveTimer || var1 == this.stoppingTimer;
      var1.stop();
      this.index = -1;
      this.updateLabels();
      this.liveTimer = null;
      this.stoppingTimer = null;
   }

   private void updateLabels() {
      for (int var1 = 0; var1 < this.labels.length; var1++) {
         this.labels[var1].setIcon(var1 <= this.index ? this.on : this.off);
      }
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.index++;
      if (this.atEndOfCycle()) {
         if (this.stoppingTimer != null) {
            this.stopNow(this.stoppingTimer);
         } else {
            this.index = 0;
         }
      }

      this.updateLabels();
   }

   private static final class OnIcon implements Icon {
      private static final int SIZE = 6;

      private OnIcon() {
      }

      @Override
      public int getIconHeight() {
         return 6;
      }

      @Override
      public int getIconWidth() {
         return 6;
      }

      @Override
      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         Color var5 = var2.getColor();
         var2.setColor(Color.GREEN);
         ((Graphics2D)var2).fillRect(var3 + 1, var4 + 1, 4, 4);
         var2.setColor(Color.BLACK);
         var2.drawRect(var3, var4, 5, 5);
         var2.setColor(var5);
      }
   }
}
