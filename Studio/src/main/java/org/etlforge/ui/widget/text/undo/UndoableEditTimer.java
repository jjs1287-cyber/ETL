package org.etlforge.ui.widget.text.undo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.UndoableEditEvent;
import org.etlforge.ui.undo.UndoableEditSender;

public final class UndoableEditTimer {
   public static final int SEND_DELAY = 500;
   private static final UndoableEditTimer INSTANCE = new UndoableEditTimer();
   private UndoableEditTimer.SenderTimer timer;
   private UndoableEditTimer.EditSenderTask timerTask;
   private UndoableTextEditSender sender;

   private UndoableEditTimer() {
   }

   public static UndoableEditTimer getTimer() {
      return INSTANCE;
   }

   public void registerSender(UndoableTextEditSender var1) {
      if (var1 != this.sender) {
         this.sender = var1;
         if (this.timer != null) {
            this.timerTask.sendEdit();
            this.timer.stop();
         }

         this.timerTask = new UndoableEditTimer.EditSenderTask(this.sender);
         this.timer = new UndoableEditTimer.SenderTimer(this.timerTask);
      }
   }

   public void removeSender(UndoableEditSender var1) {
      if (var1 == this.sender) {
         this.sendNow();
         this.disposeTimer();
         this.sender = null;
      }
   }

   public void sendNow() {
      if (this.timer != null) {
         this.timerTask.sendEdit();
         this.timer.stop();
      }
   }

   private void disposeTimer() {
      this.timer = null;
      this.timerTask = null;
   }

   public void undoableEditHappened(UndoableEditEvent var1) {
      if (this.timer != null) {
         boolean var2 = this.timer.isRunning();
         if (var2) {
            this.timer.stop();
         }

         if (var2) {
            this.timer.restart();
         } else {
            this.timer.start();
         }
      }
   }

   private static final class EditSenderTask implements ActionListener {
      private final UndoableTextEditSender sender;
      private boolean editHasBeenSent;
      private boolean stopped;

      public EditSenderTask(UndoableTextEditSender var1) {
         this.sender = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.sendEdit();
      }

      public void sendEdit() {
         if (!this.editHasBeenSent) {
            if (!this.isStopped()) {
               this.sender.sendEdit();
            }

            this.editHasBeenSent = true;
         }
      }

      public synchronized boolean isStopped() {
         return this.stopped;
      }

      public synchronized void stop() {
         this.stopped = true;
      }

      public synchronized void restart() {
         this.stopped = false;
         this.editHasBeenSent = false;
      }
   }

   private static final class SenderTimer extends Timer {
      private final UndoableEditTimer.EditSenderTask task;

      public SenderTimer(UndoableEditTimer.EditSenderTask var1) {
         super(500, var1);
         this.setRepeats(false);
         this.task = var1;
      }

      @Override
      public void stop() {
         super.stop();
         this.task.stop();
      }

      @Override
      public void start() {
         super.start();
         this.task.restart();
      }

      @Override
      public void restart() {
         super.restart();
         this.task.restart();
      }
   }
}
