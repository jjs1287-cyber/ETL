package org.etlforge.ui.util.time;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.ui.widget.LabelDecorator;

public final class CurrentDateTimeDisplayer implements UiProvider {
   private Timer timer;
   private final JLabel label;
   private DateFormat format;
   private TimeZone timeZone;
   private final Object lock = new Object();
   private int delay = 1000;

   public CurrentDateTimeDisplayer() {
      this(2, 2);
   }

   public CurrentDateTimeDisplayer(int var1, int var2) {
      this.format = DateFormat.getDateTimeInstance(var1, var2);
      this.label = new JLabel(this.getCurrentDisplayString());
   }

   private synchronized String getCurrentDisplayString() {
      return this.format.format(new Date());
   }

   public synchronized void start() {
      if (this.timer == null) {
         ActionListener var1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               CurrentDateTimeDisplayer.this.label.setText(CurrentDateTimeDisplayer.this.getCurrentDisplayString());
            }
         };
         this.timer = new Timer(this.delay, var1);
         this.timer.start();
      }
   }

   public synchronized void stop() {
      if (this.timer != null) {
         this.timer.stop();
         this.timer = null;
      }
   }

   public synchronized void setUpdateInterval(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("milliseconds must be > 0 (was " + var1 + ")");
      }

      this.delay = var1;
      if (this.timer != null) {
         this.timer.setDelay(var1);
      }
   }

   public void setTimeZone(TimeZone var1) {
      TimeZone var2 = getSafeTimeZone(var1);
      synchronized (this.lock) {
         this.timeZone = var2;
         this.format.setTimeZone(var2);
      }
   }

   private static TimeZone getSafeTimeZone(TimeZone var0) {
      return var0 != null ? var0 : TimeZone.getDefault();
   }

   public void setFormat(DateFormat var1) {
      synchronized (this.lock) {
         this.format = var1;
         this.format.setTimeZone(this.timeZone);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.label);
   }

   @Override
   public void requestFocus() {
   }

   public void setEditable(boolean var1) {
      if (var1) {
         this.start();
      } else {
         this.stop();
      }
   }

   public void setAppearance(final LabelDecorator var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            var1.decorate(new LabelAdapter(CurrentDateTimeDisplayer.this.label));
         }
      });
   }
}
