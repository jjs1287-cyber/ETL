package org.etlforge.ui.util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.Empty;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.event.ApplicationMessageListener;

public final class StatusPanel extends JPanel implements ApplicationMessageListener {
   private static final long DEFAULT_TIME = 5000L;
   private JLabel statusLabel;
   private long messageRemainingInMilliSeconds = 5000L;
   private final Stack<ApplicationMessageEvent> messageStack;
   private ApplicationMessageEvent currentMessage;
   private Icon emptyIcon = Empty.icon(16, 16);
   private Icon infoIcon;
   private Icon warningIcon;
   private Icon errorIcon;

   public StatusPanel() {
      this.messageStack = new Stack<>();
      this.initUi();
   }

   private void initUi() {
      this.setLayout(new BorderLayout());
      this.setBorder(BorderFactory.createEtchedBorder());
      this.statusLabel = new JLabel(" ", this.emptyIcon, 2);
      this.add(this.statusLabel, "West");
   }

   public void setMessageRemainingInMilliSeconds(long var1) {
      this.messageRemainingInMilliSeconds = var1;
   }

   public void postMessage(Object var1, String var2, int var3) {
      this.incomingMessage(new ApplicationMessageEvent(var1, var2, var3));
   }

   private void postMessageImpl(ApplicationMessageEvent var1) {
      if (var1.getLifeTime() == ApplicationMessageEvent.LIFETIME_INDEFINITE) {
         this.messageStack.clear();
      } else {
         ApplicationMessageEvent.LifeTime var2 = var1.getLifeTime();
         long var3 = var2 == ApplicationMessageEvent.LIFETIME_HANDLER_DEPENDANT ? this.messageRemainingInMilliSeconds : var2.getMilliSeconds();
         if (var3 <= 0L || var1.getMessage().length() == 0) {
            this.removeCurrentMessage();
            return;
         }

         DelayedJob.createAndSchedule((int)var3, new StatusPanel.ClearMessage(var1));
      }

      this.messageStack.push(var1);
      this.setCurrentMessage(var1);
   }

   private void showNextMessage() {
      if (this.messageStack.isEmpty()) {
         this.setCurrentMessage(null);
      } else {
         ApplicationMessageEvent var1 = this.messageStack.peek();
         if (var1 != this.currentMessage) {
            this.setCurrentMessage(var1);
         }
      }
   }

   private void removeCurrentMessage() {
      if (!this.messageStack.isEmpty()) {
         ApplicationMessageEvent var1 = this.messageStack.peek();
         if (var1.getLifeTime() != ApplicationMessageEvent.LIFETIME_INDEFINITE) {
            this.messageStack.pop();
            if (this.messageStack.isEmpty()) {
               var1 = null;
            } else {
               var1 = this.messageStack.peek();
            }
         }

         this.setCurrentMessage(var1);
      }
   }

   private void setCurrentMessage(ApplicationMessageEvent var1) {
      this.currentMessage = var1;
      if (var1 == null) {
         this.statusLabel.setIcon(this.emptyIcon);
         this.statusLabel.setText(" ");
      } else if (var1.getMessageType() == 0 && var1.getMessage().trim().length() == 0) {
         this.statusLabel.setIcon(this.emptyIcon);
         this.statusLabel.setText(" ");
      } else {
         this.statusLabel.setIcon(this.getIcon(var1));
         this.statusLabel.setText(var1.getMessage());
      }
   }

   private Icon getIcon(ApplicationMessageEvent var1) {
      switch (var1.getMessageType()) {
         case 0:
            return this.infoIcon;
         case 1:
            return this.warningIcon;
         case 2:
         default:
            return null;
         case 3:
            return this.errorIcon;
      }
   }

   @Override
   public void incomingMessage(final ApplicationMessageEvent var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            StatusPanel.this.postMessageImpl(var1);
         }
      });
   }

   public void setTextFont(Font var1) {
      this.statusLabel.setFont(var1);
   }

   public void setInfoIcon(Icon var1) {
      this.infoIcon = var1;
   }

   public void setWarningIcon(Icon var1) {
      this.warningIcon = var1;
   }

   public void setErrorIcon(Icon var1) {
      this.errorIcon = var1;
   }

   private class ClearMessage implements Runnable {
      private final ApplicationMessageEvent event;

      public ClearMessage(ApplicationMessageEvent var2) {
         this.event = var2;
      }

      @Override
      public void run() {
         StatusPanel.this.messageStack.remove(this.event);
         StatusPanel.this.showNextMessage();
      }
   }
}
