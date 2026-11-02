package org.etlforge.ui.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;

public class DelayedDocumentContentChangeReaction implements Runnable {
   private int delay;
   private DelayedJob timer;
   private final DelayedDocumentContentChangeReaction.ChangeListener changeListener;
   private final PropertyChangeListener newDocumentListener;
   private final Runnable job;

   public DelayedDocumentContentChangeReaction(Runnable var1) {
      this(200, var1);
   }

   protected DelayedDocumentContentChangeReaction() {
      this(200);
   }

   protected DelayedDocumentContentChangeReaction(int var1) {
      this(var1, null);
   }

   public DelayedDocumentContentChangeReaction(int var1, Runnable var2) {
      this.setDelay(var1);
      this.job = var2;
      this.changeListener = new DelayedDocumentContentChangeReaction.ChangeListener();
      this.newDocumentListener = new DelayedDocumentContentChangeReaction.NewDocumentAssignedListener();
   }

   public final void setDelay(int var1) {
      this.delay = ArgCheck.checkNotNegative(var1, "delay");
   }

   public void sendNow() {
      this.cancel();
      this.job.run();
   }

   public void cancel() {
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }
   }

   public final Disposable install(final JTextComponent var1) {
      var1.getDocument().addDocumentListener(this.changeListener);
      var1.addPropertyChangeListener("document", this.newDocumentListener);
      return new Disposable() {
         @Override
         public void dispose() {
            DelayedDocumentContentChangeReaction.this.uninstall(var1);
         }
      };
   }

   public final void uninstall(JTextComponent var1) {
      var1.getDocument().removeDocumentListener(this.changeListener);
      var1.removePropertyChangeListener("document", this.newDocumentListener);
   }

   @Override
   public void run() {
      if (this.job == null) {
         throw new IllegalStateException("Must either provide a Runnable, or override the run() method.");
      }

      this.job.run();
   }

   public DocumentListener asDocumentListener() {
      return this.changeListener;
   }

   private class ChangeListener extends DocumentContentChangeListener {
      private ChangeListener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         this.stopTimer();
         DelayedDocumentContentChangeReaction.RunnableWrapper var2 = DelayedDocumentContentChangeReaction.this.new RunnableWrapper();
         if (DelayedDocumentContentChangeReaction.this.delay > 0) {
            DelayedDocumentContentChangeReaction.this.timer = new DelayedJob(DelayedDocumentContentChangeReaction.this.delay, var2);
            DelayedDocumentContentChangeReaction.this.timer.schedule();
         } else {
            var2.run();
         }
      }

      private void stopTimer() {
         if (DelayedDocumentContentChangeReaction.this.timer != null) {
            DelayedDocumentContentChangeReaction.this.timer.cancel();
            DelayedDocumentContentChangeReaction.this.timer = null;
         }
      }
   }

   private class NewDocumentAssignedListener implements PropertyChangeListener {
      private NewDocumentAssignedListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "document".equals(var1.getPropertyName());
         Document var2 = (Document)var1.getOldValue();
         if (var2 != null) {
            var2.removeDocumentListener(DelayedDocumentContentChangeReaction.this.changeListener);
         }

         Document var3 = (Document)var1.getNewValue();
         if (var3 != null) {
            var3.addDocumentListener(DelayedDocumentContentChangeReaction.this.changeListener);
         }
      }
   }

   private class RunnableWrapper implements Runnable {
      private RunnableWrapper() {
      }

      @Override
      public void run() {
         DelayedDocumentContentChangeReaction.this.timer = null;
         DelayedDocumentContentChangeReaction.this.run();
      }
   }
}
