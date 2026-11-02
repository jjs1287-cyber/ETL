package org.etlforge.ui.event;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class UndoableEditListeners extends AbstractListenerManager<UndoableEditListener> implements ListenerManager<UndoableEditListener> {
   protected UndoableEditListener createNotifier() {
      return new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            for (UndoableEditListener var3 : UndoableEditListeners.this.getListeners()) {
               var3.undoableEditHappened(var1);
            }
         }
      };
   }
}
