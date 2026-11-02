package org.etlforge.ui.util;

import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import javax.swing.Action;

public abstract class AbstractActionPropertyChangeListener implements PropertyChangeListener {
   private static ReferenceQueue<Object> refQueue;
   private WeakReference<Object> observer;
   private Action actionListenedTo;

   protected AbstractActionPropertyChangeListener(Object var1, Action var2) {
      this.actionListenedTo = var2;
      this.observer = new WeakReference<>(var1, getRefQueue());
   }

   private static synchronized ReferenceQueue<Object> getRefQueue() {
      if (refQueue == null) {
         refQueue = new ReferenceQueue<>();
      }

      return refQueue;
   }

   protected final Object getObservingButton() {
      return this.observer.get();
   }

   protected final Action getActionListenedTo() {
      return this.actionListenedTo;
   }
}
