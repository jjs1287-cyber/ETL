package org.etlforge.ui.util.progress;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.Label;

public final class ProgressMessage {
   private String base;
   private StringBuilder current;
   private String maxMessage;
   private int maxDots;
   private int dotCount;
   private final Receiver<String> callback;
   private final ProgressMessage.AutomaticUpdater autoUpdater;

   public ProgressMessage(String var1) {
      this(var1, (Receiver<String>)null);
   }

   public ProgressMessage(String var1, final Label var2) {
      this(var1, new Receiver<String>() {
         public void handle(String var1) {
            var2.setText(var1);
         }
      });
   }

   public ProgressMessage(String var1, Receiver<String> var2) {
      this.callback = var2 != null ? var2 : new NoReceiver<>();
      this.maxDots = 8;
      this.autoUpdater = new ProgressMessage.AutomaticUpdater();
      this.reset(var1, false);
   }

   public String getText() {
      return this.current.toString();
   }

   public String getBaseMessage() {
      return this.base;
   }

   public String getMaxMessage() {
      return this.maxMessage;
   }

   public void setMaxDots(final int var1) {
      Preconditions.checkArgument(var1 > 0, "maxDots must be > 0 (was " + var1 + ")");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProgressMessage.this.maxDots = var1;
         }
      });
   }

   public void setText(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProgressMessage.this.reset(var1);
         }
      });
   }

   public void setUpdateAutomatically(int var1) {
      Preconditions.checkArgument(var1 > 0, "delay must be > 0 (was " + var1 + ")");
      this.autoUpdater.install(var1);
   }

   public void stopAutomaticUpdate() {
      this.autoUpdater.uninstall();
   }

   public void update() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProgressMessage.this.updateImpl();
         }
      });
   }

   private void updateImpl() {
      this.dotCount++;
      if (this.dotCount > this.maxDots) {
         this.dotCount = 0;
      }

      this.current = new StringBuilder(this.base);

      for (int var1 = 0; var1 < this.dotCount; var1++) {
         this.current.append(".");
      }

      this.notifyAcceptor();
   }

   private void reset(String var1) {
      this.reset(var1, true);
   }

   private void reset(String var1, boolean var2) {
      this.base = var1;
      this.current = new StringBuilder(var1);
      this.dotCount = 0;
      StringBuilder var3 = new StringBuilder(var1);

      for (int var4 = 0; var4 < this.maxDots; var4++) {
         var3.append(".");
      }

      this.maxMessage = var3.toString();
      if (var2) {
         this.notifyAcceptor();
      }
   }

   private void notifyAcceptor() {
      this.callback.handle(this.current.toString());
   }

   @Override
   public String toString() {
      return this.getText();
   }

   private class AutomaticUpdater implements ActionListener {
      private Timer updateTimer;

      private AutomaticUpdater() {
      }

      public void install(final int var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AutomaticUpdater.this.installImpl(var1);
            }
         });
      }

      public void uninstall() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AutomaticUpdater.this.uninstallImpl();
            }
         });
      }

      private void installImpl(int var1) {
         assert EventQueue.isDispatchThread() : "This method must be called on the EDT";
         if (this.updateTimer == null) {
            this.updateTimer = new Timer(var1, this);
            this.updateTimer.setRepeats(true);
            this.updateTimer.start();
         }
      }

      private void uninstallImpl() {
         assert EventQueue.isDispatchThread() : "This method must be called on the EDT";
         if (this.updateTimer != null) {
            this.updateTimer.stop();
            this.updateTimer = null;
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProgressMessage.this.update();
      }
   }
}
